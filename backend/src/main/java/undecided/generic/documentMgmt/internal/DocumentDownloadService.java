package undecided.generic.documentMgmt.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 帳票ダウンロードのサービス。
 *
 * <p>DB から帳票メタデータを取得し、MinIO からファイル実体をストリーミングダウンロードする。
 * 論理削除済みの帳票はダウンロード不可。
 */
@Service
@Transactional(readOnly = true)
class DocumentDownloadService {

  private final DocumentRepository documentRepository;
  private final undecided.generic.documentMgmt.spi.DocumentStorageService documentStorageService;

  DocumentDownloadService(
      DocumentRepository documentRepository,
      undecided.generic.documentMgmt.spi.DocumentStorageService documentStorageService) {
    this.documentRepository = documentRepository;
    this.documentStorageService = documentStorageService;
  }

  /**
   * 帳票をダウンロードする。
   *
   * @param documentId 帳票 ID
   * @return ダウンロード結果（ファイル内容と Content-Type）
   * @throws DocumentNotFoundException 帳票が見つからない場合、または論理削除済みの場合
   */
  @NonNull
  DocumentDownloadResult download(@NonNull SnowflakeId documentId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
