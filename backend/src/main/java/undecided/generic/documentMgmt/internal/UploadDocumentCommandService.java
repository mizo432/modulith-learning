package undecided.generic.documentMgmt.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 帳票アップロードのコマンドサービス。
 *
 * <p>ファイル検証 → MinIO アップロード → DB メタデータ保存 → バージョン管理の順に処理する。
 * 同名ファイルの再アップロードは新バージョンとして保存する。
 */
@Service
@Transactional
class UploadDocumentCommandService {

  private final DocumentFileValidator documentFileValidator;
  private final undecided.generic.documentMgmt.spi.DocumentStorageService documentStorageService;
  private final DocumentRepository documentRepository;
  private final undecided.supporting.snowflake.SnowflakeIdGenerator snowflakeIdGenerator;

  UploadDocumentCommandService(
      DocumentFileValidator documentFileValidator,
      undecided.generic.documentMgmt.spi.DocumentStorageService documentStorageService,
      DocumentRepository documentRepository,
      undecided.supporting.snowflake.SnowflakeIdGenerator snowflakeIdGenerator) {
    this.documentFileValidator = documentFileValidator;
    this.documentStorageService = documentStorageService;
    this.documentRepository = documentRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  /**
   * 帳票をアップロードする。
   *
   * @param command アップロードコマンド
   * @return アップロードされた帳票の ID
   * @throws InvalidDocumentFileException ファイル検証に失敗した場合
   */
  @NonNull
  SnowflakeId execute(@NonNull UploadDocumentCommand command) {
    throw new UnsupportedOperationException("not implemented");
  }
}
