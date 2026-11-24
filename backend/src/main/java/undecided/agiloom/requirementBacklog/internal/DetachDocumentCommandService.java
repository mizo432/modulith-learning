package undecided.agiloom.requirementBacklog.internal;

import undecided.agiloom.requirementBacklog.spi.BacklogItemDocument;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.generic.documentMgmt.spi.Document;
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
    BacklogItemDocument linkage =
        backlogItemDocumentRepository
            .findByBacklogItemIdAndDocumentId(command.getBacklogItemId(), command.getDocumentId())
            .orElseThrow(
                () -> new IllegalArgumentException("添付が存在しません: " + command.getDocumentId()));
    Document document =
        documentFacade
            .findById(command.getDocumentId())
            .orElseThrow(
                () -> new IllegalArgumentException("帳票が存在しません: " + command.getDocumentId()));
    boolean isUploader =
        document.getUploaderEmployeeId() != null
            && document.getUploaderEmployeeId().equals(command.getActorEmployeeId());
    if (!isUploader) {
      throw new BacklogItemPermissionDeniedException("ファイルを削除できるのはアップロードした本人のみです");
    }
    documentFacade.delete(command.getDocumentId(), command.getActorEmployeeId());
    backlogItemDocumentRepository.delete(linkage);
  }
}
