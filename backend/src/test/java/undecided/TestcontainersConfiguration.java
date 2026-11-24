package undecided;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
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
 * <p><b>シングルトンコンテナ</b>: コンテナは静的初期化で起動し、テストJVMの間は起動したままにします。 Spring
 * BootのTestcontainersライフサイクルは、すでに起動しているコンテナをコンテキストのクローズで停止しません。
 * 以前の実装はコンテナの起動をSpringに委ねていたため、あるテストコンテキストが閉じられる （{@code @DirtiesContext}
 * やコンテキストキャッシュの追い出し）と共有コンテナまで停止され、 後続のテストクラスが停止済みデータベースに対して実行されて失敗しました（Issue #737）。
 */
@TestConfiguration(proxyBeanMethods = false)
@EntityScan(basePackages = {"org.springframework.modulith.events.jpa"})
public class TestcontainersConfiguration {

  private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
      new PostgreSQLContainer<>("postgres:17.0");

  private static final GenericContainer<?> REDIS_CONTAINER =
      new GenericContainer<>("redis:7.2").withExposedPorts(6379);

  static {
    POSTGRES_CONTAINER.start();
    REDIS_CONTAINER.start();
  }

  @Bean
  @ServiceConnection
  PostgreSQLContainer<?> postgresContainer() {
    return POSTGRES_CONTAINER;
  }

  @Bean
  @ServiceConnection(name = "redis")
  GenericContainer<?> redisContainer() {
    return REDIS_CONTAINER;
  }
}
