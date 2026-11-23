package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.generic.documentMgmt.spi.Document;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムの添付ファイル1件を表すレスポンス DTO。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BacklogItemAttachmentResponse {
  private SnowflakeId documentId;
  private SnowflakeId seriesId;
  private String fileName;
  private String contentType;
  private long fileSize;
  private int version;
  private SnowflakeId uploaderEmployeeId;
  private String uploaderName;
  private LocalDateTime createdAt;

  /** 帳票と系列 ID からレスポンスを生成します。 */
  static BacklogItemAttachmentResponse from(Document document, SnowflakeId seriesId) {
    return BacklogItemAttachmentResponse.builder()
        .documentId(document.getId())
        .seriesId(seriesId)
        .fileName(document.getFileName())
        .contentType(document.getContentType())
        .fileSize(document.getFileSize())
        .version(document.getVersion())
        .uploaderEmployeeId(document.getUploaderEmployeeId())
        .uploaderName(document.getUploaderName())
        .createdAt(document.getCreatedAt())
        .build();
  }
}
