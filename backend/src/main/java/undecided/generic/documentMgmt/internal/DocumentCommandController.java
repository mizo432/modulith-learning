package undecided.generic.documentMgmt.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    throw new UnsupportedOperationException("not implemented");
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
    throw new UnsupportedOperationException("not implemented");
  }
}
