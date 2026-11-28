package undecided.notification.internal;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentDeleted;
import undecided.notification.spi.NotificationEntity;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * コメントイベントのリスナー。
 *
 * <p>requirementBacklog領域から発行されるコメント追加・削除イベントを受信し、
 * コメント参加者（操作者除く）に対して通知レコードを作成し、メールを送信します。
 */
@Component
public class BacklogItemCommentEventListener {

  private final NotificationEntityRepository notificationRepository;
  private final NotificationCommentParticipantSubsetRepository commentParticipantSubsetRepository;
  private final EmailNotificationService emailNotificationService;
  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final Clock clock;

  public BacklogItemCommentEventListener(
      NotificationEntityRepository notificationRepository,
      NotificationCommentParticipantSubsetRepository commentParticipantSubsetRepository,
      EmailNotificationService emailNotificationService,
      SnowflakeIdGenerator snowflakeIdGenerator,
      Clock clock) {
    this.notificationRepository = notificationRepository;
    this.commentParticipantSubsetRepository = commentParticipantSubsetRepository;
    this.emailNotificationService = emailNotificationService;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
    this.clock = clock;
  }

  /**
   * コメント追加イベントを受信して通知を作成します。
   *
   * @param event コメント追加イベント
   */
  @ApplicationModuleListener
  public void onCommentAdded(BacklogItemCommentAdded event) {
    NotificationType type = NotificationType.COMMENT_ADDED;
    createNotificationsForParticipants(
        event.backlogItemId(),
        event.commentId(),
        event.authorEmployeeId(),
        event.authorName(),
        type);
  }

  /**
   * コメント削除イベントを受信して通知を作成します。
   *
   * @param event コメント削除イベント
   */
  @ApplicationModuleListener
  public void onCommentDeleted(BacklogItemCommentDeleted event) {
    NotificationType type = NotificationType.COMMENT_DELETED;
    createNotificationsForParticipants(
        event.backlogItemId(),
        event.commentId(),
        event.actorEmployeeId(),
        null,
        type);
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

  /**
   * コメント参加者（操作者除く）に対して通知レコードを作成し、メールを送信します。
   *
   * @param backlogItemId バックログアイテムID
   * @param commentId コメントID
   * @param actorEmployeeId 操作者ID（通知から除外）
   * @param authorName コメント投稿者名（nullの場合「コメント」を使用）
   * @param type 通知タイプ
   */
  private void createNotificationsForParticipants(
      SnowflakeId backlogItemId,
      SnowflakeId commentId,
      SnowflakeId actorEmployeeId,
      String authorName,
      NotificationType type) {

    Set<NotificationCommentParticipantSubset> participants =
        commentParticipantSubsetRepository.findByBacklogItemId(backlogItemId);

    for (NotificationCommentParticipantSubset participant : participants) {
      // 操作者自身には通知しない
      if (participant.getEmployeeId().equals(actorEmployeeId)) {
        continue;
      }

      String title = buildTitle(type);
      String content = buildContent(type, authorName);

      NotificationEntity notification =
          NotificationEntity.builder()
              .id(snowflakeIdGenerator.nextIdValue())
              .recipientId(participant.getEmployeeId())
              .backlogItemId(backlogItemId)
              .commentId(commentId)
              .title(title)
              .content(content)
              .type(type)
              .createdAt(LocalDateTime.now(clock))
              .updatedAt(LocalDateTime.now(clock))
              .build();

      notificationRepository.save(notification);

      // メール送信は非同期で、失敗しても通知レコードは残す
      String employeeEmail = participant.getEmployeeEmail();
      if (employeeEmail != null && !employeeEmail.isEmpty()) {
        emailNotificationService.sendAsync(employeeEmail, title, content);
      }
    }
  }

  private String buildTitle(NotificationType type) {
    return type == NotificationType.COMMENT_ADDED
        ? "コメントが追加されました"
        : "コメントが削除されました";
  }

  private String buildContent(NotificationType type, String authorName) {
    String action = type == NotificationType.COMMENT_ADDED ? "追加" : "削除";
    if (authorName != null && !authorName.isEmpty()) {
      return authorName + "がコメントを" + action + "しました";
    }
    return "コメントが" + action + "されました";
  }
}
