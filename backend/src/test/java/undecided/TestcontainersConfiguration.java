package undecided;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Testcontainersを使用したPostgreSQLのテスト用設定クラスです。
 *
 * <p>Mediumテスト（統合テスト）でPostgreSQLコンテナを起動し、 {@link ServiceConnection} を通じてDataSourceの接続設定を自動構成します。
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgresContainer() {
    return new PostgreSQLContainer<>("postgres:17.0");
  }
}
