package undecided.agiloom.requirementBacklog.internal;

import java.util.Map;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * バックログアイテムAPIの例外をHTTPステータスへ変換するハンドラー。
 *
 * <ul>
 *   <li>権限なし（SM／PO以外による状態変更、コメントした本人・ADMIN以外によるコメント削除）: 403
 *   <li>不正な状態遷移・返信ありコメントの削除: 409
 *   <li>楽観ロック失敗（同時更新の検知）: 409
 *   <li>入力不正・対象が存在しない: 400
 * </ul>
 */
@RestControllerAdvice(
    assignableTypes = {BacklogItemCommandController.class, BacklogItemQueryController.class})
class BacklogItemExceptionHandler {

  @ExceptionHandler(BacklogItemPermissionDeniedException.class)
  ResponseEntity<Map<String, String>> handlePermissionDenied(
      BacklogItemPermissionDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  ResponseEntity<Map<String, String>> handleOptimisticLockingFailure(
      OptimisticLockingFailureException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
  }
}
