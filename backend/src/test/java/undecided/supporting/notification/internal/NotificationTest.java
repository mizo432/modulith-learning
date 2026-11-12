package undecided.supporting.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.notification.spi.Notification;
import undecided.supporting.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code NotificationTest} は、Notificationエンティティの単体テストクラスです。
 *
 * <p>規約要件: 1. @Tag("small") を付与 2. クラス・メソッドともに日本語 @DisplayName を付与 3. 対象メソッドごとに @Nested クラスでグループ化
 * 4. メソッド名は should で開始し、アンダースコアを使用しない 5. package-private 可視性 6. null や境界値のテストを含める
 */
@Tag("small")
@DisplayName("Notificationエンティティのテスト")
class NotificationTest {

  private static final UUID RECIPIENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
  private static final UUID SOURCE_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
  private static final String SOURCE_DOMAIN = "agiloom";
  private static final String TITLE = "Task assigned";
  private static final String CONTENT = "You have been assigned to a new task";
  private static final NotificationType TYPE = NotificationType.ASSIGNEE_CHANGE;

  private Notification createNotification() {
    return Notification.builder()
        .id(SnowflakeId.of(1L))
        .recipientId(RECIPIENT_ID)
        .sourceId(SOURCE_ID)
        .sourceDomain(SOURCE_DOMAIN)
        .title(TITLE)
        .content(CONTENT)
        .type(TYPE)
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
      assertThat(notification.getRecipientId()).isEqualTo(RECIPIENT_ID);
      assertThat(notification.getSourceId()).isEqualTo(SOURCE_ID);
      assertThat(notification.getSourceDomain()).isEqualTo(SOURCE_DOMAIN);
      assertThat(notification.getTitle()).isEqualTo(TITLE);
      assertThat(notification.getContent()).isEqualTo(CONTENT);
      assertThat(notification.getType()).isEqualTo(TYPE);
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
      Notification notification =
          Notification.builder()
              .id(SnowflakeId.of(1L))
              .recipientId(RECIPIENT_ID)
              .sourceDomain(SOURCE_DOMAIN)
              .title(TITLE)
              .content(CONTENT)
              .type(TYPE)
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
      Notification notification =
          Notification.builder()
              .id(SnowflakeId.of(1L))
              .recipientId(RECIPIENT_ID)
              .sourceDomain(SOURCE_DOMAIN)
              .title(TITLE)
              .content(CONTENT)
              .type(TYPE)
              .isRead(true)
              .build();

      // Assert
      assertThat(notification.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("sourceIdなしでビルドできること")
    void shouldBuildWithoutSourceId() {
      // Act
      Notification notification =
          Notification.builder()
              .id(SnowflakeId.of(1L))
              .recipientId(RECIPIENT_ID)
              .sourceDomain(SOURCE_DOMAIN)
              .title(TITLE)
              .content(CONTENT)
              .type(TYPE)
              .build();

      // Assert
      assertThat(notification.getSourceId()).isNull();
    }

    @Test
    @DisplayName("異なる通知タイプでビルドできること")
    void shouldBuildWithDifferentTypes() {
      // Act & Assert
      for (NotificationType notificationType : NotificationType.values()) {
        Notification notification =
            Notification.builder()
                .id(SnowflakeId.of(1L))
                .recipientId(RECIPIENT_ID)
                .sourceDomain(SOURCE_DOMAIN)
                .title(TITLE)
                .content(CONTENT)
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
    @DisplayName("markAsReadは冪等であること")
    void shouldIdempotentMarkAsRead() {
      // Arrange: 未読の通知を2回markAsRead
      Notification notification = createNotification();

      // Act
      notification.markAsRead();
      notification.markAsRead();

      // Assert
      assertThat(notification.getIsRead()).isTrue();

      // Arrange: 既に既読の通知をmarkAsRead
      Notification readNotification =
          Notification.builder()
              .id(SnowflakeId.of(1L))
              .recipientId(RECIPIENT_ID)
              .sourceDomain(SOURCE_DOMAIN)
              .title(TITLE)
              .content(CONTENT)
              .type(TYPE)
              .isRead(true)
              .build();

      // Act
      readNotification.markAsRead();

      // Assert
      assertThat(readNotification.getIsRead()).isTrue();
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
      UUID newRecipientId = UUID.fromString("66666666-6666-6666-6666-666666666666");
      UUID newSourceId = UUID.fromString("77777777-7777-7777-7777-777777777777");
      String newSourceDomain = "jobmanagement";
      String newTitle = "New title";
      String newContent = "New content";
      NotificationType newType = NotificationType.SYSTEM;
      LocalDateTime newDeletedAt = LocalDateTime.now();

      // Act
      Notification notification =
          Notification.builder()
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
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("titleとcontentが更新される")
    void shouldUpdateTitleAndContent() {
      // Arrange
      Notification notification = createNotification();

      // Act
      notification.update("New title", "New content", NotificationType.SYSTEM);

      // Assert
      assertThat(notification.getTitle()).isEqualTo("New title");
      assertThat(notification.getContent()).isEqualTo("New content");
      assertThat(notification.getType()).isEqualTo(NotificationType.SYSTEM);
    }

    @Test
    @DisplayName("typeはnullで更新できる")
    void shouldUpdateWithTypeAsNull() {
      // Arrange
      Notification notification = createNotification();

      // Act
      notification.update("New title", "New content", null);

      // Assert
      assertThat(notification.getTitle()).isEqualTo("New title");
      assertThat(notification.getContent()).isEqualTo("New content");
      assertThat(notification.getType()).isEqualTo(TYPE);
    }

    @Test
    @DisplayName("titleがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsNull() {
      // Arrange
      Notification notification = createNotification();

      // Act & Assert
      assertThatThrownBy(() -> notification.update(null, "Content", TYPE))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not be null");
    }

    @Test
    @DisplayName("contentがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenContentIsNull() {
      // Arrange
      Notification notification = createNotification();

      // Act & Assert
      assertThatThrownBy(() -> notification.update("Title", null, TYPE))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("content must not be null");
    }

    @Test
    @DisplayName("contentが空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenContentIsEmpty() {
      // Arrange
      Notification notification = createNotification();

      // Act & Assert
      assertThatThrownBy(() -> notification.update("Title", "   ", TYPE))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("content must not be empty");
    }

    @Test
    @DisplayName("titleが空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsEmpty() {
      // Arrange
      Notification notification = createNotification();

      // Act & Assert
      assertThatThrownBy(() -> notification.update("   ", "Content", TYPE))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not be empty");
    }

    @Test
    @DisplayName("titleが200文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleExceedsLength() {
      // Arrange
      Notification notification = createNotification();
      String longTitle = "A".repeat(201);

      // Act & Assert
      assertThatThrownBy(() -> notification.update(longTitle, "Content", TYPE))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not exceed 200 characters");
    }

    @Test
    @DisplayName("contentが2000文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenContentExceedsLength() {
      // Arrange
      Notification notification = createNotification();
      String longContent = "A".repeat(2001);

      // Act & Assert
      assertThatThrownBy(() -> notification.update("Title", longContent, TYPE))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("content must not exceed 2000 characters");
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
      assertThat(types)
          .hasSize(8)
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
