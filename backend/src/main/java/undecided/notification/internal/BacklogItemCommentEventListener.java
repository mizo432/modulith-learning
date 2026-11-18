package undecided.notification.internal;

import java.util.List;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentDeleted;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * コメントイベントのリスナー。
 *
 * <p>requirementBacklog領域から発行されるコメント追加・削除イベントを受信し、
 * 通知レコードを作成します。
 */
public class BacklogItemCommentEventListener {

  private final NotificationRepository notificationRepository;
  private final CommentParticipantSubsetRepository commentParticipantSubsetRepository;
  private final EmailNotificationService emailNotificationService;

  public BacklogItemCommentEventListener(
      NotificationRepository notificationRepository,
      CommentParticipantSubsetRepository commentParticipantSubsetRepository,
      EmailNotificationService emailNotificationService) {
    this.notificationRepository = notificationRepository;
    this.commentParticipantSubsetRepository = commentParticipantSubsetRepository;
    this.emailNotificationService = emailNotificationService;
  }

  /**
   * コメント追加イベントを受信して通知を作成します。
   *
   * @param event コメント追加イベント
   */
  public void onCommentAdded(BacklogItemCommentAdded event) {
    // TODO: 実装
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * コメント削除イベントを受信して通知を作成します。
   *
   * @param event コメント削除イベント
   */
  public void onCommentDeleted(BacklogItemCommentDeleted event) {
    // TODO: 実装
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * 通知タイプを取得します。
   *
   * @param isAdded 追加イベントかどうか
   * @return 通知タイプ
   */
  public NotificationType getNotificationType(boolean isAdded) {
    return isAdded ? NotificationType.COMMENT_ADDED : NotificationType.COMMENT_DELETED;
  }
}
