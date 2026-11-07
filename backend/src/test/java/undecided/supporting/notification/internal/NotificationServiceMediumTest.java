package undecided.supporting.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.supporting.notification.spi.Notification;
import undecided.supporting.notification.spi.NotificationRequest;
import undecided.supporting.notification.spi.NotificationService;
import undecided.supporting.notification.spi.NotificationType;

/**
 * {@code NotificationServiceMediumTest} は、NotificationServiceImpl の統合テストクラスです。
 * <p>
 * 規約要件:
 * 1. @Tag("medium") を付与
 * 2. @ActiveProfiles("test") および @Import(TestcontainersConfiguration.class) で PostgreSQL コンテナを利用
 * 3. クラス・メソッドともに日本語 @DisplayName を付与
 * 4. 対象メソッドごとに @Nested クラスでグループ化
 * 5. メソッド名は should で開始し、アンダースコアを使用しない
 * 6. package-private 可視性
 */
@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("NotificationServiceの統合テスト")
class NotificationServiceMediumTest {

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private NotificationRepository notificationRepository;

  private final UUID recipientId = UUID.randomUUID();
  private final UUID sourceId = UUID.randomUUID();
  private final String sourceDomain = "agiloom";

  @Nested
  @DisplayName("sendメソッドの統合テスト")
  class SendMethodTest {

    @Test
    @DisplayName("通知が正常に送信されデータベースに保存されること")
    void shouldSendAndSaveNotification() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .sourceId(sourceId)
          .title("統合テスト通知")
          .content("統合テストの本文です")
          .type(NotificationType.SYSTEM)
          .build();

      // Act
      Notification result = notificationService.send(request);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isNotNull();
      assertThat(result.getRecipientId()).isEqualTo(recipientId);
      assertThat(result.getSourceId()).isEqualTo(sourceId);
      assertThat(result.getSourceDomain()).isEqualTo(sourceDomain);
      assertThat(result.getTitle()).isEqualTo("統合テスト通知");
      assertThat(result.getContent()).isEqualTo("統合テストの本文です");
      assertThat(result.getType()).isEqualTo(NotificationType.SYSTEM);
      assertThat(result.getIsRead()).isFalse();
      assertThat(result.getCreatedAt()).isNotNull();
      assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("sourceIdなしで通知が送信されること")
    void shouldSendNotificationWithoutSourceId() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("sourceIdなしの通知")
          .content("sourceIdなしの本文")
          .type(NotificationType.MENTION)
          .build();

      // Act
      Notification result = notificationService.send(request);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isNotNull();
      assertThat(result.getSourceId()).isNull();
    }

    @Test
    @DisplayName("すべての通知タイプで通知が送信されること")
    void shouldSendNotificationWithAllTypes() {
      // Act & Assert
      for (NotificationType type : NotificationType.values()) {
        NotificationRequest request = NotificationRequest.builder(sourceDomain)
            .recipientId(recipientId)
            .title(type + "通知")
            .content(type + "の本文")
            .type(type)
            .build();

        Notification result = notificationService.send(request);

        assertThat(result)
            .as("通知タイプ %s で通知が送信されること", type)
            .isNotNull();
        assertThat(result.getType()).isEqualTo(type);
      }
    }
  }

  @Nested
  @DisplayName("getNotificationsByRecipientメソッドの統合テスト")
  class GetNotificationsByRecipientTest {

    @Test
    @DisplayName("受信者の通知一覧が取得されること")
    void shouldGetNotificationsByRecipient() {
      // Arrange
      NotificationRequest request1 = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("通知1")
          .content("本文1")
          .type(NotificationType.SYSTEM)
          .build();
      NotificationRequest request2 = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("通知2")
          .content("本文2")
          .type(NotificationType.MENTION)
          .build();
      notificationService.send(request1);
      notificationService.send(request2);

      // Act
      List<Notification> result = notificationService.getNotificationsByRecipient(recipientId);

      // Assert
      assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("通知がない受信者の場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoNotifications() {
      // Arrange
      UUID emptyRecipientId = UUID.randomUUID();

      // Act
      List<Notification> result = notificationService.getNotificationsByRecipient(emptyRecipientId);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getUnreadNotificationsByRecipientメソッドの統合テスト")
  class GetUnreadNotificationsByRecipientTest {

    @Test
    @DisplayName("未読通知のみが取得されること")
    void shouldGetOnlyUnreadNotifications() {
      // Arrange
      NotificationRequest request1 = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("未読通知")
          .content("未読の本文")
          .type(NotificationType.SYSTEM)
          .build();
      NotificationRequest request2 = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("既読通知")
          .content("既読の本文")
          .type(NotificationType.MENTION)
          .build();
      Notification unread = notificationService.send(request1);
      Notification read = notificationService.send(request2);
      read.markAsRead();
      notificationRepository.save(read);

      // Act
      List<Notification> result = notificationService.getUnreadNotificationsByRecipient(recipientId);

      // Assert
      assertThat(result).hasSize(1);
      assertThat(result.get(0).getIsRead()).isFalse();
    }

    @Test
    @DisplayName("未読通知がない場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoUnreadNotifications() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("既読のみ")
          .content("既読の本文")
          .type(NotificationType.SYSTEM)
          .build();
      Notification notification = notificationService.send(request);
      notification.markAsRead();
      notificationRepository.save(notification);

      // Act
      List<Notification> result = notificationService.getUnreadNotificationsByRecipient(recipientId);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("markAsReadメソッドの統合テスト")
  class MarkAsReadTest {

    @Test
    @DisplayName("通知が既読としてマークされること")
    void shouldMarkNotificationAsRead() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("既読テスト通知")
          .content("既読テストの本文")
          .type(NotificationType.SYSTEM)
          .build();
      Notification notification = notificationService.send(request);
      String notificationId = String.valueOf(notification.getId().value());

      // Act
      notificationService.markAsRead(notificationId);

      // Assert
      Notification updated = notificationRepository.findById(notification.getId()).orElseThrow();
      assertThat(updated.getIsRead()).isTrue();
      assertThat(updated.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("存在しない通知IDの場合、例外がスローされること")
    void shouldThrowExceptionWhenNotificationNotFound() {
      // Act & Assert
      assertThatThrownBy(() -> notificationService.markAsRead("999999999999999999"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Notification not found");
    }

    @Test
    @DisplayName("無効なID文字列の場合、例外がスローされること")
    void shouldThrowExceptionWhenInvalidIdString() {
      // Act & Assert
      assertThatThrownBy(() -> notificationService.markAsRead("invalid-id"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("既に既読の通知を再度markAsReadしても問題ないこと")
    void shouldIdempotentMarkAsRead() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("冪等性テスト通知")
          .content("冪等性テストの本文")
          .type(NotificationType.SYSTEM)
          .build();
      Notification notification = notificationService.send(request);
      String notificationId = String.valueOf(notification.getId().value());
      notificationService.markAsRead(notificationId);

      // Act
      notificationService.markAsRead(notificationId);

      // Assert
      Notification updated = notificationRepository.findById(notification.getId()).orElseThrow();
      assertThat(updated.getIsRead()).isTrue();
    }
  }

  @Nested
  @DisplayName("リポジトリメソッドの統合テスト")
  class RepositoryMethodTest {

    @Test
    @DisplayName("sourceDomainで通知が検索されること")
    void shouldFindBySourceDomain() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title("検索テスト通知")
          .content("検索テストの本文")
          .type(NotificationType.SYSTEM)
          .build();
      notificationService.send(request);

      // Act
      List<Notification> result = notificationRepository.findBySourceDomain(sourceDomain);

      // Assert
      assertThat(result).isNotEmpty();
      assertThat(result.get(0).getSourceDomain()).isEqualTo(sourceDomain);
    }
  }
}
