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
import com.github.mizo432.shared.exception.BusinessException;
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

  private static final UUID RECIPIENT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID SOURCE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
  private static final String SOURCE_DOMAIN = "agiloom";
  private static final String TITLE = "Test notification";
  private static final String CONTENT = "This is a test notification";
  private static final NotificationType TYPE = NotificationType.SYSTEM;

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private SnowflakeIdGenerator snowflakeIdGenerator;

  @InjectMocks
  private NotificationServiceImpl notificationService;

  @Captor
  private ArgumentCaptor<Notification> notificationCaptor;

  private NotificationRequest createRequest() {
    return NotificationRequest.builder(SOURCE_DOMAIN)
        .recipientId(RECIPIENT_ID)
        .sourceId(SOURCE_ID)
        .title(TITLE)
        .content(CONTENT)
        .type(TYPE)
        .build();
  }

  private Notification createSavedNotification() {
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
      assertThat(result.getRecipientId()).isEqualTo(RECIPIENT_ID);
      assertThat(result.getSourceDomain()).isEqualTo(SOURCE_DOMAIN);
      assertThat(result.getTitle()).isEqualTo(TITLE);
      verify(notificationRepository).save(notificationCaptor.capture());
      Notification captured = notificationCaptor.getValue();
      assertThat(captured.getRecipientId()).isEqualTo(RECIPIENT_ID);
      assertThat(captured.getSourceId()).isEqualTo(SOURCE_ID);
      assertThat(captured.getSourceDomain()).isEqualTo(SOURCE_DOMAIN);
      assertThat(captured.getTitle()).isEqualTo(TITLE);
      assertThat(captured.getContent()).isEqualTo(CONTENT);
      assertThat(captured.getType()).isEqualTo(TYPE);
      assertThat(captured.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("sourceIdがnullの場合でも通知が作成されること")
    void shouldCreateNotificationWithoutSourceId() {
      // Arrange
      NotificationRequest request = NotificationRequest.builder(SOURCE_DOMAIN)
          .recipientId(RECIPIENT_ID)
          .title(TITLE)
          .content(CONTENT)
          .type(TYPE)
          .build();
      Notification savedNotification = Notification.builder()
          .id(SnowflakeId.of(2L))
          .recipientId(RECIPIENT_ID)
          .sourceDomain(SOURCE_DOMAIN)
          .title(TITLE)
          .content(CONTENT)
          .type(TYPE)
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
        NotificationRequest request = NotificationRequest.builder(SOURCE_DOMAIN)
            .recipientId(RECIPIENT_ID)
            .title(TITLE)
            .content(CONTENT)
            .type(notificationType)
            .build();
        Notification savedNotification = Notification.builder()
            .id(SnowflakeId.of(1L))
            .recipientId(RECIPIENT_ID)
            .sourceDomain(SOURCE_DOMAIN)
            .title(TITLE)
            .content(CONTENT)
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
              .recipientId(RECIPIENT_ID)
              .sourceDomain(SOURCE_DOMAIN)
              .title("Another notification")
              .content("Content")
              .type(NotificationType.MENTION)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build()
      );
      when(notificationRepository.findByRecipientIdAndDeletedAtIsNull(RECIPIENT_ID)).thenReturn(expectedNotifications);

      // Act
      List<NotificationDTO> result = notificationService.getNotificationsByRecipient(RECIPIENT_ID);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getId().value()).isEqualTo(1L);
      assertThat(result.get(1).getId().value()).isEqualTo(2L);
      verify(notificationRepository).findByRecipientIdAndDeletedAtIsNull(RECIPIENT_ID);
    }

    @Test
    @DisplayName("通知がない場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoNotifications() {
      // Arrange
      when(notificationRepository.findByRecipientIdAndDeletedAtIsNull(RECIPIENT_ID)).thenReturn(List.of());

      // Act
      List<NotificationDTO> result = notificationService.getNotificationsByRecipient(RECIPIENT_ID);

      // Assert
      assertThat(result).isEmpty();
      verify(notificationRepository).findByRecipientIdAndDeletedAtIsNull(RECIPIENT_ID);
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
              .recipientId(RECIPIENT_ID)
              .sourceDomain(SOURCE_DOMAIN)
              .title("Unread notification")
              .content("Content")
              .type(NotificationType.SYSTEM)
              .isRead(false)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build()
      );
      when(notificationRepository.findByRecipientIdAndIsReadAndDeletedAtIsNull(RECIPIENT_ID, false))
          .thenReturn(unreadNotifications);

      // Act
      List<NotificationDTO> result = notificationService.getUnreadNotificationsByRecipient(RECIPIENT_ID);

      // Assert
      assertThat(result).hasSize(1);
      assertThat(result.get(0).isRead()).isFalse();
      verify(notificationRepository).findByRecipientIdAndIsReadAndDeletedAtIsNull(RECIPIENT_ID, false);
    }

    @Test
    @DisplayName("未読通知がない場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoUnreadNotifications() {
      // Arrange
      when(notificationRepository.findByRecipientIdAndIsReadAndDeletedAtIsNull(RECIPIENT_ID, false))
          .thenReturn(List.of());

      // Act
      List<NotificationDTO> result = notificationService.getUnreadNotificationsByRecipient(RECIPIENT_ID);

      // Assert
      assertThat(result).isEmpty();
      verify(notificationRepository).findByRecipientIdAndIsReadAndDeletedAtIsNull(RECIPIENT_ID, false);
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
          .recipientId(RECIPIENT_ID)
          .sourceDomain(SOURCE_DOMAIN)
          .title(TITLE)
          .content(CONTENT)
          .type(TYPE)
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
          .isInstanceOf(BusinessException.class)
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
    @DisplayName("無効なID文字列の場合、BusinessExceptionがスローされること")
    void shouldThrowExceptionWhenInvalidIdString() {
      // Act & Assert
      assertThatThrownBy(() -> notificationService.markAsRead("invalid-id"))
          .isInstanceOf(BusinessException.class)
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
          .recipientId(RECIPIENT_ID)
          .sourceDomain(SOURCE_DOMAIN)
          .title(TITLE)
          .content(CONTENT)
          .type(TYPE)
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
