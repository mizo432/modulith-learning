package undecided.supporting.presentation.web.idempotency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.config.presentation.web.idempotency.IdempotencyKeyStore;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("IdempotencyKeyStoreのテスト")
class IdempotencyKeyStoreTest {

  @Autowired private IdempotencyKeyStore store;

  @Autowired private StringRedisTemplate redisTemplate;

  @BeforeEach
  void setUp() {
    // Clear all idempotency keys before each test
    java.util.Set<String> keys = redisTemplate.keys("idempotency:*");
    if (keys != null && !keys.isEmpty()) {
      redisTemplate.delete(keys);
    }
  }

  @Nested
  @DisplayName("getIfPresentメソッドのテスト")
  class GetIfPresentTest {

    @Test
    @DisplayName("キーが存在しない場合、nullを返すこと")
    void shouldReturnNullWhenKeyDoesNotExist() {
      // Act
      IdempotencyKeyStore.IdempotencyResult result = store.getIfPresent("nonexistent-key");

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("キーが存在する場合、キャッシュされた結果を返すこと")
    void shouldReturnCachedResultWhenKeyExists() {
      // Arrange
      store.put("test-key", 201, "created-body");

      // Act
      IdempotencyKeyStore.IdempotencyResult result = store.getIfPresent("test-key");

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.status()).isEqualTo(201);
      assertThat(result.body()).isEqualTo("created-body");
    }
  }

  @Nested
  @DisplayName("putメソッドのテスト")
  class PutTest {

    @Test
    @DisplayName("キーと結果を正常に保存すること")
    void shouldStoreKeyAndResult() {
      // Act
      IdempotencyKeyStore.IdempotencyResult result = store.put("new-key", 200, "body-data");

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.status()).isEqualTo(200);
      assertThat(result.body()).isEqualTo("body-data");

      // Verify it's cached
      IdempotencyKeyStore.IdempotencyResult cached = store.getIfPresent("new-key");
      assertThat(cached).isNotNull();
      assertThat(cached.status()).isEqualTo(200);
    }

    @Test
    @DisplayName("ボディがnullの場合でも正常に保存すること")
    void shouldStoreWithNullBody() {
      // Act
      IdempotencyKeyStore.IdempotencyResult result = store.put("null-body-key", 204, null);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.status()).isEqualTo(204);
      assertThat(result.body()).isNull();
    }
  }

  @Nested
  @DisplayName("isKeyNewメソッドのテスト")
  class IsKeyNewTest {

    @Test
    @DisplayName("新しいキーの場合、trueを返すこと")
    void shouldReturnTrueForNewKey() {
      // Act
      boolean isNew = store.isKeyNew("brand-new-key");

      // Assert
      assertThat(isNew).isTrue();
    }

    @Test
    @DisplayName("既存のキーの場合、falseを返すこと")
    void shouldReturnFalseForExistingKey() {
      // Arrange
      store.put("existing-key", 200, "body");

      // Act
      boolean isNew = store.isKeyNew("existing-key");

      // Assert
      assertThat(isNew).isFalse();
    }
  }

  @Nested
  @DisplayName("asMapメソッドのテスト")
  class AsMapTest {

    @Test
    @DisplayName("キャッシュされた全キーと結果を返すこと")
    void shouldReturnAllCachedEntries() {
      // Arrange
      store.put("key1", 200, "body1");
      store.put("key2", 201, "body2");

      // Act
      ConcurrentMap<String, IdempotencyKeyStore.IdempotencyResult> map = store.asMap();

      // Assert
      assertThat(map).hasSize(2);
      assertThat(map.containsKey("key1")).isTrue();
      assertThat(map.containsKey("key2")).isTrue();
    }

    @Test
    @DisplayName("キャッシュが空の場合、空のマップを返すこと")
    void shouldReturnEmptyMapWhenCacheIsEmpty() {
      // Act
      ConcurrentMap<String, IdempotencyKeyStore.IdempotencyResult> map = store.asMap();

      // Assert
      assertThat(map).isEmpty();
    }
  }
}
