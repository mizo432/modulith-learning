package undecided.supporting.presentation.web.idempotency;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.concurrent.ConcurrentMap;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * 冪等性キーの管理とレスポンスキャッシュを行うコンポーネントです。
 *
 * <p>POSTおよびPATCHメソッドで送信されたリクエストの冪等性キーを管理し、 重複リクエストを検出してキャッシュされたレスポンスを返却します。
 */
@Component
public class IdempotencyKeyStore {

  private static final Duration DEFAULT_TTL = Duration.ofHours(1);

  private final Cache<String, IdempotencyResult> cache;

  public IdempotencyKeyStore() {
    this.cache = Caffeine.newBuilder().expireAfterWrite(DEFAULT_TTL).maximumSize(10_000).build();
  }

  /**
   * 冪等性キーが既に存在するかチェックし、存在する場合はキャッシュされた結果を返します。
   *
   * @param key 冪等性キー
   * @return キャッシュされた結果（存在する場合）、否则null
   */
  @Nullable
  public IdempotencyResult getIfPresent(@NonNull String key) {
    return cache.getIfPresent(key);
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
    cache.put(key, result);
    return result;
  }

  /**
   * 指定された冪等性キーが存在しない場合、trueを返します。
   *
   * @param key 冪等性キー
   * @return キーが存在しない場合はtrue、存在する場合はfalse
   */
  public boolean isKeyNew(@NonNull String key) {
    return cache.getIfPresent(key) == null;
  }

  /**
   * キャッシュ内の全キーを取得します（デバッグ用）。
   *
   * @return キャッシュ内の全キーと結果のマップ
   */
  public ConcurrentMap<String, IdempotencyResult> asMap() {
    return cache.asMap();
  }

  /**
   * 冪等性処理の結果を保持するレコードです。
   *
   * @param status HTTPステータスコード
   * @param body レスポンスボディ
   */
  public record IdempotencyResult(int status, @Nullable Object body) {}
}
