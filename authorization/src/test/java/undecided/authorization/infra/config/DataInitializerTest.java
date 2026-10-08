package undecided.authorization.infra.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import undecided.authorization.domain.model.user.UserRepository;

/** {@code DataInitializerTest} は、DataInitializerの統合テストクラスです。 */
@Tag("medium")
@DisplayName("DataInitializerの統合テスト")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(DataInitializerTest.TestcontainersConfig.class)
class DataInitializerTest {

  @Autowired private UserRepository userRepository;

  @TestConfiguration
  static class TestcontainersConfig {
    @Bean
    @ServiceConnection(name = "postgresql")
    public PostgreSQLContainer<?> postgresContainer() {
      return new PostgreSQLContainer<>("postgres:17.0");
    }
  }

  @Nested
  @DisplayName("アプリケーション起動時の初期化テスト")
  static class InitializationTest {

    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("初回起動時にデフォルト管理者が自動作成されること")
    void shouldCreateDefaultAdminOnFirstStart() {
      // Assert
      assertThat(userRepository.count()).as("初期化後にユーザーが存在すること").isGreaterThan(0);

      assertThat(userRepository.findByUsername("admin")).as("adminユーザーが存在すること").isPresent();
    }

    @Test
    @DisplayName("デフォルト管理者はROLE_ADMINロールを持つこと")
    void shouldHaveRoleAdminRole() {
      // Act & Assert
      assertThat(userRepository.findByUsername("admin"))
          .as("adminユーザーがROLE_ADMINロールを持つこと")
          .isPresent()
          .get()
          .satisfies(user -> assertThat(user.hasRole("ROLE_ADMIN")).isTrue());
    }

    @Test
    @DisplayName("デフォルト管理者はfirstLoginフラグがtrueであること")
    void shouldHaveFirstLoginTrueForAdmin() {
      // Act & Assert
      assertThat(userRepository.findByUsername("admin"))
          .as("adminユーザーのfirstLoginがtrueであること")
          .isPresent()
          .get()
          .satisfies(user -> assertThat(user.isFirstLogin()).isTrue());
    }

    @Test
    @DisplayName("デフォルト管理者のユーザー名はadminであること")
    void shouldHaveAdminUsername() {
      // Act & Assert
      assertThat(userRepository.findByUsername("admin"))
          .as("adminユーザーが存在すること")
          .isPresent()
          .get()
          .satisfies(user -> assertThat(user.getUsername()).isEqualTo("admin"));
    }

    @Test
    @DisplayName("デフォルト管理者は有効状態であること")
    void shouldHaveAdminEnabled() {
      // Act & Assert
      assertThat(userRepository.findByUsername("admin"))
          .as("adminユーザーが有効状態であること")
          .isPresent()
          .get()
          .satisfies(user -> assertThat(user.isEnabled()).isTrue());
    }
  }
}
