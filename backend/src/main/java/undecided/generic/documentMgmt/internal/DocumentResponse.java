package undecided.generic.documentMgmt.internal;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.supporting.snowflake.SnowflakeId;

/** 帳票 API のレスポンス DTO。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DocumentResponse {

  /** 帳票 ID（文字列形式。JavaScript の Number 精度対策）。 */
  private String id;

  /** ファイル名。 */
  private String fileName;

  /** Content-Type（MIME タイプ）。 */
  private String contentType;

  /** ファイルサイズ（バイト数）。 */
  private long fileSize;

  /** ファイル種別。 */
  private DocumentFileType fileType;

  /** バージョン番号。 */
  private int version;

  /** アップローダーの従業員 ID（文字列形式）。 */
  private String uploaderEmployeeId;

  /** アップローダー名。 */
  private String uploaderName;

  /** 論理削除フラグ。 */
  private boolean deleted;

  /** 論理削除日時。 */
  private LocalDateTime deletedAt;

  /** 作成日時。 */
  private LocalDateTime createdAt;

  /** 更新日時。 */
  private LocalDateTime updatedAt;

  /** {@link undecided.generic.documentMgmt.spi.Document} から DocumentResponse に変換する。 */
  static DocumentResponse from(undecided.generic.documentMgmt.spi.Document document) {
    return DocumentResponse.builder()
        .id(document.getId() != null ? String.valueOf(document.getId().value()) : null)
        .fileName(document.getFileName())
        .contentType(document.getContentType())
        .fileSize(document.getFileSize())
        .fileType(document.getFileType())
        .version(document.getVersion())
        .uploaderEmployeeId(
            document.getUploaderEmployeeId() != null
                ? String.valueOf(document.getUploaderEmployeeId().value())
                : null)
        .uploaderName(document.getUploaderName())
        .deleted(document.isDeleted())
        .deletedAt(document.getDeletedAt())
        .createdAt(document.getCreatedAt())
        .updatedAt(document.getUpdatedAt())
        .build();
  }
}
