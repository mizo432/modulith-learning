package undecided.apigateway.config.ratelimit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * レートリミット超過時の429レスポンスをカスタマイズするフィルタ
 *
 * <p>Spring Cloud Gateway WebMVCのBucket4jフィルタは、レートリミット超過時に
 * デフォルトの429レスポンスを返します。このフィルタはそのレスポンスを検出し、
 * 詳細設計書 7.2節で定義されたJSON形式で置き換えます。
 *
 * <p>使用方法:
 * <pre>{@code
 * .filter(rateLimit(config -> config
 *     .setCapacity(100)
 *     .setPeriod(Duration.ofMinutes(1))
 *     .setKeyResolver(keyResolver)))
 * .filter(new RateLimitResponseFilter(objectMapper))
 * }</pre>
 */
public class RateLimitResponseFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

  private static final Logger log = LoggerFactory.getLogger(RateLimitResponseFilter.class);
  private static final String RATE_LIMIT_CODE = "RATE_LIMIT_EXCEEDED";
  private static final String RATE_LIMIT_MESSAGE = "リクエストの送信頻度が高すぎます";

  private final ObjectMapper objectMapper;
  private final int defaultRetryAfterSeconds;

  /**
   * @param objectMapper JSONシリアライズ用のObjectMapper
   * @param defaultRetryAfterSeconds デフォルトのリトライ可能時間（秒）。
   *     Bucket4jフィルタがRetry-Afterヘッダーを設定しないため、
   *     レートリミットの期間（例: 60秒）を指定してください。
   */
  public RateLimitResponseFilter(ObjectMapper objectMapper, int defaultRetryAfterSeconds) {
    this.objectMapper = objectMapper;
    this.defaultRetryAfterSeconds = defaultRetryAfterSeconds;
  }

  @Override
  public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
    ServerResponse response = next.handle(request);

    // 429レスポンスの場合、カスタムJSON形式で置き換える
    if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS) {
      String path = request.path();
      int retryAfter = calculateRetryAfter(response);

      log.warn("Rate limit exceeded for path: {}", path);

      RateLimitExceededError error = new RateLimitExceededError(
          HttpStatus.TOO_MANY_REQUESTS.value(),
          RATE_LIMIT_CODE,
          RATE_LIMIT_MESSAGE,
          retryAfter,
          path);

      try {
        String jsonBody = objectMapper.writeValueAsString(error);
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
            .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
            .body(jsonBody);
      } catch (JsonProcessingException e) {
        log.error("Failed to serialize rate limit error response", e);
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
            .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
            .body("{\"error\":{\"status\":429,\"code\":\"RATE_LIMIT_EXCEEDED\",\"message\":\"リクエストの送信頻度が高すぎます\"}}");
      }
    }

    return response;
  }

  /**
   * リトライ可能時間を計算します。
   *
   * <p>Spring Cloud Gateway WebMVCのBucket4jフィルタはデフォルトでRetry-Afterヘッダーを
   * 設定しないため、コンストラクタで指定されたデフォルト値を返します。
   * デフォルト値はレートリミットの期間（例: 60秒）を指定してください。
   *
   * @param response サーバーレスポンス（未使用だが、APIの互換性のために保持）
   * @return リトライ可能時間（秒）
   */
  private int calculateRetryAfter(ServerResponse response) {
    // Bucket4j WebMVCフィルタはRetry-Afterヘッダーを設定しないため、
    // レートリミットの期間に基づいたデフォルト値を返す
    return defaultRetryAfterSeconds;
  }
}
