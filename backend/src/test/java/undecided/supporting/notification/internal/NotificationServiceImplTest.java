package undecided.supporting.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.supporting.notification.internal.Notification;
import undecided.supporting.notification.spi.NotificationDTO;
import undecided.supporting.notification.spi.NotificationRequest;
import undecided.supporting.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * {@code NotificationServiceImplTest} は、NotificationServiceImpl の単体テストクラスです。
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
@DisplayName("NotificationServiceImplの単体テスト")
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

  UUID recipientId = UUID.randomUUID();
  UUID sourceId = UUID.randomUUID();
  String sourceDomain = "agiloom";
  String title = "Test notification";
  String content = "This is a test notification";
  NotificationType type = NotificationType.SYSTEM;

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private SnowflakeIdGenerator snowflakeIdGenerator;

  @InjectMocks
  private NotificationServiceImpl notificationService;

  @Captor
  private ArgumentCaptor<Notification> notificationCaptor;

  private NotificationRequest createRequest() {
    return NotificationRequest.builder(sourceDomain)
        .recipientId(recipientId)
        .sourceId(sourceId)
        .title(title)
        .content(content)
        .type(type)
        .build();
  }

  private Notification createSavedNotification() {
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
  @DisplayName("sendメソッドのテスト")
  class SendMethodTest {

    @Test
    @DisplayName("通知リクエストから通知が作成され保存されDTOが返されること")
    void shouldCreateAndSaveNotification() {
      // Arrange
      NotificationRequest request = createRequest();
      Notification savedNotification = createSavedNotification();
      when(snowflakeIdGenerator.nextId()).thenReturn(1L);
      when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

      // Act
      NotificationDTO result = notificationService.send(request);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId().value()).isEqualTo(1L);
      assertThat(result.getRecipientId()).isEqualTo(recipientId);
      assertThat(result.getSourceDomain()).isEqualTo(sourceDomain);
      assertThat(result.getTitle()).isEqualTo(title);
      verify(notificationRepository).save(notificationCaptor.capture());
      Notification captured = notificationCaptor.getValue();
      assertThat(captured.getRecipientId()).isEqualTo(recipientId);
      assertThat(captured.getSourceId()).isEqualTo(sourceId);
      assertThat(captured.getSourceDomain()).isEqualTo(sourceDomain);
      assertThat(captured.getTitle()).isEqualTo(title);
      assertThat(captured.getContent()).isEqualTo(content);
      assertThat(captured.getType()).isEqualTo(type);
      assertThat(captured.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("sourceIdがnullの場合でも通知が作成されること")
    void shouldCreateNotificationWithoutSourceId() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title(title)
          .content(content)
          .type(type)
          .build();
      Notification savedNotification = Notification.builder()
          .id(SnowflakeId.of(2L))
          .recipientId(recipientId)
          .sourceDomain(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();
      when(snowflakeIdGenerator.nextId()).thenReturn(2L);
      when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

      // Act
      NotificationDTO result = notificationService.send(request);

      // Assert
      assertThat(result).isNotNull();
      verify(notificationRepository).save(notificationCaptor.capture());
      assertThat(notificationCaptor.getValue().getSourceId()).isNull();
    }

    @Test
    @DisplayName("異なる通知タイプで通知が作成されること")
    void shouldCreateNotificationWithDifferentTypes() {
      // Arrange
      when(snowflakeIdGenerator.nextId()).thenReturn(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
      for (NotificationType notificationType : NotificationType.values()) {
        NotificationRequest request = NotificationRequest.builder(sourceDomain)
            .recipientId(recipientId)
            .title(title)
            .content(content)
            .type(notificationType)
            .build();
        Notification savedNotification = Notification.builder()
            .id(SnowflakeId.of(1L))
            .recipientId(recipientId)
            .sourceDomain(sourceDomain)
            .title(title)
            .content(content)
            .type(notificationType)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // Act
        NotificationDTO result = notificationService.send(request);

        // Assert
        assertThat(result.getType()).isEqualTo(notificationType);
      }
    }
  }

  @Nested
  @DisplayName("getNotificationsByRecipientメソッドのテスト")
  class GetNotificationsByRecipientTest {

    @Test
    @DisplayName("受信者の通知一覧がDTOとして返されること")
    void shouldReturnNotificationsByRecipient() {
      // Arrange
      List<Notification> expectedNotifications = List.of(
          createSavedNotification(),
          Notification.builder()
              .id(SnowflakeId.of(2L))
              .recipientId(recipientId)
              .sourceDomain(sourceDomain)
              .title("Another notification")
              .content("Content")
              .type(NotificationType.MENTION)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build()
      );
      when(notificationRepository.findByRecipientIdAndDeletedAtIsNull(recipientId)).thenReturn(expectedNotifications);

      // Act
      List<NotificationDTO> result = notificationService.getNotificationsByRecipient(recipientId);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getId().value()).isEqualTo(1L);
      assertThat(result.get(1).getId().value()).isEqualTo(2L);
      verify(notificationRepository).findByRecipientIdAndDeletedAtIsNull(recipientId);
    }

    @Test
    @DisplayName("通知がない場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoNotifications() {
      // Arrange
      when(notificationRepository.findByRecipientIdAndDeletedAtIsNull(recipientId)).thenReturn(List.of());

      // Act
      List<NotificationDTO> result = notificationService.getNotificationsByRecipient(recipientId);

      // Assert
      assertThat(result).isEmpty();
      verify(notificationRepository).findByRecipientIdAndDeletedAtIsNull(recipientId);
    }
  }

  @Nested
  @DisplayName("getUnreadNotificationsByRecipientメソッドのテスト")
  class GetUnreadNotificationsByRecipientTest {

    @Test
    @DisplayName("未読通知のみがDTOとして返されること")
    void shouldReturnOnlyUnreadNotifications() {
      // Arrange
      List<Notification> unreadNotifications = List.of(
          Notification.builder()
              .id(SnowflakeId.of(1L))
              .recipientId(recipientId)
              .sourceDomain(sourceDomain)
              .title("Unread notification")
              .content("Content")
              .type(NotificationType.SYSTEM)
              .isRead(false)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build()
      );
      when(notificationRepository.findByRecipientIdAndIsReadAndDeletedAtIsNull(recipientId, false))
          .thenReturn(unreadNotifications);

      // Act
      List<NotificationDTO> result = notificationService.getUnreadNotificationsByRecipient(recipientId);

      // Assert
      assertThat(result).hasSize(1);
      assertThat(result.get(0).isRead()).isFalse();
      verify(notificationRepository).findByRecipientIdAndIsReadAndDeletedAtIsNull(recipientId, false);
    }

    @Test
    @DisplayName("未読通知がない場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoUnreadNotifications() {
      // Arrange
      when(notificationRepository.findByRecipientIdAndIsReadAndDeletedAtIsNull(recipientId, false))
          .thenReturn(List.of());

      // Act
      List<NotificationDTO> result = notificationService.getUnreadNotificationsByRecipient(recipientId);

      // Assert
      assertThat(result).isEmpty();
      verify(notificationRepository).findByRecipientIdAndIsReadAndDeletedAtIsNull(recipientId, false);
    }
  }

  @Nested
  @DisplayName("markAsReadメソッドのテスト")
  class MarkAsReadTest {

    @Test
    @DisplayName("通知が既読としてマークされること")
    void shouldMarkNotificationAsRead() {
      // Arrange
      SnowflakeId notificationId = SnowflakeId.of(1L);
      Notification notification = Notification.builder()
          .id(notificationId)
          .recipientId(recipientId)
          .sourceDomain(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .isRead(false)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();
      when(notificationRepository.findByIdAndDeletedAtIsNull(any(SnowflakeId.class))).thenReturn(Optional.of(notification));

      // Act
      notificationService.markAsRead(String.valueOf(notificationId.value()));

      // Assert
      assertThat(notification.getIsRead()).isTrue();
      verify(notificationRepository).save(notification);
    }

    @Test
    @DisplayName("存在しない通知IDの場合、例外がスローされること")
    void shouldThrowExceptionWhenNotificationNotFound() {
      // Arrange
      SnowflakeId notificationId = SnowflakeId.of(999L);
      when(notificationRepository.findByIdAndDeletedAtIsNull(any(SnowflakeId.class))).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> notificationService.markAsRead(String.valueOf(notificationId.value())))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("通知が見つかりません");
      verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("nullの通知IDの場合、NullPointerExceptionがスローされること")
    void shouldThrowExceptionWhenNotificationIdIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> notificationService.markAsRead(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("notificationId");
    }

    @Test
    @DisplayName("無効なID文字列の場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenInvalidIdString() {
      // Act & Assert
      assertThatThrownBy(() -> notificationService.markAsRead("invalid-id"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("通知IDは数値である必要があります");
    }

    @Test
    @DisplayName("既に既読の通知を再度markAsReadしても問題ないこと")
    void shouldIdempotentMarkAsRead() {
      // Arrange
      SnowflakeId notificationId = SnowflakeId.of(1L);
      LocalDateTime beforeUpdate = LocalDateTime.now().minusSeconds(10);
      Notification notification = Notification.builder()
          .id(notificationId)
          .recipientId(recipientId)
          .sourceDomain(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .isRead(true)
          .createdAt(LocalDateTime.now())
          .updatedAt(beforeUpdate)
          .build();
      when(notificationRepository.findByIdAndDeletedAtIsNull(any(SnowflakeId.class))).thenReturn(Optional.of(notification));

      // Act
      notificationService.markAsRead(String.valueOf(notificationId.value()));

      // Assert
      assertThat(notification.getIsRead()).isTrue();
      verify(notificationRepository).save(notification);
    }
  }
}
