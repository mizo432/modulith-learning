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
   * デフォルトのバケット容量（期間内に補充されるトークン数）
   *
   * <p>Bucket4j WebMVCの {@code setCapacity} に渡され、指定された期間内に補充されるトークン数を
   * 表します。例えば、capacity=100, period=60秒の場合、1分あたり100リクエストが許容されます。
   *
   * <p>デフォルト: 100（1分あたり100リクエスト）
   */
  private int capacity = 100;

  /**
   * デフォルトのバースト容量（バケットの最大容量）
   *
   * <p>バケットが保持できるトークンの最大数を表します。capacityより大きく設定することで、
   * 短時間のバーストリクエストを許容できます。
   *
   * <p>NOTE: Spring Cloud Gateway WebMVCのBucket4jフィルタでは、{@code setCapacity}が
   * バースト容量と補充トークン数の両方を兼ねており、独立したバースト容量の設定はサポートされていません。
   * このプロパティは、将来的にWebFlux版のフィルタに移行した場合や、カスタム実装で使用する目的で
   * 残しています。
   *
   * <p>デフォルト: 200
   */
  private int burstCapacity = 200;

  /**
   * デフォルトの期間（秒）
   * デフォルト: 60秒（1分）
   */
  private long periodSeconds = 60;

  /**
   * 認証関連エンドポイントのバケット容量
   * デフォルト: 5（1分あたり5リクエスト）
   */
  private int authCapacity = 5;

  /**
   * 認証関連エンドポイントのバースト容量
   *
   * <p>NOTE: WebMVCのBucket4jフィルタでは独立したバースト容量の設定がサポートされていません。
   *
   * <p>デフォルト: 10
   */
  private int authBurstCapacity = 10;

  /**
   * トークン更新エンドポイントのバケット容量
   * デフォルト: 10（1分あたり10リクエスト）
   */
  private int authRefreshCapacity = 10;

  /**
   * 書き込み操作のバケット容量
   * デフォルト: 30（1分あたり30リクエスト）
   */
  private int writeCapacity = 30;

  /**
   * 書き込み操作のバースト容量
   *
   * <p>NOTE: WebMVCのBucket4jフィルタでは独立したバースト容量の設定がサポートされていません。
   *
   * <p>デフォルト: 60
   */
  private int writeBurstCapacity = 60;

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
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

  public int getAuthCapacity() {
    return authCapacity;
  }

  public void setAuthCapacity(int authCapacity) {
    this.authCapacity = authCapacity;
  }

  public int getAuthBurstCapacity() {
    return authBurstCapacity;
  }

  public void setAuthBurstCapacity(int authBurstCapacity) {
    this.authBurstCapacity = authBurstCapacity;
  }

  public int getAuthRefreshCapacity() {
    return authRefreshCapacity;
  }

  public void setAuthRefreshCapacity(int authRefreshCapacity) {
    this.authRefreshCapacity = authRefreshCapacity;
  }

  public int getWriteCapacity() {
    return writeCapacity;
  }

  public void setWriteCapacity(int writeCapacity) {
    this.writeCapacity = writeCapacity;
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
