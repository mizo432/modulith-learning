package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
   * <p>未実装です。呼び出すと {@link UnsupportedOperationException} をスローします。
   *
   * @param command 編集コマンド
   * @throws UnsupportedOperationException 常にスローされる
   */
  void execute(@NonNull UpdateBacklogItemCommentCommand command) {
    throw new UnsupportedOperationException("コメントの編集はまだ実装されていません");
  }
}
