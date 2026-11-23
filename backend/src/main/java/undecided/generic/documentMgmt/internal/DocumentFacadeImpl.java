package undecided.generic.documentMgmt.internal;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentContent;
import undecided.generic.documentMgmt.spi.DocumentFacade;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 帳票管理モジュールの公開ファサードの実装。
 *
 * <p>internal の各サービスへ委譲します。
 */
@Service
class DocumentFacadeImpl implements DocumentFacade {

  private final UploadDocumentCommandService uploadDocumentCommandService;
  private final DeleteDocumentCommandService deleteDocumentCommandService;
  private final DocumentDownloadService documentDownloadService;
  private final DocumentQueryService documentQueryService;
  private final DocumentRepository documentRepository;

  DocumentFacadeImpl(
      UploadDocumentCommandService uploadDocumentCommandService,
      DeleteDocumentCommandService deleteDocumentCommandService,
      DocumentDownloadService documentDownloadService,
      DocumentQueryService documentQueryService,
      DocumentRepository documentRepository) {
    this.uploadDocumentCommandService = uploadDocumentCommandService;
    this.deleteDocumentCommandService = deleteDocumentCommandService;
    this.documentDownloadService = documentDownloadService;
    this.documentQueryService = documentQueryService;
    this.documentRepository = documentRepository;
  }

  @Override
  public @NonNull SnowflakeId upload(
      @NonNull String fileName,
      @NonNull String contentType,
      long fileSize,
      @NonNull InputStream content,
      @Nullable SnowflakeId uploaderEmployeeId,
      @Nullable String uploaderName,
      @Nullable SnowflakeId seriesId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public @NonNull Optional<Document> findById(@NonNull SnowflakeId documentId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public @NonNull List<Document> findBySeriesId(@NonNull SnowflakeId seriesId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public @NonNull DocumentContent download(@NonNull SnowflakeId documentId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void delete(@NonNull SnowflakeId documentId, @Nullable SnowflakeId actorEmployeeId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
