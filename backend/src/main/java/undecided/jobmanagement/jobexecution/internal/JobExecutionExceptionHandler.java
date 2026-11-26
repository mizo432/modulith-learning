package undecided.jobmanagement.jobexecution.internal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** ジョブ管理の例外を HTTP ステータスへ変換するハンドラ。エラーレスポンスは RFC 9457（Problem Details）形式で返す。 */
@RestControllerAdvice
class JobExecutionExceptionHandler {

  @ExceptionHandler(JobPermissionDeniedException.class)
  ProblemDetail handlePermissionDenied(JobPermissionDeniedException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
  }

  @ExceptionHandler(JobDefinitionNotFoundException.class)
  ProblemDetail handleDefinitionNotFound(JobDefinitionNotFoundException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(JobLaunchConflictException.class)
  ProblemDetail handleLaunchConflict(JobLaunchConflictException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
  }

  @ExceptionHandler(JobParameterFormatException.class)
  ProblemDetail handleParameterFormat(JobParameterFormatException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
  }
}
