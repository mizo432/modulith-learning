package undecided.notification.internal;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.notification.spi.NotificationEntity;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目9, 10, 11: 通知取得サービスのテスト
 *
 * <p>- 項目9: 通知取得APIを呼び出したとき、指定ユーザーの未読通知が返る
 * <p>- 項目10: 通知を既読にしたとき、isReadがtrueになる
 * <p>- 項目11: 未読通知がないとき、空リストが返る
 */
@Tag("small")
@DisplayName("NotificationQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class NotificationQueryServiceTest {

  private static final long RECIPIENT_ID_VALUE = 100L;
  private static final long NOTIFICATION_ID_VALUE = 5000L;
  private static final long ITEM_ID_VALUE = 10L;

  private static final SnowflakeId RECIPIENT_ID = SnowflakeId.of(RECIPIENT_ID_VALUE);
  private static final SnowflakeId NOTIFICATION_ID = SnowflakeId.of(NOTIFICATION_ID_VALUE);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);

  @Mock
  private NotificationEntityRepository notificationRepository;

  private NotificationQueryService service;

  @BeforeEach
  void setUp() {
    service = new NotificationQueryService(notificationRepository, Clock.systemDefaultZone());
  }

  @Nested
  @DisplayName("getUnreadNotifications メソッドのテスト")
  class GetUnreadNotificationsTest {

    @Test
    @DisplayName("通知取得APIを呼び出したとき、指定ユーザーの未読通知が返る")
    void shouldReturnUnreadNotifications() {
      // テストリスト項目9
      NotificationEntity notification =
          NotificationEntity.builder()
              .id(NOTIFICATION_ID)
              .recipientId(RECIPIENT_ID)
              .backlogItemId(ITEM_ID)
              .title("テスト通知")
              .content("テスト内容")
              .type(NotificationType.COMMENT_ADDED)
              .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      List<NotificationEntity> expected = List.of(notification);
      when(notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(RECIPIENT_ID))
          .thenReturn(expected);

      List<NotificationEntity> result = service.getUnreadNotifications(RECIPIENT_ID);

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getId()).isEqualTo(NOTIFICATION_ID);
    }

    @Test
    @DisplayName("未読通知がないとき、空リストが返る")
    void shouldReturnEmptyListWhenNoUnread() {
      // テストリスト項目11
      when(notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(RECIPIENT_ID))
          .thenReturn(Collections.emptyList());

      List<NotificationEntity> result = service.getUnreadNotifications(RECIPIENT_ID);

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("markAsRead メソッドのテスト")
  class MarkAsReadTest {

    @Test
    @DisplayName("通知を既読にしたとき、isReadがtrueになる")
    void shouldMarkAsRead() {
      // テストリスト項目10
      NotificationEntity notification =
          NotificationEntity.builder()
              .id(NOTIFICATION_ID)
              .recipientId(RECIPIENT_ID)
              .backlogItemId(ITEM_ID)
              .title("テスト通知")
              .content("テスト内容")
              .type(NotificationType.COMMENT_ADDED)
              .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(notification));

      service.markAsRead(NOTIFICATION_ID);

      assertThat(notification.getIsRead()).isTrue();
      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository).save(captor.capture());
      assertThat(captor.getValue().getIsRead()).isTrue();
    }

    @Test
    @DisplayName("存在しない通知IDを既読にしようとすると、何もしない")
    void shouldDoNothingWhenNotificationNotFound() {
      when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.empty());

      service.markAsRead(NOTIFICATION_ID);

      verify(notificationRepository, never()).save(any(NotificationEntity.class));
    }
  }
}
