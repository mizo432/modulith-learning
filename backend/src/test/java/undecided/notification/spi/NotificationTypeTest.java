package undecided.notification.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * テストリスト項目12, 13: 通知タイプのテスト
 *
 * <p>- 項目12: コメント追加イベントのとき、NotificationTypeはCOMMENT_ADDEDである
 * <p>- 項目13: コメント削除イベントのとき、NotificationTypeはCOMMENT_DELETEDである
 */
@Tag("small")
@DisplayName("NotificationTypeの単体テスト")
class NotificationTypeTest {

  @Test
  @DisplayName("COMMENT_ADDEDが存在すること")
  void shouldHaveCommentAdded() {
    assertThat(NotificationType.COMMENT_ADDED).isNotNull();
    assertThat(NotificationType.COMMENT_ADDED.name()).isEqualTo("COMMENT_ADDED");
  }

  @Test
  @DisplayName("COMMENT_DELETEDが存在すること")
  void shouldHaveCommentDeleted() {
    assertThat(NotificationType.COMMENT_DELETED).isNotNull();
    assertThat(NotificationType.COMMENT_DELETED.name()).isEqualTo("COMMENT_DELETED");
  }

  @Test
  @DisplayName("分割通知を含む列挙値が存在すること")
  void shouldHaveSplitNotificationType() {
    assertThat(NotificationType.values())
        .containsExactlyInAnyOrder(
            NotificationType.COMMENT_ADDED,
            NotificationType.COMMENT_DELETED,
            NotificationType.BACKLOG_ITEM_SPLIT);
  }

  @Test
  @DisplayName("文字列から列挙値を取得できること")
  void shouldParseFromString() {
    assertThat(NotificationType.valueOf("COMMENT_ADDED")).isEqualTo(NotificationType.COMMENT_ADDED);
    assertThat(NotificationType.valueOf("COMMENT_DELETED")).isEqualTo(NotificationType.COMMENT_DELETED);
    assertThat(NotificationType.valueOf("BACKLOG_ITEM_SPLIT"))
        .isEqualTo(NotificationType.BACKLOG_ITEM_SPLIT);
  }
}
