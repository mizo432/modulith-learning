package undecided.config.presentation.web.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 冪等性キーの管理とレスポンスキャッシュを行うコンポーネントです。
 *
 * <p>POSTおよびPATCHメソッドで送信されたリクエストの冪等性キーを管理し、 重複リクエストを検出してキャッシュされたレスポンスを返却します。 Redis database 1
 * を使用します。
 */
@Component
public class IdempotencyKeyStore {

  private static final String KEY_PREFIX = "idempotency:";
  private static final Duration DEFAULT_TTL = Duration.ofHours(1);

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  public IdempotencyKeyStore(
      @NonNull StringRedisTemplate redisTemplate, @NonNull ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  /**
   * 冪等性キーが既に存在するかチェックし、存在する場合はキャッシュされた結果を返します。
   *
   * @param key 冪等性キー
   * @return キャッシュされた結果（存在する場合）、否则null
   */
  @Nullable
  public IdempotencyResult getIfPresent(@NonNull String key) {
    String redisKey = KEY_PREFIX + key;
    String value = redisTemplate.opsForValue().get(redisKey);
    if (value == null) {
      return null;
    }
    return deserializeResult(value);
  }

  /**
   * 新しい冪等性キーとレスポンス結果をキャッシュに保存します。
   *
   * @param key 冪等性キー
   * @param status HTTPステータスコード
   * @param body レスポンスボディ（シリアライズ可能なオブジェクト）
   * @return 保存された結果
   */
  @NonNull
  public IdempotencyResult put(@NonNull String key, int status, @Nullable Object body) {
    IdempotencyResult result = new IdempotencyResult(status, body);
    String redisKey = KEY_PREFIX + key;
    String serialized = serializeResult(result);
    redisTemplate.opsForValue().set(redisKey, serialized, DEFAULT_TTL);
    return result;
  }

  /**
   * 指定された冪等性キーが存在しない場合、trueを返します。
   *
   * @param key 冪等性キー
   * @return キーが存在しない場合はtrue、存在する場合はfalse
   */
  public boolean isKeyNew(@NonNull String key) {
    String redisKey = KEY_PREFIX + key;
    return !Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
  }

  /**
   * キャッシュ内の全キーを取得します（デバッグ用）。
   *
   * @return キャッシュ内の全キーと結果のマップ
   */
  public ConcurrentMap<String, IdempotencyResult> asMap() {
    ConcurrentMap<String, IdempotencyResult> result = new ConcurrentHashMap<>();
    for (String key : redisTemplate.keys(KEY_PREFIX + "*")) {
      String value = redisTemplate.opsForValue().get(key);
      if (value != null) {
        String originalKey = key.substring(KEY_PREFIX.length());
        result.put(originalKey, deserializeResult(value));
      }
    }
    return result;
  }

  @NonNull
  private String serializeResult(@NonNull IdempotencyResult result) {
    try {
      Map<String, Object> map = new HashMap<>();
      map.put("status", result.status());
      map.put("body", result.body());
      return objectMapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize IdempotencyResult", e);
    }
  }

  @NonNull
  private IdempotencyResult deserializeResult(@NonNull String json) {
    try {
      Map<String, Object> map =
          objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
      int status = (Integer) map.get("status");
      Object body = map.get("body");
      return new IdempotencyResult(status, body);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to deserialize IdempotencyResult", e);
    }
  }

  /**
   * 冪等性処理の結果を保持するレコードです。
   *
   * @param status HTTPステータスコード
   * @param body レスポンスボディ
   */
  public record IdempotencyResult(int status, @Nullable Object body) {}
}
