package undecided.supporting.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

/** テストリスト項目1〜3: 外部通信ロギングインターセプタのテスト（#755） */
@Tag("small")
@DisplayName("LoggingClientHttpRequestInterceptorの単体テスト")
class LoggingClientHttpRequestInterceptorTest {

  private final LoggingClientHttpRequestInterceptor interceptor =
      new LoggingClientHttpRequestInterceptor();

  private Logger logger;
  private ListAppender<ILoggingEvent> appender;

  @BeforeEach
  void setUp() {
    logger = (Logger) LoggerFactory.getLogger(LoggingClientHttpRequestInterceptor.class);
    appender = new ListAppender<>();
    appender.start();
    logger.addAppender(appender);
    logger.setLevel(Level.INFO);
  }

  @AfterEach
  void tearDown() {
    logger.detachAppender(appender);
    logger.setLevel(null);
  }

  @Test
  @DisplayName("リクエストを送ったとき、メソッド・URI・ステータス・所要時間を含むINFOログが記録される")
  void shouldLogRequestAndResponse() throws IOException {
    MockClientHttpRequest request =
        new MockClientHttpRequest(
            HttpMethod.GET, URI.create("https://example.com/master/v1/latest"));

    interceptor.intercept(
        request, new byte[0], (req, body) -> new MockClientHttpResponse(new byte[0], HttpStatus.OK));

    assertThat(appender.list)
        .anySatisfy(
            event -> {
              assertThat(event.getLevel()).isEqualTo(Level.INFO);
              assertThat(event.getFormattedMessage())
                  .contains("GET")
                  .contains("https://example.com/master/v1/latest");
            });
    assertThat(appender.list)
        .anySatisfy(
            event -> {
              assertThat(event.getLevel()).isEqualTo(Level.INFO);
              assertThat(event.getFormattedMessage()).contains("200");
              assertThat(event.getFormattedMessage()).matches(".*durationMs=\\d+.*");
            });
  }

  @Test
  @DisplayName("リクエストに機密ヘッダーが含まれるとき、その値がログに記録されない")
  void shouldNotLogSecretHeaderValues() throws IOException {
    MockClientHttpRequest request =
        new MockClientHttpRequest(
            HttpMethod.GET, URI.create("https://example.com/master/v1/latest"));
    request.getHeaders().set("x-api-key", "secret-value-123");

    interceptor.intercept(
        request, new byte[0], (req, body) -> new MockClientHttpResponse(new byte[0], HttpStatus.OK));

    assertThat(appender.list)
        .allSatisfy(
            event -> assertThat(event.getFormattedMessage()).doesNotContain("secret-value-123"));
  }

  @Test
  @DisplayName("通信が例外で失敗したとき、WARNログが記録され、例外はそのまま伝播する")
  void shouldLogWarnAndRethrowOnFailure() {
    MockClientHttpRequest request =
        new MockClientHttpRequest(
            HttpMethod.POST, URI.create("https://example.com/master/v1/download-url"));

    assertThatThrownBy(
            () ->
                interceptor.intercept(
                    request,
                    new byte[0],
                    (req, body) -> {
                      throw new IOException("connection failed");
                    }))
        .isInstanceOf(IOException.class)
        .hasMessage("connection failed");
    assertThat(appender.list)
        .anySatisfy(event -> assertThat(event.getLevel()).isEqualTo(Level.WARN));
  }
}
