package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Notificationのテスト")
class NotificationTest {

  private Notification createNotification() {
    return Notification.builder()
        .id(SnowflakeId.of(1L))
        .recipientId(UUID.randomUUID())
        .productId(UUID.randomUUID())
        .title("Task assigned")
        .content("You have been assigned to a new task")
        .type(NotificationType.ASSIGNEE_CHANGE)
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      Notification notification = createNotification();

      assertThat(notification).isNotNull();
      assertThat(notification.getTitle()).isEqualTo("Task assigned");
      assertThat(notification.getType()).isEqualTo(NotificationType.ASSIGNEE_CHANGE);
      assertThat(notification.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("デフォルトでは未読である")
    void shouldBeUnreadByDefault() {
      Notification notification = createNotification();

      assertThat(notification.getIsRead()).isFalse();
    }
  }

  @Nested
  @DisplayName("markAsReadメソッドのテスト")
  class MarkAsReadMethodTest {

    @Test
    @DisplayName("既読フラグがtrueに更新される")
    void shouldMarkAsRead() {
      Notification notification = createNotification();

      notification.markAsRead();

      assertThat(notification.getIsRead()).isTrue();
      assertThat(notification.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("既読后再呼び出ししても問題ない")
    void shouldIdempotentMarkAsRead() {
      Notification notification = createNotification();
      notification.markAsRead();
      notification.markAsRead();

      assertThat(notification.getIsRead()).isTrue();
    }
  }
}
