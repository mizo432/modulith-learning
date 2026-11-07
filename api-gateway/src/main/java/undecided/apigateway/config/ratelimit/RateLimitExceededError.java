package undecided.apigateway.config.ratelimit;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import org.jspecify.annotations.NonNull;

/**
 * レートリミット超過時のエラーレスポンス
 *
 * <p>詳細設計書 7.2節に基づくレスポンス形式を提供します。
 *
 * <p>イミュータブルなデータモデルのため、Java record を使用します。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RateLimitExceededError(@NonNull ErrorDetail error) {

  public RateLimitExceededError(
      int status, @NonNull String code, @NonNull String message, int retryAfter, @NonNull String path) {
    this(new ErrorDetail(status, code, message, retryAfter, Instant.now(), path));
  }

  /**
   * エラー詳細情報
   *
   * <p>イミュータブルなデータモデルのため、Java record を使用します。
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record ErrorDetail(
      int status,
      @NonNull String code,
      @NonNull String message,
      int retryAfter,
      @NonNull Instant timestamp,
      @NonNull String path) {
  }
}
