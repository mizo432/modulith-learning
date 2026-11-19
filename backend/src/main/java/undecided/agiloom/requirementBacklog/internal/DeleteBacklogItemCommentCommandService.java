package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentDeleted;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムのコメントを削除するコマンドサービス。 */
@Service
@Transactional
class DeleteBacklogItemCommentCommandService {

  private final BacklogItemCommentRepository backlogItemCommentRepository;
  private final AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;
  private final ApplicationEventPublisher eventPublisher;

  DeleteBacklogItemCommentCommandService(
      BacklogItemCommentRepository backlogItemCommentRepository,
      AdminEmployeeSubsetRepository adminEmployeeSubsetRepository,
      ApplicationEventPublisher eventPublisher) {
    this.backlogItemCommentRepository = backlogItemCommentRepository;
    this.adminEmployeeSubsetRepository = adminEmployeeSubsetRepository;
    this.eventPublisher = eventPublisher;
  }

  /**
   * コメントを削除します。
   *
   * <p>削除できるのはコメントした本人またはADMINロール保持者のみです。
   *
   * @param command 削除コマンド
   * @throws IllegalArgumentException コメントが存在しない場合、またはactorEmployeeIdがnullの場合
   * @throws BacklogItemPermissionDeniedException 削除権限がない場合
   */
  void execute(@NonNull DeleteBacklogItemCommentCommand command) {
    SnowflakeId actorEmployeeId = command.getActorEmployeeId();
    if (actorEmployeeId == null) {
      throw new IllegalArgumentException("操作者ID（actorEmployeeId）はnullであってはいけません");
    }

    BacklogItemComment comment =
        backlogItemCommentRepository
            .findById(command.getCommentId())
            .orElseThrow(
                () -> new IllegalArgumentException("コメントが存在しません: " + command.getCommentId()));

    // コメントがコマンドのbacklogItemIdに属することを検証する
    if (!comment.getBacklogItem().getId().equals(command.getBacklogItemId())) {
      throw new IllegalArgumentException(
          "コメント(" + command.getCommentId()
              + ")はバックログアイテム(" + command.getBacklogItemId() + ")に属していません");
    }

    boolean isAuthor = comment.getAuthorEmployeeId() != null
        && comment.getAuthorEmployeeId().equals(actorEmployeeId);
    boolean isAdmin = adminEmployeeSubsetRepository.existsByEmployeeId(actorEmployeeId);

    if (!isAuthor && !isAdmin) {
      throw new BacklogItemPermissionDeniedException(
          "コメントを削除する権限がありません。削除できるのはコメントした本人またはシステム管理者のみです");
    }

    // 子返信があるかチェック
    List<BacklogItemComment> replies =
        backlogItemCommentRepository.findByParentCommentId(command.getCommentId());
    if (!replies.isEmpty()) {
      throw new IllegalStateException("子返信があるコメントは削除できません");
    }

    backlogItemCommentRepository.delete(comment);

    // コメント削除イベントを発行
    eventPublisher.publishEvent(
        new BacklogItemCommentDeleted(
            command.getBacklogItemId(),
            command.getCommentId(),
            actorEmployeeId,
            LocalDateTime.now()));
  }
}
