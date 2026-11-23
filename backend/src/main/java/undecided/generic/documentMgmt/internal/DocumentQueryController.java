package undecided.generic.documentMgmt.internal;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/** 帳票クエリ（メタデータ取得/ダウンロード/バージョン一覧）の REST コントローラー。 */
@RestController
class DocumentQueryController {

  private final DocumentQueryService documentQueryService;
  private final DocumentDownloadService documentDownloadService;

  DocumentQueryController(
      DocumentQueryService documentQueryService,
      DocumentDownloadService documentDownloadService) {
    this.documentQueryService = documentQueryService;
    this.documentDownloadService = documentDownloadService;
  }

  /**
   * 帳票メタデータを取得する。
   *
   * <p>GET /api/v1/documents/{documentId}
   *
   * @param documentId 帳票 ID
   * @return 帳票メタデータ
   */
  @GetMapping("/api/v1/documents/{documentId}")
  ResponseEntity<DocumentResponse> getDocument(@PathVariable String documentId) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * 帳票をダウンロードする。
   *
   * <p>GET /api/v1/documents/{documentId}/download
   *
   * @param documentId 帳票 ID
   * @return ファイル内容（Content-Disposition ヘッダー付き）
   */
  @GetMapping("/api/v1/documents/{documentId}/download")
  ResponseEntity<StreamingResponseBody> downloadDocument(@PathVariable String documentId) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * ファイル名でバージョン一覧を取得する。
   *
   * <p>GET /api/v1/documents?fileName={fileName}
   *
   * @param fileName ファイル名
   * @return バージョン一覧
   */
  @GetMapping("/api/v1/documents")
  List<DocumentResponse> getVersions(
      @org.springframework.web.bind.annotation.RequestParam("fileName") String fileName) {
    throw new UnsupportedOperationException("not implemented");
  }
}
