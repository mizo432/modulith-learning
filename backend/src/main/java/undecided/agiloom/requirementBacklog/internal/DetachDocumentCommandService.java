package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.generic.documentMgmt.spi.DocumentFacade;

/**
 * バックログアイテムからファイルの添付を解除するコマンドサービス。
 *
 * <p>帳票管理モジュール側で論理削除し（削除できるのはアップロード者のみ）、
 * 自領域の関連付けを削除します。
 */
@Service
@Transactional
class DetachDocumentCommandService {

  private final BacklogItemDocumentRepository backlogItemDocumentRepository;
  private final DocumentFacade documentFacade;

  DetachDocumentCommandService(
      BacklogItemDocumentRepository backlogItemDocumentRepository,
      DocumentFacade documentFacade) {
    this.backlogItemDocumentRepository = backlogItemDocumentRepository;
    this.documentFacade = documentFacade;
  }

  /**
   * ファイルの添付を解除します。
   *
   * @param command 添付解除コマンド
   */
  void execute(@NonNull DetachDocumentCommand command) {
    throw new UnsupportedOperationException("not implemented");
  }
}
