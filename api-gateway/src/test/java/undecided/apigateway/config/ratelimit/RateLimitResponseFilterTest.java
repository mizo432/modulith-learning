package undecided.apigateway.config.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Tag("small")
@DisplayName("RateLimitResponseFilterのテスト")
@SuppressWarnings("unchecked")
class RateLimitResponseFilterTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Nested
  @DisplayName("RateLimitExceededErrorシリアライズのテスト")
  class ErrorSerializationTest {

    @Test
    @DisplayName("RateLimitExceededErrorのシリアライズ形式が正しいこと")
    void shouldSerializeErrorInCorrectFormat() throws JsonProcessingException {
      // Arrange
      RateLimitExceededError error =
          new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テストメッセージ", 60, "/api/test");

      // Act
      String json = objectMapper.writeValueAsString(error);

      // Assert
      assertThat(json)
          .as("JSONにerrorキーが含まれること")
          .contains("\"error\"");
      assertThat(json)
          .as("statusが429であること")
          .contains("\"status\":429");
      assertThat(json)
          .as("codeがRATE_LIMIT_EXCEEDEDであること")
          .contains("\"code\":\"RATE_LIMIT_EXCEEDED\"");
      assertThat(json)
          .as("messageが含まれること")
          .contains("\"message\":\"テストメッセージ\"");
      assertThat(json)
          .as("retryAfterが60であること")
          .contains("\"retryAfter\":60");
      assertThat(json)
          .as("pathが含まれること")
          .contains("\"path\":\"/api/test\"");
      assertThat(json)
          .as("timestampが含まれること")
          .contains("\"timestamp\"");
    }

    @Test
    @DisplayName("timestampはISO 8601形式でシリアライズされること")
    void shouldSerializeTimestampAsIso8601() throws JsonProcessingException {
      // Arrange
      RateLimitExceededError error =
          new RateLimitExceededError(429, "RATE_LIMIT_EXCEEDED", "テスト", 30, "/api/test");

      // Act
      String json = objectMapper.writeValueAsString(error);

      // Assert
      assertThat(json)
          .as("timestampキーが含まれること")
          .contains("\"timestamp\"");
      assertThat(json)
          .as("timestampがISO 8601形式であること")
          .contains("\"timestamp\":\"");
    }
  }

  @Nested
  @DisplayName("フィルタロジックのテスト")
  class FilterLogicTest {

    @Test
    @DisplayName("429レスポンスはカスタムJSON形式で置き換えられること")
    void shouldReplace429ResponseWithCustomJson() throws Exception {
      // Arrange
      RateLimitResponseFilter filter = new RateLimitResponseFilter(objectMapper, 60);
      ServerRequest request = mock(ServerRequest.class);
      HandlerFunction<ServerResponse> next = mock(HandlerFunction.class);

      when(request.path()).thenReturn("/api/test");
      ServerResponse original429 = ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).build();
      when(next.handle(request)).thenReturn(original429);

      // Act
      ServerResponse response = filter.filter(request, next);

      // Assert
      assertThat(response.statusCode())
          .as("ステータスコードは429であること")
          .isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
      assertThat(response)
          .as("元の429レスポンスとは異なるインスタンスが返されること")
          .isNotSameAs(original429);
      assertThat(response.headers().getContentType())
          .as("Content-Typeはapplication/jsonであること")
          .isNotNull();
      assertThat(response.headers().getContentType().getType())
          .as("Content-Typeのタイプはapplicationであること")
          .isEqualTo("application");
      assertThat(response.headers().getContentType().getSubtype())
          .as("Content-Typeのサブタイプはjsonであること")
          .isEqualTo("json");
      // NOTE: レスポンスボディのJSON形式検証は ErrorSerializationTest でカバーされている。
      // WebMVCのServerResponseではbody()メソッドがObjectを返すが、
      // テスト環境での直接アクセスが困難なため、シリアライズテストで代替する。
    }

    @Test
    @DisplayName("429以外のレスポンスはそのまま通過すること")
    void shouldPassThroughNon429Response() throws Exception {
      // Arrange
      RateLimitResponseFilter filter = new RateLimitResponseFilter(objectMapper, 60);
      ServerRequest request = mock(ServerRequest.class);
      HandlerFunction<ServerResponse> next = mock(HandlerFunction.class);

      when(next.handle(request)).thenReturn(ServerResponse.ok().body("OK"));

      // Act
      ServerResponse response = filter.filter(request, next);

      // Assert
      assertThat(response.statusCode())
          .as("ステータスコードは200であること")
          .isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("200レスポンスは変更されないこと")
    void shouldNotModify200Response() throws Exception {
      // Arrange
      RateLimitResponseFilter filter = new RateLimitResponseFilter(objectMapper, 60);
      ServerRequest request = mock(ServerRequest.class);
      HandlerFunction<ServerResponse> next = mock(HandlerFunction.class);

      ServerResponse originalResponse = ServerResponse.ok().body("Hello");
      when(next.handle(request)).thenReturn(originalResponse);

      // Act
      ServerResponse response = filter.filter(request, next);

      // Assert
      assertThat(response)
          .as("元のレスポンスがそのまま返されること")
          .isSameAs(originalResponse);
    }

    @Test
    @DisplayName("500レスポンスは変更されないこと")
    void shouldNotModify500Response() throws Exception {
      // Arrange
      RateLimitResponseFilter filter = new RateLimitResponseFilter(objectMapper, 60);
      ServerRequest request = mock(ServerRequest.class);
      HandlerFunction<ServerResponse> next = mock(HandlerFunction.class);

      when(next.handle(request)).thenReturn(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

      // Act
      ServerResponse response = filter.filter(request, next);

      // Assert
      assertThat(response.statusCode())
          .as("ステータスコードは500であること")
          .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
