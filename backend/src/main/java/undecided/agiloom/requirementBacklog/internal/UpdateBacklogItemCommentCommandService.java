package undecided.agiloom.requirementBacklog.internal;

import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムのコメントを編集するコマンドサービス。 */
@Service
@Transactional
class UpdateBacklogItemCommentCommandService {

  private final BacklogItemCommentRepository backlogItemCommentRepository;
  private final AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  UpdateBacklogItemCommentCommandService(
      BacklogItemCommentRepository backlogItemCommentRepository,
      AdminEmployeeSubsetRepository adminEmployeeSubsetRepository) {
    this.backlogItemCommentRepository = backlogItemCommentRepository;
    this.adminEmployeeSubsetRepository = adminEmployeeSubsetRepository;
  }

  /**
   * コメントを編集します。
   *
   * <p>編集できるのはコメントした本人またはADMINロール保持者のみです。
   * 子返信があるコメントは編集できません。
   *
   * @param command 編集コマンド
   * @throws IllegalArgumentException コメントが存在しない場合、actorEmployeeIdがnullの場合、本文が空の場合
   * @throws IllegalStateException 子返信がある場合
   * @throws BacklogItemPermissionDeniedException 編集権限がない場合
   */
  void execute(@NonNull UpdateBacklogItemCommentCommand command) {
    SnowflakeId actorEmployeeId = command.getActorEmployeeId();
    if (actorEmployeeId == null) {
      throw new IllegalArgumentException("操作者ID（actorEmployeeId）はnullであってはいけません");
    }

    checkNonEmpty(command.getBody(), () -> new IllegalArgumentException("Comment body must not be empty"));

    BacklogItemComment comment =
        backlogItemCommentRepository
            .findById(command.getCommentId())
            .orElseThrow(
                () -> new IllegalArgumentException("コメントが存在しません: " + command.getCommentId()));

    // 子返信があるかチェック
    List<BacklogItemComment> replies = backlogItemCommentRepository.findByParentCommentId(command.getCommentId());
    if (!replies.isEmpty()) {
      throw new IllegalStateException("子返信があるコメントは編集できません");
    }

    // 権限チェック（本人またはADMINのみ）
    boolean isAuthor = comment.getAuthorEmployeeId() != null
        && comment.getAuthorEmployeeId().equals(actorEmployeeId);
    boolean isAdmin = adminEmployeeSubsetRepository.existsByEmployeeId(actorEmployeeId);

    if (!isAuthor && !isAdmin) {
      throw new BacklogItemPermissionDeniedException(
          "コメントを編集する権限がありません。編集できるのはコメントした本人またはシステム管理者のみです");
    }

    comment.updateBody(command.getBody());
    backlogItemCommentRepository.save(comment);
  }
}
