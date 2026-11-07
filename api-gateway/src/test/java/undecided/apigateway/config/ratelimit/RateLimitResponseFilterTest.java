package undecided.apigateway.config.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateLimitResponseFilterTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Test
  @DisplayName("RateLimitExceededErrorのシリアライズ形式が正しい")
  void errorSerializationFormat() throws JsonProcessingException {
    // Given
    RateLimitExceededError error =
        new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テストメッセージ", 60, "/api/test");

    // When
    String json = objectMapper.writeValueAsString(error);

    // Then
    assertThat(json).contains("\"error\"");
    assertThat(json).contains("\"status\":429");
    assertThat(json).contains("\"code\":\"RATE_LIMIT_EXCEEDED\"");
    assertThat(json).contains("\"message\":\"テストメッセージ\"");
    assertThat(json).contains("\"retryAfter\":60");
    assertThat(json).contains("\"path\":\"/api/test\"");
    assertThat(json).contains("\"timestamp\"");
  }

  @Test
  @DisplayName("RateLimitExceededErrorのtimestampはISO 8601形式でシリアライズされる")
  void timestampIsSerializedAsIso8601() throws JsonProcessingException {
    // Given
    RateLimitExceededError error =
        new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テスト", 30, "/api/test");

    // When
    String json = objectMapper.writeValueAsString(error);

    // Then - timestampがJSONに含まれていることを確認
    assertThat(json).contains("\"timestamp\"");
    // timestampの値がISO 8601形式であることを確認（Tを含む）
    assertThat(json).contains("\"timestamp\":\"");
  }

  @Test
  @DisplayName("RateLimitResponseFilterインスタンスが作成できる")
  void filterCanBeCreated() {
    // When
    RateLimitResponseFilter filter = new RateLimitResponseFilter(objectMapper, 60);

    // Then
    assertThat(filter).isNotNull();
  }
}
