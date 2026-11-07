package undecided.apigateway.config.ratelimit;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * レートリミッティングの設定プロパティ
 *
 * <p>詳細設計書に基づく制限設定を提供します。
 *
 * <p>制限設定:
 * <ul>
 *   <li>ログインエンドポイント: 1分あたり5回</li>
 *   <li>トークン更新: 1分あたり10回</li>
 *   <li>GETリクエスト: 1分あたり100回</li>
 *   <li>POST/PUT/PATCH/DELETEリクエスト: 1分あたり30回</li>
 * </ul>
 */
@Configuration
@ConfigurationProperties(prefix = "ratelimit")
public class RateLimitProperties {

  /**
   * デフォルトの replenish レート（1秒あたりのトークン補充数）
   * デフォルト: 100（1分あたり100リクエスト）
   */
  private int replenishRate = 100;

  /**
   * デフォルトのバースト容量（一度に許容される最大リクエスト数）
   * デフォルト: 200
   */
  private int burstCapacity = 200;

  /**
   * デフォルトの期間（秒）
   * デフォルト: 60秒（1分）
   */
  private long periodSeconds = 60;

  /**
   * 認証関連エンドポイントの replenish レート
   * デフォルト: 5（1分あたり5リクエスト）
   */
  private int authReplenishRate = 5;

  /**
   * 認証関連エンドポイントのバースト容量
   * デフォルト: 10
   */
  private int authBurstCapacity = 10;

  /**
   * 書き込み操作の replenish レート
   * デフォルト: 30（1分あたり30リクエスト）
   */
  private int writeReplenishRate = 30;

  /**
   * 書き込み操作のバースト容量
   * デフォルト: 60
   */
  private int writeBurstCapacity = 60;

  public int getReplenishRate() {
    return replenishRate;
  }

  public void setReplenishRate(int replenishRate) {
    this.replenishRate = replenishRate;
  }

  public int getBurstCapacity() {
    return burstCapacity;
  }

  public void setBurstCapacity(int burstCapacity) {
    this.burstCapacity = burstCapacity;
  }

  public long getPeriodSeconds() {
    return periodSeconds;
  }

  public void setPeriodSeconds(long periodSeconds) {
    this.periodSeconds = periodSeconds;
  }

  public int getAuthReplenishRate() {
    return authReplenishRate;
  }

  public void setAuthReplenishRate(int authReplenishRate) {
    this.authReplenishRate = authReplenishRate;
  }

  public int getAuthBurstCapacity() {
    return authBurstCapacity;
  }

  public void setAuthBurstCapacity(int authBurstCapacity) {
    this.authBurstCapacity = authBurstCapacity;
  }

  public int getWriteReplenishRate() {
    return writeReplenishRate;
  }

  public void setWriteReplenishRate(int writeReplenishRate) {
    this.writeReplenishRate = writeReplenishRate;
  }

  public void setWriteBurstCapacity(int writeBurstCapacity) {
    this.writeBurstCapacity = writeBurstCapacity;
  }

  public int getWriteBurstCapacity() {
    return writeBurstCapacity;
  }

  public Duration getPeriod() {
    return Duration.ofSeconds(periodSeconds);
  }
}
