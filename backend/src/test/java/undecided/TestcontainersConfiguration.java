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
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgresContainer() {
    return new PostgreSQLContainer<>("postgres:17.0");
  }

  @Bean
  @ServiceConnection
  public GenericContainer<?> redisContainer() {
    return new GenericContainer<>("redis:7.2").withExposedPorts(6379);
  }
}
