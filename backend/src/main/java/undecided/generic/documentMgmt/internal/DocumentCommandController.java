package undecided.generic.documentMgmt.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import undecided.generic.documentMgmt.spi.Document;
import undecided.supporting.snowflake.SnowflakeId;

/** 帳票コマンド（アップロード/論理削除）の REST コントローラー。 */
@RestController
class DocumentCommandController {

  private final UploadDocumentCommandService uploadDocumentCommandService;
  private final DeleteDocumentCommandService deleteDocumentCommandService;
  private final DocumentQueryService documentQueryService;

  DocumentCommandController(
      UploadDocumentCommandService uploadDocumentCommandService,
      DeleteDocumentCommandService deleteDocumentCommandService,
      DocumentQueryService documentQueryService) {
    this.uploadDocumentCommandService = uploadDocumentCommandService;
    this.deleteDocumentCommandService = deleteDocumentCommandService;
    this.documentQueryService = documentQueryService;
  }

  /**
   * 帳票をアップロードする。
   *
   * <p>POST /api/v1/documents
   *
   * @param file アップロードするファイル
   * @param uploaderEmployeeId アップローダーの従業員 ID（文字列形式）
   * @param uploaderName アップローダー名
   * @return アップロードされた帳票のメタデータ
   */
  @PostMapping("/api/v1/documents")
  ResponseEntity<DocumentResponse> uploadDocument(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "uploaderEmployeeId", required = false) String uploaderEmployeeId,
      @RequestParam(value = "uploaderName", required = false) String uploaderName) {
    SnowflakeId documentId =
        uploadDocumentCommandService.execute(
            UploadDocumentCommand.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .content(inputStreamOf(file))
                .uploaderEmployeeId(
                    uploaderEmployeeId == null || uploaderEmployeeId.isEmpty()
                        ? null
                        : SnowflakeId.of(Long.parseLong(uploaderEmployeeId)))
                .uploaderName(uploaderName)
                .build());
    Document document =
        documentQueryService
            .findById(documentId)
            .orElseThrow(() -> new DocumentNotFoundException("帳票が存在しません: " + documentId));
    return ResponseEntity.ok(DocumentResponse.from(document));
  }

  /**
   * 帳票を論理削除する。
   *
   * <p>DELETE /api/v1/documents/{documentId}
   *
   * @param documentId 帳票 ID
   * @param actorEmployeeId 操作者の従業員 ID（文字列形式）
   * @return 204 No Content
   */
  @DeleteMapping("/api/v1/documents/{documentId}")
  ResponseEntity<Void> deleteDocument(
      @PathVariable String documentId,
      @RequestParam("actorEmployeeId") String actorEmployeeId) {
    deleteDocumentCommandService.execute(
        DeleteDocumentCommand.builder()
            .documentId(SnowflakeId.of(Long.parseLong(documentId)))
            .actorEmployeeId(SnowflakeId.of(Long.parseLong(actorEmployeeId)))
            .build());
    return ResponseEntity.noContent().build();
  }

  private static InputStream inputStreamOf(MultipartFile file) {
    try {
      return file.getInputStream();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
