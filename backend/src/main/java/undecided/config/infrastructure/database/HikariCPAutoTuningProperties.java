package undecided.config.infrastructure.database;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HikariCP接続プールの実行時オートチューニング設定プロパティクラスです。
 *
 * <p>このクラスは、HikariCPの接続プール設定をアプリケーション実行中に動的に調整するための 設定値を保持します。application.ymlの hikari.auto-tuning
 * プレフィックスにバインドされます。
 *
 * <p>Prometheusを使用せず、MicrometerとHikariCP MBeanメトリクスのみで 自律的な接続プール最適化を実現します。
 */
@ConfigurationProperties(prefix = "hikari.auto-tuning")
public class HikariCPAutoTuningProperties {

  /** オートチューニング機能を有効にするかどうか。デフォルトはfalse。 */
  private boolean enabled = false;

  /** チューニング実行間隔（秒）。デフォルトは60秒（1分）。 */
  private long intervalSeconds = 60;

  /** 接続プールの最大サイズ上限。デフォルトは50。 */
  private int maxPoolSizeLimit = 50;

  /** 接続プールの最小アイドル数下限。デフォルトは1。 */
  private int minIdleLimit = 1;

  /** 1回の調整で変更できる最大ステップ数。デフォルトは3。 ノイズによる過剰な調整を防ぎます。 */
  private int maxAdjustmentStep = 3;

  /** 接続タイムアウト検知時の安全マージン（接続数）。デフォルトは5。 タイムアウトが発生した際にプールサイズに追加する接続数です。 */
  private int timeoutSafetyMargin = 5;

  /** 履歴ウィンドウサイズ（サンプル数）。デフォルトは10。 より長い履歴を保持することで、トラフィックパターンをより正確に予測できます。 */
  private int historyWindowSize = 10;

  /** 指数移動平均(EWA)の平滑化係数。デフォルトは0.3。 値が大きいほど最近のデータに重きを置きます。 */
  private double ewaAlpha = 0.3;

  /** CPU使用率の閾値（%）。デフォルトは80.0。 この値を超えると接続数を抑制してDB負荷を軽減します。 */
  private double cpuThreshold = 80.0;

  /** DBクエリ遅延の閾値（ミリ秒）。デフォルトは1000.0。 この値を超えると接続プールの拡大を検討します。 */
  private double queryLatencyThreshold = 1000.0;

  /** idleTimeoutの動的調整を有効にするかどうか。デフォルトはfalse。 */
  private boolean adjustIdleTimeout = false;

  /** maxLifetimeの動的調整を有効にするかどうか。デフォルトはfalse。 */
  private boolean adjustMaxLifetime = false;

  /** 接続タイムアウトが連続して発生した回数でフォールバックをトリガーする閾値。デフォルトは3。 */
  private int timeoutFallbackThreshold = 3;

  /** フォールバック時のデフォルトプールサイズ。デフォルトは30。 */
  private int fallbackPoolSize = 30;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public long getIntervalSeconds() {
    return intervalSeconds;
  }

  public void setIntervalSeconds(long intervalSeconds) {
    this.intervalSeconds = intervalSeconds;
  }

  public int getMaxPoolSizeLimit() {
    return maxPoolSizeLimit;
  }

  public void setMaxPoolSizeLimit(int maxPoolSizeLimit) {
    this.maxPoolSizeLimit = maxPoolSizeLimit;
  }

  public int getMinIdleLimit() {
    return minIdleLimit;
  }

  public void setMinIdleLimit(int minIdleLimit) {
    this.minIdleLimit = minIdleLimit;
  }

  public int getMaxAdjustmentStep() {
    return maxAdjustmentStep;
  }

  public void setMaxAdjustmentStep(int maxAdjustmentStep) {
    this.maxAdjustmentStep = maxAdjustmentStep;
  }

  public int getTimeoutSafetyMargin() {
    return timeoutSafetyMargin;
  }

  public void setTimeoutSafetyMargin(int timeoutSafetyMargin) {
    this.timeoutSafetyMargin = timeoutSafetyMargin;
  }

  public int getHistoryWindowSize() {
    return historyWindowSize;
  }

  public void setHistoryWindowSize(int historyWindowSize) {
    this.historyWindowSize = historyWindowSize;
  }

  public double getEwaAlpha() {
    return ewaAlpha;
  }

  public void setEwaAlpha(double ewaAlpha) {
    this.ewaAlpha = ewaAlpha;
  }

  public double getCpuThreshold() {
    return cpuThreshold;
  }

  public void setCpuThreshold(double cpuThreshold) {
    this.cpuThreshold = cpuThreshold;
  }

  public double getQueryLatencyThreshold() {
    return queryLatencyThreshold;
  }

  public void setQueryLatencyThreshold(double queryLatencyThreshold) {
    this.queryLatencyThreshold = queryLatencyThreshold;
  }

  public boolean isAdjustIdleTimeout() {
    return adjustIdleTimeout;
  }

  public void setAdjustIdleTimeout(boolean adjustIdleTimeout) {
    this.adjustIdleTimeout = adjustIdleTimeout;
  }

  public boolean isAdjustMaxLifetime() {
    return adjustMaxLifetime;
  }

  public void setAdjustMaxLifetime(boolean adjustMaxLifetime) {
    this.adjustMaxLifetime = adjustMaxLifetime;
  }

  public int getTimeoutFallbackThreshold() {
    return timeoutFallbackThreshold;
  }

  public void setTimeoutFallbackThreshold(int timeoutFallbackThreshold) {
    this.timeoutFallbackThreshold = timeoutFallbackThreshold;
  }

  public int getFallbackPoolSize() {
    return fallbackPoolSize;
  }

  public void setFallbackPoolSize(int fallbackPoolSize) {
    this.fallbackPoolSize = fallbackPoolSize;
  }
}
