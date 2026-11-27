package undecided.supporting.http;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * 外部通信の送受信をログに記録するインターセプタ。
 *
 * <p>ヘッダーとボディは記録しない（APIキー等の秘匿情報の漏えい防止）。
 */
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

  private static final Logger log =
      LoggerFactory.getLogger(LoggingClientHttpRequestInterceptor.class);

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    long startNanos = System.nanoTime();
    log.info("外部通信 送信: {} {}", request.getMethod(), request.getURI());
    try {
      ClientHttpResponse response = execution.execute(request, body);
      log.info(
          "外部通信 受信: {} {} status={} durationMs={}",
          request.getMethod(),
          request.getURI(),
          response.getStatusCode().value(),
          elapsedMillis(startNanos));
      return response;
    } catch (IOException | RuntimeException ex) {
      log.warn(
          "外部通信 失敗: {} {} durationMs={}",
          request.getMethod(),
          request.getURI(),
          elapsedMillis(startNanos),
          ex);
      throw ex;
    }
  }

  private static long elapsedMillis(long startNanos) {
    return (System.nanoTime() - startNanos) / 1_000_000;
  }
}
