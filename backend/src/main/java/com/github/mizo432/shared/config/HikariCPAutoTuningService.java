package com.github.mizo432.shared.config;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * HikariCP接続プールの実行時オートチューニングサービスです。
 *
 * <p>このサービスは、HikariCPの接続プールメトリクスとMicrometerを統合し、 自律的に最適なプール設定を計算して適用します。Prometheusは使用せず、
 * アプリケーション内で完結する予測アルゴリズムを採用しています。
 *
 * <p>主な機能:
 *
 * <ul>
 *   <li>HikariCP MBeanメトリクスとMicrometerの統合収集
 *   <li>指数移動平均(EWA)による接続数予測
 *   <li>接続タイムアウト検知と自動応答
 *   <li>CPU使用率・DBクエリ遅延に基づく適応的調整
 *   <li>安全ネット（境界値チェック、フォールバックデフォルト）
 *   <li>idleTimeout・maxLifetimeの動的調整（オプション）
 * </ul>
 */
@Service
@ConditionalOnBean(HikariDataSource.class)
@ConditionalOnProperty(prefix = "hikari.auto-tuning", name = "enabled", havingValue = "true")
public class HikariCPAutoTuningService {

  private static final Logger logger = LoggerFactory.getLogger(HikariCPAutoTuningService.class);

  private final HikariDataSource dataSource;
  private final HikariCPAutoTuningProperties properties;
  private final Optional<MeterRegistry> meterRegistry;

  // 履歴データ用リングバッファ
  private final Deque<PoolMetricsSnapshot> metricsHistory;
  private final int historyWindowSize;
  // タイムアウト連続検知カウンター
  private final AtomicInteger consecutiveTimeouts = new AtomicInteger(0);
  // 指数移動平均用状態
  private double ewaActiveConnections;
  private double ewaPendingThreads;
  // 前回の調整値（ステップ制限用）
  private int lastMaxPoolSize;
  private int lastMinIdle;

  /**
   * コンストラクタ。
   *
   * @param dataSource HikariCPデータソース
   * @param properties オートチューニング設定プロパティ
   * @param meterRegistry Micrometerメトリクスレジストリ（オプション）
   */
  public HikariCPAutoTuningService(
      HikariDataSource dataSource,
      HikariCPAutoTuningProperties properties,
      Optional<MeterRegistry> meterRegistry) {
    this.dataSource = dataSource;
    this.properties = properties;
    this.meterRegistry = meterRegistry;
    this.historyWindowSize = properties.getHistoryWindowSize();
    this.metricsHistory = new ArrayDeque<>(historyWindowSize);
    this.lastMaxPoolSize = dataSource.getMaximumPoolSize();
    this.lastMinIdle = dataSource.getMinimumIdle();

    logger.info(
        "HikariCP Auto-Tuning Service initialized - interval: {}s, history: {}, max-pool-limit: {}, min-idle-limit: {}",
        properties.getIntervalSeconds(),
        historyWindowSize,
        properties.getMaxPoolSizeLimit(),
        properties.getMinIdleLimit());
  }

  /** 定期的に実行されるオートチューニングタスクです。 接続プールのメトリクスを収集し、最適なプール設定を計算して適用します。 */
  @Scheduled(
      fixedDelayString = "${hikari.auto-tuning.interval-seconds:60000}",
      initialDelayString = "${hikari.auto-tuning.interval-seconds:60000}")
  public void autoTune() {
    try {
      // メトリクス収集
      PoolMetricsSnapshot snapshot = collectMetrics();
      metricsHistory.addLast(snapshot);
      if (metricsHistory.size() > historyWindowSize) {
        metricsHistory.removeFirst();
      }

      // 指数移動平均の更新
      updateExponentialMovingAverage(snapshot);

      // タイムアウト検知とフォールバック判定
      if (detectConnectionTimeouts(snapshot)) {
        handleTimeoutFallback();
        return;
      }

      // 最適なプールサイズ計算
      int optimalMaxPoolSize = calculateOptimalMaxPoolSize(snapshot);
      int optimalMinIdle = calculateOptimalMinIdle(optimalMaxPoolSize);

      // ステップ制限の適用
      optimalMaxPoolSize = applyStepLimit(lastMaxPoolSize, optimalMaxPoolSize);
      optimalMinIdle = applyStepLimit(lastMinIdle, optimalMinIdle);

      // 安全ネットチェック
      if (!isWithinSafeBounds(optimalMaxPoolSize, optimalMinIdle)) {
        logger.warn(
            "⚠️ Prediction out of safe bounds! max={}, minIdle={} - Falling back to defaults",
            optimalMaxPoolSize,
            optimalMinIdle);
        fallbackToSafeDefaults();
        return;
      }

      // 現在の設定値を取得
      int currentMaxPoolSize = dataSource.getMaximumPoolSize();
      int currentMinIdle = dataSource.getMinimumIdle();

      logger.info(
          "HikariCP Auto-Tuning - Current: max={}, minIdle={} | Predicted: max={}, minIdle={}",
          currentMaxPoolSize,
          currentMinIdle,
          optimalMaxPoolSize,
          optimalMinIdle);

      // 設定値の適用
      if (optimalMaxPoolSize != currentMaxPoolSize) {
        dataSource.setMaximumPoolSize(optimalMaxPoolSize);
        lastMaxPoolSize = optimalMaxPoolSize;
        logger.info(
            "🔁 Updated maximum-pool-size: {} -> {}", currentMaxPoolSize, optimalMaxPoolSize);
      }

      if (optimalMinIdle != currentMinIdle) {
        dataSource.setMinimumIdle(optimalMinIdle);
        lastMinIdle = optimalMinIdle;
        logger.info("🔁 Updated minimum-idle: {} -> {}", currentMinIdle, optimalMinIdle);
      }

      // オプション: idleTimeoutの動的調整
      if (properties.isAdjustIdleTimeout()) {
        adjustIdleTimeout(snapshot);
      }

      // オプション: maxLifetimeの動的調整
      if (properties.isAdjustMaxLifetime()) {
        adjustMaxLifetime(snapshot);
      }

    } catch (Exception e) {
      logger.warn("Failed to auto-tune HikariCP pool: {}", e.getMessage(), e);
    }
  }

  /**
   * 接続プールのメトリクスを収集してスナップショットを作成します。
   *
   * @return 収集したメトリクスを含むスナップショット
   */
  private PoolMetricsSnapshot collectMetrics() {
    try {
      long totalConnections = getTotalConnections();
      long idleConnections = getIdleConnections();
      long activeConnections = getActiveConnections();
      long pendingThreads = getPendingThreads();
      long connectionTimeouts = getConnectionTimeouts();

      // Micrometerから追加メトリクスを収集
      double cpuUsage = collectCpuUsage();
      double dbQueryLatency = collectDbQueryLatency();

      PoolMetricsSnapshot snapshot =
          new PoolMetricsSnapshot(
              System.currentTimeMillis(),
              totalConnections,
              idleConnections,
              activeConnections,
              pendingThreads,
              connectionTimeouts,
              cpuUsage,
              dbQueryLatency);

      logger.debug(
          "Collected metrics - total: {}, idle: {}, active: {}, pending: {}, timeouts: {}, cpu: {}%, latency: {}ms",
          totalConnections,
          idleConnections,
          activeConnections,
          pendingThreads,
          connectionTimeouts,
          cpuUsage,
          dbQueryLatency);

      return snapshot;

    } catch (Exception e) {
      logger.warn("Failed to collect HikariCP metrics: {}", e.getMessage());
      // エラー時はゼロ値のスナップショットを返す
      return new PoolMetricsSnapshot(System.currentTimeMillis(), 0, 0, 0, 0, 0, 0.0, 0.0);
    }
  }

  /**
   * 指数移動平均(EWA)を更新します。 最近のデータに重きを置くことで、急激な負荷変化に素早く対応できます。
   *
   * @param snapshot 最新のメトリクススナップショット
   */
  private void updateExponentialMovingAverage(PoolMetricsSnapshot snapshot) {
    double alpha = properties.getEwaAlpha();

    ewaActiveConnections =
        alpha * snapshot.getActiveConnections() + (1 - alpha) * ewaActiveConnections;
    ewaPendingThreads = alpha * snapshot.getPendingThreads() + (1 - alpha) * ewaPendingThreads;
  }

  /**
   * 接続タイムアウトを検知します。
   *
   * @param snapshot 最新のメトリクススナップショット
   * @return タイムアウトが検知された場合はtrue
   */
  private boolean detectConnectionTimeouts(PoolMetricsSnapshot snapshot) {
    if (snapshot.getConnectionTimeouts() > 0) {
      int consecutive = consecutiveTimeouts.incrementAndGet();
      logger.warn(
          "⚠️ Connection timeout detected! Consecutive count: {}, threshold: {}",
          consecutive,
          properties.getTimeoutFallbackThreshold());
      return consecutive >= properties.getTimeoutFallbackThreshold();
    } else {
      consecutiveTimeouts.set(0);
      return false;
    }
  }

  /** タイムアウト連続検知時のフォールバック処理を実行します。 安全なデフォルト値にプールサイズを戻します。 */
  private void handleTimeoutFallback() {
    int fallbackSize = properties.getFallbackPoolSize();
    logger.warn(
        "🚨 Connection pool fallback triggered! Setting max-pool-size to safe default: {}",
        fallbackSize);
    dataSource.setMaximumPoolSize(fallbackSize);
    lastMaxPoolSize = fallbackSize;
    consecutiveTimeouts.set(0);
  }

  /** 安全なデフォルト値にフォールバックします。 */
  private void fallbackToSafeDefaults() {
    int fallbackSize = properties.getFallbackPoolSize();
    dataSource.setMaximumPoolSize(fallbackSize);
    dataSource.setMinimumIdle(Math.max(fallbackSize / 3, properties.getMinIdleLimit()));
    lastMaxPoolSize = fallbackSize;
    lastMinIdle = Math.max(fallbackSize / 3, properties.getMinIdleLimit());
  }

  /**
   * 1回の調整ステップ数を制限します。 ノイズによる過剰な調整を防ぎます。
   *
   * @param currentValue 現在の値
   * @param targetValue 目標値
   * @return 制限された目標値
   */
  private int applyStepLimit(int currentValue, int targetValue) {
    int maxStep = properties.getMaxAdjustmentStep();
    int diff = targetValue - currentValue;
    if (Math.abs(diff) > maxStep) {
      return currentValue + Integer.signum(diff) * maxStep;
    }
    return targetValue;
  }

  /**
   * 予測値が安全な範囲内にあるかチェックします。
   *
   * @param maxPoolSize 予測されたmaximum-pool-size
   * @param minIdle 予測されたminimum-idle
   * @return 安全な範囲内にある場合はtrue
   */
  private boolean isWithinSafeBounds(int maxPoolSize, int minIdle) {
    return maxPoolSize >= 1
        && maxPoolSize <= properties.getMaxPoolSizeLimit()
        && minIdle >= properties.getMinIdleLimit()
        && minIdle <= maxPoolSize;
  }

  /**
   * 直近の接続使用パターンとMicrometerメトリクスに基づいて最適なmaximum-pool-sizeを計算します。
   *
   * <p>計算ロジック:
   *
   * <ul>
   *   <li>指数移動平均(EWA)によるアクティブ接続数予測
   *   <li>待機スレッドの累積に基づくキャパシティ追加
   *   <li>接続タイムアウト発生時の安全マージン追加
   *   <li>CPU使用率閾値超え時の接続数抑制
   *   <li>DBクエリ遅延閾値超え時のプール拡大検討
   *   <li>設定された上限(maxPoolSizeLimit)を超えないように制限
   * </ul>
   *
   * @param snapshot 最新のメトリクススナップショット
   * @return 最適なmaximum-pool-size
   */
  private int calculateOptimalMaxPoolSize(PoolMetricsSnapshot snapshot) {
    // EWAによるアクティブ接続数予測
    int predictedSize = (int) Math.ceil(ewaActiveConnections);

    // 待機スレッドが累積している場合は、さらにキャパシティを追加
    if (ewaPendingThreads > 0) {
      predictedSize += Math.min((int) Math.ceil(ewaPendingThreads) + 2, 10);
    }

    // 接続タイムアウトが発生した場合は安全マージンを追加
    if (snapshot.getConnectionTimeouts() > 0) {
      predictedSize += properties.getTimeoutSafetyMargin();
    }

    // 履歴からのピーク接続数を考慮
    int peakActive = 0;
    for (PoolMetricsSnapshot s : metricsHistory) {
      if (s.getActiveConnections() > peakActive) {
        peakActive = (int) s.getActiveConnections();
      }
    }
    // ピーク接続数に20%のバッファ
    int peakBasedSize = (int) Math.ceil(peakActive * 1.2);
    predictedSize = Math.max(predictedSize, peakBasedSize);

    // CPU使用率が高い場合は接続数を抑制
    if (snapshot.getCpuUsage() > properties.getCpuThreshold()) {
      double reductionFactor =
          1.0 - ((snapshot.getCpuUsage() - properties.getCpuThreshold()) / 100.0);
      predictedSize = (int) Math.ceil(predictedSize * Math.max(reductionFactor, 0.7));
      logger.info(
          "CPU usage {}% exceeds threshold {}%, reducing pool size by factor {}",
          snapshot.getCpuUsage(), properties.getCpuThreshold(), reductionFactor);
    }

    // DBクエリ遅延が高い場合はプールを拡大
    if (snapshot.getDbQueryLatency() > properties.getQueryLatencyThreshold()) {
      double latencyFactor = snapshot.getDbQueryLatency() / properties.getQueryLatencyThreshold();
      predictedSize = (int) Math.ceil(predictedSize * Math.min(latencyFactor, 1.5));
      logger.info(
          "DB query latency {}ms exceeds threshold {}ms, expanding pool by factor {}",
          snapshot.getDbQueryLatency(),
          properties.getQueryLatencyThreshold(),
          latencyFactor);
    }

    // 上限を超えないように制限
    predictedSize = Math.min(predictedSize, properties.getMaxPoolSizeLimit());

    // 最小値は1以上
    return Math.max(1, predictedSize);
  }

  /**
   * 最適なminimum-idleを計算します。
   *
   * <p>計算ロジック:
   *
   * <ul>
   *   <li>直近の平均アイドル接続数を基準にする
   *   <li>maximum-pool-sizeの30%を目安にする
   *   <li>両方の値の平均を取って決定
   *   <li>設定された下限(minIdleLimit)未満にならないように制限
   * </ul>
   *
   * @param maxPoolSize 計算されたmaximum-pool-size
   * @return 最適なminimum-idle
   */
  private int calculateOptimalMinIdle(int maxPoolSize) {
    // 履歴からの平均アイドル接続数
    long totalIdle = 0;
    int count = 0;
    for (PoolMetricsSnapshot s : metricsHistory) {
      totalIdle += s.getIdleConnections();
      count++;
    }
    int averageIdle = count > 0 ? (int) (totalIdle / count) : 0;

    // maximum-pool-sizeの30%を目安
    int targetIdle = (int) (maxPoolSize * 0.3);

    // 平均アイドル数とターゲットの平均
    int predictedMinIdle = (averageIdle + targetIdle) / 2;

    // 下限を超えないように制限
    predictedMinIdle = Math.max(predictedMinIdle, properties.getMinIdleLimit());

    // maximum-pool-sizeを超えない
    predictedMinIdle = Math.min(predictedMinIdle, maxPoolSize);

    return predictedMinIdle;
  }

  /**
   * idleTimeoutを動的に調整します。
   *
   * @param snapshot 最新のメトリクススナップショット
   */
  private void adjustIdleTimeout(PoolMetricsSnapshot snapshot) {
    long currentIdleTimeout = dataSource.getIdleTimeout();
    long currentMaxLifetime = dataSource.getMaxLifetime();

    // アイドル接続が非常に多い場合はidleTimeoutを短縮
    if (snapshot.getIdleConnections() > snapshot.getTotalConnections() * 0.7
        && snapshot.getTotalConnections() > 0) {
      long newIdleTimeout = Math.max(currentIdleTimeout / 2, 300000); // 最小5分
      if (newIdleTimeout != currentIdleTimeout) {
        dataSource.setIdleTimeout(newIdleTimeout);
        logger.info("🔁 Updated idle-timeout: {}ms -> {}ms", currentIdleTimeout, newIdleTimeout);
      }
    }
    // アイドル接続が非常に少ない場合はidleTimeoutを延長
    else if (snapshot.getIdleConnections() < snapshot.getTotalConnections() * 0.1
        && snapshot.getTotalConnections() > 0) {
      long newIdleTimeout = Math.min(currentIdleTimeout * 2, 1800000); // 最大30分
      if (newIdleTimeout != currentIdleTimeout) {
        dataSource.setIdleTimeout(newIdleTimeout);
        logger.info("🔁 Updated idle-timeout: {}ms -> {}ms", currentIdleTimeout, newIdleTimeout);
      }
    }
  }

  /**
   * maxLifetimeを動的に調整します。
   *
   * @param snapshot 最新のメトリクススナップショット
   */
  private void adjustMaxLifetime(PoolMetricsSnapshot snapshot) {
    long currentMaxLifetime = dataSource.getMaxLifetime();

    // 接続タイムアウトが多い場合はmaxLifetimeを短縮して接続の新鮮さを保つ
    if (snapshot.getConnectionTimeouts() > 0) {
      long newMaxLifetime = Math.max(currentMaxLifetime / 2, 600000); // 最小10分
      if (newMaxLifetime != currentMaxLifetime) {
        dataSource.setMaxLifetime(newMaxLifetime);
        logger.info("🔁 Updated max-lifetime: {}ms -> {}ms", currentMaxLifetime, newMaxLifetime);
      }
    }
  }

  /**
   * MicrometerからCPU使用率を収集します。
   *
   * @return CPU使用率（%）
   */
  private double collectCpuUsage() {
    return meterRegistry
        .map(
            registry -> {
              io.micrometer.core.instrument.Gauge gauge = registry.find("system.cpu.usage").gauge();
              return gauge != null ? gauge.value() * 100.0 : 0.0;
            })
        .orElse(0.0);
  }

  /**
   * MicrometerからDBクエリ遅延を収集します。
   *
   * @return 平均クエリ遅延（ミリ秒）
   */
  private double collectDbQueryLatency() {
    return meterRegistry
        .map(
            registry -> {
              Timer timer = registry.find("hikaricp.connections.acquire").timer();
              return timer != null ? timer.mean(TimeUnit.MILLISECONDS) : 0.0;
            })
        .orElse(0.0);
  }

  /**
   * 現在の総接続数を取得します。
   *
   * @return 総接続数
   */
  private long getTotalConnections() {
    return dataSource.getHikariPoolMXBean().getTotalConnections();
  }

  /**
   * 現在のアイドル接続数を取得します。
   *
   * @return アイドル接続数
   */
  private long getIdleConnections() {
    return dataSource.getHikariPoolMXBean().getIdleConnections();
  }

  /**
   * 現在のアクティブ接続数を取得します。
   *
   * @return アクティブ接続数
   */
  private long getActiveConnections() {
    return dataSource.getHikariPoolMXBean().getActiveConnections();
  }

  /**
   * 現在の待機スレッド数を取得します。
   *
   * @return 待機スレッド数
   */
  private long getPendingThreads() {
    return dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();
  }

  /**
   * 接続タイムアウトの累積回数を取得します。
   *
   * @return タイムアウト回数
   */
  private long getConnectionTimeouts() {
    return dataSource.getHikariPoolMXBean().getTotalConnections() > 0
        ? dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
        : 0;
  }

  /** 接続プールのメトリクススナップショットを保持するレコードです。 */
  private static class PoolMetricsSnapshot {
    private final long timestamp;
    private final long totalConnections;
    private final long idleConnections;
    private final long activeConnections;
    private final long pendingThreads;
    private final long connectionTimeouts;
    private final double cpuUsage;
    private final double dbQueryLatency;

    PoolMetricsSnapshot(
        long timestamp,
        long totalConnections,
        long idleConnections,
        long activeConnections,
        long pendingThreads,
        long connectionTimeouts,
        double cpuUsage,
        double dbQueryLatency) {
      this.timestamp = timestamp;
      this.totalConnections = totalConnections;
      this.idleConnections = idleConnections;
      this.activeConnections = activeConnections;
      this.pendingThreads = pendingThreads;
      this.connectionTimeouts = connectionTimeouts;
      this.cpuUsage = cpuUsage;
      this.dbQueryLatency = dbQueryLatency;
    }

    long getTimestamp() {
      return timestamp;
    }

    long getTotalConnections() {
      return totalConnections;
    }

    long getIdleConnections() {
      return idleConnections;
    }

    long getActiveConnections() {
      return activeConnections;
    }

    long getPendingThreads() {
      return pendingThreads;
    }

    long getConnectionTimeouts() {
      return connectionTimeouts;
    }

    double getCpuUsage() {
      return cpuUsage;
    }

    double getDbQueryLatency() {
      return dbQueryLatency;
    }
  }
}
