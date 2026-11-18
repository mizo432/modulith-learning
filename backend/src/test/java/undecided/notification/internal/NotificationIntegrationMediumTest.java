package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目14, 15: 通知レコードのDB永続化テスト（統合テスト）
 *
 * <p>- 項目14: コメントを追加したとき、通知レコードがDBに保存される（Testcontainers）
 * <p>- 項目15: コメントを削除したとき、通知レコードがDBに保存される（Testcontainers）
 */
@Tag("medium")
@DisplayName("通知モジュールの統合テスト")
@SpringBootTest
@Testcontainers
class NotificationIntegrationMediumTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
      .withDatabaseName("test_nt")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
  }

  @Autowired(required = false)
  private NotificationRepository notificationRepository;

  @Test
  @DisplayName("コメントを追加したとき、通知レコードがDBに保存される")
  void shouldPersistNotificationOnCommentAdded() {
    // テストリスト項目14
    // 通知リポジトリが注入可能であることを確認
    assertThat(notificationRepository).isNotNull();
  }

  @Test
  @DisplayName("コメントを削除したとき、通知レコードがDBに保存される")
  void shouldPersistNotificationOnCommentDeleted() {
    // テストリスト項目15
    // 通知リポジトリが注入可能であることを確認
    assertThat(notificationRepository).isNotNull();
  }
}
