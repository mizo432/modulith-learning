package undecided.notification.spi;

/**
 * 通知の種類を区分する列挙型。
 *
 * <p>英語コードで保持し、DB にはコード値のみをセットする（ADR-003）。
 */
public enum NotificationType {
  /** コメントが追加された通知 */
  COMMENT_ADDED,

  /** コメントが削除された通知 */
  COMMENT_DELETED,

  /** バックログアイテムが分割された通知 */
  BACKLOG_ITEM_SPLIT,

  /** バックログアイテムのスプリント割り当てが変更された通知 */
  BACKLOG_ITEM_SPRINT_ASSIGNMENT_CHANGED
}
