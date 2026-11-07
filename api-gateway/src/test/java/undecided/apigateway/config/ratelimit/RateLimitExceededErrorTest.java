package undecided.apigateway.config.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("RateLimitExceededErrorのテスト")
class RateLimitExceededErrorTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("指定した値でエラーオブジェクトが作成されること")
    void shouldCreateErrorWithCorrectValues() {
      // Act
      RateLimitExceededError error =
          new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テストメッセージ", 30, "/api/test");

      // Assert
      assertThat(error.error())
          .as("ErrorDetailはnullでないこと")
          .isNotNull();
      assertThat(error.error().status())
          .as("statusは429であること")
          .isEqualTo(429);
      assertThat(error.error().code())
          .as("codeはRATE_LIMIT_EXCEEDEDであること")
          .isEqualTo("RATE_LIMIT_EXCEEDED");
      assertThat(error.error().message())
          .as("messageはテストメッセージであること")
          .isEqualTo("テストメッセージ");
      assertThat(error.error().retryAfter())
          .as("retryAfterは30であること")
          .isEqualTo(30);
      assertThat(error.error().path())
          .as("pathは/api/testであること")
          .isEqualTo("/api/test");
      assertThat(error.error().timestamp())
          .as("timestampはnullでないこと")
          .isNotNull();
    }
  }

  @Nested
  @DisplayName("timestampのテスト")
  class TimestampTest {

    @Test
    @DisplayName("timestampは現在時刻が設定されること")
    void shouldSetTimestampToCurrentTime() {
      // Arrange
      long before = System.currentTimeMillis();

      // Act
      RateLimitExceededError error =
          new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テスト", 30, "/api/test");

      // Assert
      long after = System.currentTimeMillis();
      long timestampMillis = error.error().timestamp().toEpochMilli();
      assertThat(timestampMillis)
          .as("timestampは作成時の現在時刻の範囲内であること")
          .isGreaterThanOrEqualTo(before);
      assertThat(timestampMillis)
          .as("timestampは作成時の現在時刻の範囲内であること")
          .isLessThanOrEqualTo(after);
    }
  }
}
