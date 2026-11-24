package undecided.generic.documentMgmt.internal;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.generic.documentMgmt.spi.Document;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 帳票メタデータのクエリサービス。
 *
 * <p>帳票メタデータの一覧・単体取得を提供する。
 */
@Service
@Transactional(readOnly = true)
class DocumentQueryService {

  private final DocumentRepository documentRepository;

  DocumentQueryService(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
  }

  /**
   * 帳票を ID で取得する（論理削除済みを含まない）。
   *
   * @param documentId 帳票 ID
   * @return 帳票
   */
  Optional<Document> findById(@NonNull SnowflakeId documentId) {
    return documentRepository.findByIdAndDeletedFalse(documentId);
  }

  /**
   * ファイル名で帳票の一覧を取得する（全バージョン、論理削除済みを含む）。
   *
   * @param fileName ファイル名
   * @return バージョン一覧
   */
  List<Document> findVersionsByFileName(@NonNull String fileName) {
    return documentRepository.findByFileName(fileName);
  }
}
