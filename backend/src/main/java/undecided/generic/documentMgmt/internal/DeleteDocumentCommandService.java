package undecided.generic.documentMgmt.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 帳票論理削除のコマンドサービス。
 *
 * <p>削除権限はアップロード者のみ。論理削除では MinIO のオブジェクトは残す。
 * 既に論理削除済みの帳票を再度削除しようとした場合はエラーとなる。
 */
@Service
@Transactional
class DeleteDocumentCommandService {

  private final DocumentRepository documentRepository;

  DeleteDocumentCommandService(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
  }

  /**
   * 帳票を論理削除する。
   *
   * @param command 論理削除コマンド
   * @throws IllegalArgumentException 帳票が見つからない場合
   * @throws DocumentPermissionDeniedException アップロード者以外が削除しようとした場合
   * @throws IllegalStateException 既に論理削除済みの場合
   */
  void execute(@NonNull DeleteDocumentCommand command) {
    throw new UnsupportedOperationException("not implemented");
  }
}
