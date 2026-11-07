package undecided.apigateway.config.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateLimitExceededErrorTest {

  @Test
  @DisplayName("RateLimitExceededErrorのコンストラクタが正しく動作する")
  void constructor() {
    RateLimitExceededError error =
        new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テストメッセージ", 30, "/api/test");

    assertThat(error.getError()).isNotNull();
    assertThat(error.getError().getStatus()).isEqualTo(429);
    assertThat(error.getError().getCode()).isEqualTo("RATE_LIMIT_EXCEEDED");
    assertThat(error.getError().getMessage()).isEqualTo("テストメッセージ");
    assertThat(error.getError().getRetryAfter()).isEqualTo(30);
    assertThat(error.getError().getPath()).isEqualTo("/api/test");
    assertThat(error.getError().getTimestamp()).isNotNull();
  }

  @Test
  @DisplayName("ErrorDetailのゲッターが正しく動作する")
  void errorDetailGetters() {
    RateLimitExceededError error =
        new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "メッセージ", 60, "/api/v1/sprints");

    RateLimitExceededError.ErrorDetail detail = error.getError();

    assertThat(detail.getStatus()).isEqualTo(429);
    assertThat(detail.getCode()).isEqualTo("RATE_LIMIT_EXCEEDED");
    assertThat(detail.getMessage()).isEqualTo("メッセージ");
    assertThat(detail.getRetryAfter()).isEqualTo(60);
    assertThat(detail.getPath()).isEqualTo("/api/v1/sprints");
    assertThat(detail.getTimestamp()).isNotNull();
  }

  @Test
  @DisplayName("timestampは現在時刻が設定される")
  void timestampIsSetToCurrentTime() {
    long before = System.currentTimeMillis();
    RateLimitExceededError error =
        new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テスト", 30, "/api/test");
    long after = System.currentTimeMillis();

    long timestampMillis = error.getError().getTimestamp().toEpochMilli();
    assertThat(timestampMillis).isGreaterThanOrEqualTo(before);
    assertThat(timestampMillis).isLessThanOrEqualTo(after);
  }
}
