package undecided;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Testcontainersを使用したPostgreSQLおよびRedisのテスト用設定クラスです。
 *
 * <p>Mediumテスト（統合テスト）でPostgreSQLコンテナおよびRedisコンテナを起動し、 {@link ServiceConnection}
 * を通じてDataSourceおよびRedisの接続設定を自動構成します。
 *
 * <p><b>パフォーマンス最適化</b>: コンテナは静的フィールドで共有され、テストクラス間で1回だけ起動されます。
 * これにより、テスト実行時のコンテナ起動オーバーヘッドが大幅に削減されます。
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

  private static PostgreSQLContainer<?> postgresContainer;
  private static GenericContainer<?> redisContainer;

  @Bean
  @ServiceConnection(name = "postgresql")
  public PostgreSQLContainer<?> postgresContainer() {
    if (postgresContainer == null) {
      postgresContainer = new PostgreSQLContainer<>("postgres:17.0");
      postgresContainer.start();
    }
    return postgresContainer;
  }

  @Bean
  @ServiceConnection(name = "redis")
  public GenericContainer<?> redisContainer() {
    if (redisContainer == null) {
      redisContainer = new GenericContainer<>("redis:7.2").withExposedPorts(6379);
      redisContainer.start();
    }
    return redisContainer;
  }
}
