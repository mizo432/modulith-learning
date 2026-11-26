package undecided.agiloom.requirementBacklog.internal;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * バックログアイテムAPIの例外をHTTPステータスへ変換するハンドラー。
 *
 * <p>エラーレスポンスは RFC 9457（Problem Details）形式で返す。
 *
 * <ul>
 *   <li>権限なし（SM／PO以外による状態変更、コメントした本人・ADMIN以外によるコメント削除・編集）: 403
 *   <li>不正な状態遷移・返信ありコメントの削除・編集: 409
 *   <li>楽観ロック失敗（同時更新の検知）: 409
 *   <li>入力不正・対象が存在しない: 400
 * </ul>
 */
@RestControllerAdvice(
    assignableTypes = {BacklogItemCommandController.class, BacklogItemQueryController.class})
class BacklogItemExceptionHandler {

  @ExceptionHandler(BacklogItemPermissionDeniedException.class)
  ProblemDetail handlePermissionDenied(BacklogItemPermissionDeniedException ex) {
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

  @ExceptionHandler(OptimisticLockingFailureException.class)
  ProblemDetail handleOptimisticLockingFailure(OptimisticLockingFailureException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }
}
