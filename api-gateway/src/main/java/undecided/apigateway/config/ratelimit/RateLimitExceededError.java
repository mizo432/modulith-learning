package undecided.apigateway.config.ratelimit;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

/**
 * レートリミット超過時のエラーレスポンス
 *
 * <p>詳細設計書 7.2節に基づくレスポンス形式を提供します。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RateLimitExceededError {

  private final ErrorDetail error;

  public RateLimitExceededError(int status, String code, String message, int retryAfter, String path) {
    this.error = new ErrorDetail(status, code, message, retryAfter, Instant.now(), path);
  }

  public ErrorDetail getError() {
    return error;
  }

  /**
   * エラー詳細情報
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ErrorDetail {

    private final int status;
    private final String code;
    private final String message;
    private final int retryAfter;
    private final Instant timestamp;
    private final String path;

    public ErrorDetail(int status, String code, String message, int retryAfter, Instant timestamp, String path) {
      this.status = status;
      this.code = code;
      this.message = message;
      this.retryAfter = retryAfter;
      this.timestamp = timestamp;
      this.path = path;
    }

    public int getStatus() {
      return status;
    }

    public String getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    public int getRetryAfter() {
      return retryAfter;
    }

    public Instant getTimestamp() {
      return timestamp;
    }

    public String getPath() {
      return path;
    }
  }
}
