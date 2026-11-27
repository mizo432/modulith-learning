package undecided.jobmanagement.jobexecution.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * JobExecutionExceptionHandlerの単体テスト。
 *
 * <p>Spring Bootコンテキストに依存せず、ハンドラーインスタンスを直接呼び出して検証する。
 * エラーレスポンスは RFC 9457（Problem Details）に統一されていることを確認する。
 */
@Tag("small")
@DisplayName("JobExecutionExceptionHandlerの単体テスト")
class JobExecutionExceptionHandlerTest {

  private final JobExecutionExceptionHandler handler = new JobExecutionExceptionHandler();

  @Test
  @DisplayName("JobPermissionDeniedExceptionをHTTP 403のProblemDetailに変換すること")
  void shouldReturn403ProblemDetailForPermissionDenied() {
    String message = "Permission denied";
    ProblemDetail problem = handler.handlePermissionDenied(new JobPermissionDeniedException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("JobDefinitionNotFoundExceptionをHTTP 404のProblemDetailに変換すること")
  void shouldReturn404ProblemDetailForDefinitionNotFound() {
    String message = "Job definition not found";
    ProblemDetail problem =
        handler.handleDefinitionNotFound(new JobDefinitionNotFoundException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("JobLaunchConflictExceptionをHTTP 409のProblemDetailに変換すること")
  void shouldReturn409ProblemDetailForLaunchConflict() {
    String message = "Too many concurrent executions";
    ProblemDetail problem = handler.handleLaunchConflict(new JobLaunchConflictException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("JobParameterFormatExceptionをHTTP 400のProblemDetailに変換すること")
  void shouldReturn400ProblemDetailForParameterFormat() {
    String message = "Invalid parameter format";
    ProblemDetail problem = handler.handleParameterFormat(new JobParameterFormatException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }
}
