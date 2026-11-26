package undecided.jobmanagement.jobexecution.internal;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** ジョブ管理の例外を HTTP ステータスへ変換するハンドラ。 */
@RestControllerAdvice
class JobExecutionExceptionHandler {

  @ExceptionHandler(JobPermissionDeniedException.class)
  ResponseEntity<Map<String, String>> handlePermissionDenied(JobPermissionDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(JobDefinitionNotFoundException.class)
  ResponseEntity<Map<String, String>> handleDefinitionNotFound(JobDefinitionNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(JobLaunchConflictException.class)
  ResponseEntity<Map<String, String>> handleLaunchConflict(JobLaunchConflictException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(JobParameterFormatException.class)
  ResponseEntity<Map<String, String>> handleParameterFormat(JobParameterFormatException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
  }
}
