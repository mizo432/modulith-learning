package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムのコメントを削除するコマンドサービス。 */
@Service
@Transactional
class DeleteBacklogItemCommentCommandService {

  private final BacklogItemCommentRepository backlogItemCommentRepository;
  private final AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  DeleteBacklogItemCommentCommandService(
      BacklogItemCommentRepository backlogItemCommentRepository,
      AdminEmployeeSubsetRepository adminEmployeeSubsetRepository) {
    this.backlogItemCommentRepository = backlogItemCommentRepository;
    this.adminEmployeeSubsetRepository = adminEmployeeSubsetRepository;
  }

  /**
   * コメントを削除します。
   *
   * <p>削除できるのはコメントした本人またはADMINロール保持者のみです。
   *
   * <p>返信チェックは本実装では行いません。スレッド返信機能（#510）は未実装であり、
   * 子返信があるコメントを削除できないルールは、将来親参照（parent_comment_id）が導入された際に
   *有効化する前提です。
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

    // 返信チェックは実装しない（スレッド返信未実装のため）。
    // 将来 parent_comment_id 列が導入された時点で、子コメントが存在する場合は削除不可とする。

    boolean isAuthor = comment.getAuthorEmployeeId() != null
        && comment.getAuthorEmployeeId().equals(actorEmployeeId);
    boolean isAdmin = adminEmployeeSubsetRepository.existsByEmployeeId(actorEmployeeId);

    if (!isAuthor && !isAdmin) {
      throw new BacklogItemPermissionDeniedException(
          "コメントを削除する権限がありません。削除できるのはコメントした本人またはシステム管理者のみです");
    }

    backlogItemCommentRepository.delete(comment);
  }
}
