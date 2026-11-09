package undecided.supporting.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import undecided.supporting.notification.spi.NotificationType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code NotificationTest} は、Notificationエンティティの単体テストクラスです。
 * <p>
 * 規約要件:
 * 1. @Tag("small") を付与
 * 2. クラス・メソッドともに日本語 @DisplayName を付与
 * 3. 対象メソッドごとに @Nested クラスでグループ化
 * 4. メソッド名は should で開始し、アンダースコアを使用しない
 * 5. package-private 可視性
 * 6. null や境界値のテストを含める
 */
@Tag("small")
@DisplayName("Notificationエンティティのテスト")
class NotificationTest {

  private final UUID recipientId = UUID.randomUUID();
  private final UUID sourceId = UUID.randomUUID();
  private final String sourceDomain = "agiloom";
  private final String title = "Task assigned";
  private final String content = "You have been assigned to a new task";
  private final NotificationType type = NotificationType.ASSIGNEE_CHANGE;

  private Notification createNotification() {
    return Notification.builder()
        .id(SnowflakeId.of(1L))
        .recipientId(recipientId)
        .sourceId(sourceId)
        .sourceDomain(sourceDomain)
        .title(title)
        .content(content)
        .type(type)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成されること")
    void shouldCreateInstanceWithBuilder() {
      // Act
      Notification notification = createNotification();

      // Assert
      assertThat(notification).isNotNull();
      assertThat(notification.getId().value()).isEqualTo(1L);
      assertThat(notification.getRecipientId()).isEqualTo(recipientId);
      assertThat(notification.getSourceId()).isEqualTo(sourceId);
      assertThat(notification.getSourceDomain()).isEqualTo(sourceDomain);
      assertThat(notification.getTitle()).isEqualTo(title);
      assertThat(notification.getContent()).isEqualTo(content);
      assertThat(notification.getType()).isEqualTo(type);
    }

    @Test
    @DisplayName("デフォルトでは未読であること")
    void shouldBeUnreadByDefault() {
      // Act
      Notification notification = createNotification();

      // Assert
      assertThat(notification.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("明示的に作成日時と更新日時が設定できること")
    void shouldSetCreatedAtAndUpdatedAtExplicitly() {
      // Arrange
      LocalDateTime now = LocalDateTime.now();

      // Act
      Notification notification = Notification.builder()
          .id(SnowflakeId.of(1L))
          .recipientId(recipientId)
          .sourceDomain(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .createdAt(now)
          .updatedAt(now)
          .build();

      // Assert
      assertThat(notification.getCreatedAt()).isEqualTo(now);
      assertThat(notification.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("明示的に既読としてビルドできること")
    void shouldBuildAsRead() {
      // Act
      Notification notification = Notification.builder()
          .id(SnowflakeId.of(1L))
          .recipientId(recipientId)
          .sourceDomain(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .isRead(true)
          .build();

      // Assert
      assertThat(notification.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("sourceIdなしでビルドできること")
    void shouldBuildWithoutSourceId() {
      // Act
      Notification notification = Notification.builder()
          .id(SnowflakeId.of(1L))
          .recipientId(recipientId)
          .sourceDomain(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .build();

      // Assert
      assertThat(notification.getSourceId()).isNull();
    }

    @Test
    @DisplayName("異なる通知タイプでビルドできること")
    void shouldBuildWithDifferentTypes() {
      // Act & Assert
      for (NotificationType notificationType : NotificationType.values()) {
        Notification notification = Notification.builder()
            .id(SnowflakeId.of(1L))
            .recipientId(recipientId)
            .sourceDomain(sourceDomain)
            .title(title)
            .content(content)
            .type(notificationType)
            .build();

        assertThat(notification.getType())
            .as("通知タイプ %s でビルドできること", notificationType)
            .isEqualTo(notificationType);
      }
    }

    @Test
    @DisplayName("deletedAtはデフォルトでnullであること")
    void shouldHaveNullDeletedAtByDefault() {
      // Act
      Notification notification = createNotification();

      // Assert
      assertThat(notification.getDeletedAt()).isNull();
    }
  }

  @Nested
  @DisplayName("markAsReadメソッドのテスト")
  class MarkAsReadMethodTest {

    @Test
    @DisplayName("既読フラグがtrueに更新されること")
    void shouldMarkAsRead() {
      // Arrange
      Notification notification = createNotification();

      // Act
      notification.markAsRead();

      // Assert
      assertThat(notification.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("既読后再呼び出ししても問題ないこと")
    void shouldIdempotentMarkAsRead() {
      // Arrange
      Notification notification = createNotification();

      // Act
      notification.markAsRead();
      notification.markAsRead();

      // Assert
      assertThat(notification.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("既に既読の通知をmarkAsReadしても問題ないこと")
    void shouldMarkAlreadyReadNotification() {
      // Arrange
      Notification notification = Notification.builder()
          .id(SnowflakeId.of(1L))
          .recipientId(recipientId)
          .sourceDomain(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .isRead(true)
          .build();

      // Act
      notification.markAsRead();

      // Assert
      assertThat(notification.getIsRead()).isTrue();
    }
  }

  @Nested
  @DisplayName("ビルダーで各プロパティが設定できること")
  class BuilderPropertiesTest {

    @Test
    @DisplayName("ビルダーで各プロパティが設定できること")
    void shouldSetAllPropertiesWithBuilder() {
      // Arrange
      SnowflakeId newId = SnowflakeId.of(2L);
      UUID newRecipientId = UUID.randomUUID();
      UUID newSourceId = UUID.randomUUID();
      String newSourceDomain = "jobmanagement";
      String newTitle = "New title";
      String newContent = "New content";
      NotificationType newType = NotificationType.SYSTEM;
      LocalDateTime newDeletedAt = LocalDateTime.now();

      // Act
      Notification notification = Notification.builder()
          .id(newId)
          .recipientId(newRecipientId)
          .sourceId(newSourceId)
          .sourceDomain(newSourceDomain)
          .title(newTitle)
          .content(newContent)
          .type(newType)
          .isRead(true)
          .deletedAt(newDeletedAt)
          .build();

      // Assert
      assertThat(notification.getId()).isEqualTo(newId);
      assertThat(notification.getRecipientId()).isEqualTo(newRecipientId);
      assertThat(notification.getSourceId()).isEqualTo(newSourceId);
      assertThat(notification.getSourceDomain()).isEqualTo(newSourceDomain);
      assertThat(notification.getTitle()).isEqualTo(newTitle);
      assertThat(notification.getContent()).isEqualTo(newContent);
      assertThat(notification.getType()).isEqualTo(newType);
      assertThat(notification.getIsRead()).isTrue();
      assertThat(notification.getDeletedAt()).isEqualTo(newDeletedAt);
    }
  }

  @Nested
  @DisplayName("NotificationTypeのテスト")
  class NotificationTypeTest {

    @Test
    @DisplayName("すべてのタイプが定義されていること")
    void shouldHaveAllTypes() {
      // Act
      NotificationType[] types = NotificationType.values();

      // Assert
      assertThat(types).hasSize(8)
          .contains(NotificationType.MENTION)
          .contains(NotificationType.ASSIGNEE_CHANGE)
          .contains(NotificationType.DUE_DATE)
          .contains(NotificationType.AWAITING_DECISION)
          .contains(NotificationType.APPROVAL_RESULT)
          .contains(NotificationType.JOB_EXECUTION_RESULT)
          .contains(NotificationType.SYSTEM)
          .contains(NotificationType.CUSTOM);
    }

    @Test
    @DisplayName("タイプ名が文字列として取得できること")
    void shouldGetNameAsString() {
      // Act & Assert
      assertThat(NotificationType.SYSTEM.name()).isEqualTo("SYSTEM");
      assertThat(NotificationType.MENTION.name()).isEqualTo("MENTION");
      assertThat(NotificationType.CUSTOM.name()).isEqualTo("CUSTOM");
    }

    @Test
    @DisplayName("文字列からタイプが取得できること")
    void shouldGetValueFromString() {
      // Act & Assert
      assertThat(NotificationType.valueOf("SYSTEM")).isEqualTo(NotificationType.SYSTEM);
      assertThat(NotificationType.valueOf("MENTION")).isEqualTo(NotificationType.MENTION);
      assertThat(NotificationType.valueOf("CUSTOM")).isEqualTo(NotificationType.CUSTOM);
    }
  }
}
