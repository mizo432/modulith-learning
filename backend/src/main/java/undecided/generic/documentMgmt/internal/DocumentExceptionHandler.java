package undecided.generic.documentMgmt.internal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 帳票 API の例外を HTTP ステータスへ変換するハンドラー。
 *
 * <p>エラーレスポンスは RFC 9457（Problem Details）形式で返す。
 *
 * <ul>
 *   <li>帳票ファイル不正（種別・サイズ）: 400
 *   <li>不正な引数（ID の形式不正、削除対象の帳票が存在しないなど）: 400
 *   <li>帳票が見つからない: 404
 *   <li>削除権限なし: 403
 *   <li>既に論理削除済みの再削除: 409
 * </ul>
 */
@RestControllerAdvice(
    assignableTypes = {DocumentCommandController.class, DocumentQueryController.class})
class DocumentExceptionHandler {

  @ExceptionHandler(InvalidDocumentFileException.class)
  ProblemDetail handleInvalidDocumentFile(InvalidDocumentFileException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  ProblemDetail handleDocumentNotFound(DocumentNotFoundException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(DocumentPermissionDeniedException.class)
  ProblemDetail handlePermissionDenied(DocumentPermissionDeniedException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  ProblemDetail handleIllegalState(IllegalStateException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
  }
}
