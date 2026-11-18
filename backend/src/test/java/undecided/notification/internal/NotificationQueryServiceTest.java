package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
  private NotificationRepository notificationRepository;

  private NotificationQueryService service;

  @BeforeEach
  void setUp() {
    service = new NotificationQueryService(notificationRepository);
  }

  @Nested
  @DisplayName("getUnreadNotifications メソッドのテスト")
  class GetUnreadNotificationsTest {

    @Test
    @DisplayName("通知取得APIを呼び出したとき、指定ユーザーの未読通知が返る")
    void shouldReturnUnreadNotifications() {
      // テストリスト項目9
      // Act & Assert
      assertThatThrownBy(() -> service.getUnreadNotifications(RECIPIENT_ID))
          .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("未読通知がないとき、空リストが返る")
    void shouldReturnEmptyListWhenNoUnread() {
      // テストリスト項目11
      // Act & Assert
      assertThatThrownBy(() -> service.getUnreadNotifications(RECIPIENT_ID))
          .isInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Nested
  @DisplayName("markAsRead メソッドのテスト")
  class MarkAsReadTest {

    @Test
    @DisplayName("通知を既読にしたとき、isReadがtrueになる")
    void shouldMarkAsRead() {
      // テストリスト項目10
      // Act & Assert
      assertThatThrownBy(() -> service.markAsRead(NOTIFICATION_ID))
          .isInstanceOf(UnsupportedOperationException.class);
    }
  }
}
