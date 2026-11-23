package undecided.generic.documentMgmt.internal;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 帳票 API の例外を HTTP ステータスへ変換するハンドラー。
 *
 * <ul>
 *   <li>帳票ファイル不正（種別・サイズ）: 400
 *   <li>帳票が見つからない: 404
 *   <li>削除権限なし: 403
 *   <li>既に論理削除済みの再削除: 409
 * </ul>
 */
@RestControllerAdvice(
    assignableTypes = {DocumentCommandController.class, DocumentQueryController.class})
class DocumentExceptionHandler {

  @ExceptionHandler(InvalidDocumentFileException.class)
  ResponseEntity<Map<String, String>> handleInvalidDocumentFile(
      InvalidDocumentFileException ex) {
    return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  ResponseEntity<Map<String, String>> handleDocumentNotFound(DocumentNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(DocumentPermissionDeniedException.class)
  ResponseEntity<Map<String, String>> handlePermissionDenied(
      DocumentPermissionDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
  }
}
