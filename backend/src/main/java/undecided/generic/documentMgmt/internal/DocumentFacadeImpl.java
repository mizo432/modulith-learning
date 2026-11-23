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
    return uploadDocumentCommandService.execute(
        UploadDocumentCommand.builder()
            .fileName(fileName)
            .contentType(contentType)
            .fileSize(fileSize)
            .content(content)
            .uploaderEmployeeId(uploaderEmployeeId)
            .uploaderName(uploaderName)
            .seriesId(seriesId)
            .build());
  }

  @Override
  public @NonNull Optional<Document> findById(@NonNull SnowflakeId documentId) {
    return documentQueryService.findById(documentId);
  }

  @Override
  public @NonNull List<Document> findBySeriesId(@NonNull SnowflakeId seriesId) {
    return documentRepository.findBySeriesId(seriesId);
  }

  @Override
  public @NonNull DocumentContent download(@NonNull SnowflakeId documentId) {
    DocumentDownloadResult result = documentDownloadService.download(documentId);
    return new DocumentContent(
        result.content(), result.contentType(), result.fileName(), result.fileSize());
  }

  @Override
  public void delete(@NonNull SnowflakeId documentId, @Nullable SnowflakeId actorEmployeeId) {
    deleteDocumentCommandService.execute(
        DeleteDocumentCommand.builder()
            .documentId(documentId)
            .actorEmployeeId(actorEmployeeId)
            .build());
  }
}
