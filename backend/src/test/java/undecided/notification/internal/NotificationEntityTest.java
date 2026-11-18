package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目6: 通知エンティティのテスト
 *
 * <p>- 通知レコード作成時、isReadはfalseである
 * <p>- 通知を既読にしたとき、isReadがtrueになる
 */
@Tag("small")
@DisplayName("NotificationEntityの単体テスト")
class NotificationEntityTest {

  private static final long ID_VALUE = 5000L;
  private static final long RECIPIENT_ID_VALUE = 100L;
  private static final long ITEM_ID_VALUE = 10L;
  private static final long COMMENT_ID_VALUE = 1000L;

  private static final SnowflakeId ID = SnowflakeId.of(ID_VALUE);
  private static final SnowflakeId RECIPIENT_ID = SnowflakeId.of(RECIPIENT_ID_VALUE);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);

  private NotificationEntity createNotification() {
    return NotificationEntity.builder()
        .id(ID)
        .recipientId(RECIPIENT_ID)
        .backlogItemId(ITEM_ID)
        .commentId(COMMENT_ID)
        .title("コメントが追加されました")
        .content("バックログアイテムに新しいコメントが追加されました")
        .type(NotificationType.COMMENT_ADDED)
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      NotificationEntity entity = createNotification();

      assertThat(entity).isNotNull();
      assertThat(entity.getId()).isEqualTo(ID);
      assertThat(entity.getRecipientId()).isEqualTo(RECIPIENT_ID);
      assertThat(entity.getTitle()).isEqualTo("コメントが追加されました");
      assertThat(entity.getType()).isEqualTo(NotificationType.COMMENT_ADDED);
    }

    @Test
    @DisplayName("デフォルトでは未読である")
    void shouldBeUnreadByDefault() {
      NotificationEntity entity = createNotification();

      assertThat(entity.getIsRead()).isFalse();
      assertThat(entity.isUnread()).isTrue();
    }
  }

  @Nested
  @DisplayName("markAsReadメソッドのテスト")
  class MarkAsReadMethodTest {

    @Test
    @DisplayName("既読フラグがtrueに更新される")
    void shouldMarkAsRead() {
      NotificationEntity entity = createNotification();

      entity.markAsRead();

      assertThat(entity.getIsRead()).isTrue();
      assertThat(entity.isUnread()).isFalse();
      assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("既読后再呼び出ししても問題ない")
    void shouldIdempotentMarkAsRead() {
      NotificationEntity entity = createNotification();
      entity.markAsRead();
      entity.markAsRead();

      assertThat(entity.getIsRead()).isTrue();
    }
  }
}
