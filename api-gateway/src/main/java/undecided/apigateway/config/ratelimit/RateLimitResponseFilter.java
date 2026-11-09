package undecided.apigateway.config.ratelimit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import org.jspecify.annotations.NonNull;
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

  /**
   * JsonProcessingException発生時のフォールバックJSONを動的に生成します。
   *
   * <p>設計書 7.2節で定義されたレスポンス形式の簡略版です。
   * retryAfter、timestamp、pathは実行時に動的に設定されます。
   * 実際にはObjectMapperによるシリアライズが成功するため、
   * このフォールバックに到達することは極めて稀です。
   *
   * <p>このフォールバックは {@link #objectMapper} のシリアライズが失敗した場合にのみ使用されます。
   * {@link RateLimitExceededError} の構造が変更された場合、このメソッドも更新してください。
   *
   * @param retryAfter リトライ可能時間（秒）
   * @param path リクエストパス
   * @return フォールバックJSON文字列
   */
  private static String fallbackJson(int retryAfter, String path) {
    String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    return "{\"error\":{\"status\":429,\"code\":\"RATE_LIMIT_EXCEEDED\",\"message\":\"リクエストの送信頻度が高すぎます\",\"retryAfter\":"
        + retryAfter + ",\"timestamp\":\"" + timestamp + "\",\"path\":\"" + path + "\"}}";
  }

  private final @NonNull ObjectMapper objectMapper;
  private final int defaultRetryAfterSeconds;

  /**
   * @param objectMapper JSONシリアライズ用のObjectMapper
   * @param defaultRetryAfterSeconds デフォルトのリトライ可能時間（秒）。
   *     Bucket4jフィルタがRetry-Afterヘッダーを設定しないため、
   *     レートリミットの期間（例: 60秒）を指定してください。
   */
  public RateLimitResponseFilter(@NonNull ObjectMapper objectMapper, int defaultRetryAfterSeconds) {
    this.objectMapper = objectMapper;
    this.defaultRetryAfterSeconds = defaultRetryAfterSeconds;
  }

  @Override
  public @NonNull ServerResponse filter(
      @NonNull ServerRequest request, @NonNull HandlerFunction<ServerResponse> next) throws Exception {
    ServerResponse response = next.handle(request);

    // 429レスポンスの場合、カスタムJSON形式で置き換える
    if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS) {
      String path = request.path();
      int retryAfter = calculateRetryAfter();

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
            .body(fallbackJson(retryAfter, path));
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
   * <p>Bucket4jの残トークン数から正確な残り時間を計算することはできません。
   * WebMVCフィルタではバケット状態へのアクセスが提供されないため、
   * 一律でレートリミットの期間をリトライ可能時間として返します。
   *
   * @return リトライ可能時間（秒）
   */
  private int calculateRetryAfter() {
    // Bucket4j WebMVCフィルタはRetry-Afterヘッダーを設定しないため、
    // レートリミットの期間に基づいたデフォルト値を返す
    return defaultRetryAfterSeconds;
  }
}
