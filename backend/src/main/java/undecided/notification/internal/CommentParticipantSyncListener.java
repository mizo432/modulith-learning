package undecided.notification.internal;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * コメント参加者の同期リスナー。
 *
 * <p>requirementBacklog領域から発行されるコメント追加イベントを受信し、
 * コメント参加者情報をNTスキーマのcomment_participantsテーブルに記録する。
 *
 * <p>BacklogItemCommentEventListenerより先に実行されるように最優先度を設定する。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CommentParticipantSyncListener {

  private final CommentParticipantSubsetRepository commentParticipantSubsetRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  CommentParticipantSyncListener(
      CommentParticipantSubsetRepository commentParticipantSubsetRepository,
      SnowflakeIdGenerator snowflakeIdGenerator) {
    this.commentParticipantSubsetRepository = commentParticipantSubsetRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  /**
   * コメント追加イベントを受信して、参加者情報をNTスキーマに同期する。
   *
   * @param event コメント追加イベント
   */
  @ApplicationModuleListener
  public void onCommentAdded(BacklogItemCommentAdded event) {
    // 操作者（コメント投稿者）の参加者情報を記録
    if (event.authorEmployeeId() != null) {
      String employeeEmail = event.authorEmail() != null ? event.authorEmail() : "";
      commentParticipantSubsetRepository.save(
          CommentParticipantSubset.builder()
              .id(snowflakeIdGenerator.nextIdValue())
              .backlogItemId(event.backlogItemId())
              .employeeId(event.authorEmployeeId())
              .employeeName(event.authorName() != null ? event.authorName() : "")
              .employeeEmail(employeeEmail)
              .build());
    }
  }
}
