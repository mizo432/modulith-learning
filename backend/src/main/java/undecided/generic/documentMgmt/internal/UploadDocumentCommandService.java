package undecided.generic.documentMgmt.internal;
import java.time.Clock;

import java.time.LocalDateTime;
import java.util.OptionalInt;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.generic.documentMgmt.spi.DocumentVersion;
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
  private final Clock clock;

  UploadDocumentCommandService(
      DocumentFileValidator documentFileValidator,
      undecided.generic.documentMgmt.spi.DocumentStorageService documentStorageService,
      DocumentRepository documentRepository,
      undecided.supporting.snowflake.SnowflakeIdGenerator snowflakeIdGenerator,
      Clock clock) {
    this.documentFileValidator = documentFileValidator;
    this.documentStorageService = documentStorageService;
    this.documentRepository = documentRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
    this.clock = clock;
  }

  /**
   * 帳票をアップロードする。
   *
   * @param command アップロードコマンド
   * @return アップロードされた帳票の ID
   * @throws InvalidDocumentFileException ファイル検証に失敗した場合
   * @throws IllegalArgumentException アップローダー名が上限の長さを超える場合
   */
  @NonNull
  SnowflakeId execute(@NonNull UploadDocumentCommand command) {
    DocumentFileType fileType =
        documentFileValidator.validate(
            command.getFileName(), command.getContentType(), command.getFileSize());

    String uploaderName = command.getUploaderName();
    if (uploaderName != null && uploaderName.length() > Document.UPLOADER_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException(
          "アップローダー名は" + Document.UPLOADER_NAME_MAX_LENGTH + "文字以下にしてください: "
              + uploaderName.length());
    }

    DocumentVersion version =
        command.getSeriesId() != null
            ? nextVersionOfSeries(command.getSeriesId())
            : nextVersionOf(command.getFileName());
    SnowflakeId id = snowflakeIdGenerator.nextIdValue();
    String s3Key =
        documentStorageService.upload(
            id,
            version,
            command.getFileName(),
            command.getContentType(),
            command.getContent(),
            command.getFileSize());

    LocalDateTime now = LocalDateTime.now(clock);
    documentRepository.save(
        Document.builder()
            .id(id)
            .fileName(command.getFileName())
            .contentType(command.getContentType())
            .fileSize(command.getFileSize())
            .fileType(fileType)
            .version(version.value())
            .s3Key(s3Key)
            .seriesId(command.getSeriesId())
            .uploaderEmployeeId(command.getUploaderEmployeeId())
            .uploaderName(uploaderName)
            .deleted(false)
            .createdAt(now)
            .updatedAt(now)
            .build());
    return id;
  }

  /** 同じ系列の既存帳票（論理削除済みを含む）の最大バージョンの次を返す。初回は初期バージョン。 */
  private DocumentVersion nextVersionOfSeries(SnowflakeId seriesId) {
    OptionalInt latest =
        documentRepository.findBySeriesId(seriesId).stream().mapToInt(Document::getVersion).max();
    return latest.isPresent()
        ? new DocumentVersion(latest.getAsInt()).increment()
        : DocumentVersion.initial();
  }

  /** 同じファイル名の既存帳票（論理削除済みを含む）の最大バージョンの次を返す。初回は初期バージョン。 */
  private DocumentVersion nextVersionOf(String fileName) {
    OptionalInt latest =
        documentRepository.findByFileName(fileName).stream().mapToInt(Document::getVersion).max();
    return latest.isPresent()
        ? new DocumentVersion(latest.getAsInt()).increment()
        : DocumentVersion.initial();
  }
}
