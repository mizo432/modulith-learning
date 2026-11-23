package undecided.generic.documentMgmt.internal;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import undecided.generic.documentMgmt.spi.Document;
import undecided.supporting.snowflake.SnowflakeId;

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
    SnowflakeId id = SnowflakeId.of(Long.parseLong(documentId));
    Document document =
        documentQueryService
            .findById(id)
            .orElseThrow(() -> new DocumentNotFoundException("帳票が存在しません: " + id));
    return ResponseEntity.ok(DocumentResponse.from(document));
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
    DocumentDownloadResult result =
        documentDownloadService.download(SnowflakeId.of(Long.parseLong(documentId)));
    StreamingResponseBody body =
        out -> {
          try (InputStream in = result.content()) {
            in.transferTo(out);
          }
        };
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(result.contentType()))
        .contentLength(result.fileSize())
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment()
                .filename(result.fileName(), StandardCharsets.UTF_8)
                .build()
                .toString())
        .body(body);
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
      @RequestParam("fileName") String fileName) {
    return documentQueryService.findVersionsByFileName(fileName).stream()
        .map(DocumentResponse::from)
        .toList();
  }
}
