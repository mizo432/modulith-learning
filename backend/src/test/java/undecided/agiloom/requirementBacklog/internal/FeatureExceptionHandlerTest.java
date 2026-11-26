package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * FeatureExceptionHandlerの単体テスト。
 *
 * <p>Spring Bootコンテキストに依存せず、ハンドラーインスタンスを直接呼び出して検証する。
 * エラーレスポンスは RFC 9457（Problem Details）に統一されていることを確認する。
 */
@Tag("small")
@DisplayName("FeatureExceptionHandlerの単体テスト")
class FeatureExceptionHandlerTest {

  private final FeatureExceptionHandler handler = new FeatureExceptionHandler();

  @Test
  @DisplayName("BacklogItemPermissionDeniedExceptionをHTTP 403のProblemDetailに変換すること")
  void shouldReturn403ProblemDetailForPermissionDenied() {
    String message = "Permission denied";
    ProblemDetail problem =
        handler.handlePermissionDenied(new BacklogItemPermissionDeniedException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("BacklogItemAlreadyAssignedToFeatureExceptionをHTTP 409のProblemDetailに変換すること")
  void shouldReturn409ProblemDetailForAlreadyAssignedToFeature() {
    String message = "Already assigned to another feature";
    ProblemDetail problem =
        handler.handleAlreadyAssignedToFeature(
            new BacklogItemAlreadyAssignedToFeatureException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("IllegalStateExceptionをHTTP 409のProblemDetailに変換すること")
  void shouldReturn409ProblemDetailForIllegalState() {
    String message = "Invalid status transition";
    ProblemDetail problem = handler.handleIllegalState(new IllegalStateException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("IllegalArgumentExceptionをHTTP 400のProblemDetailに変換すること")
  void shouldReturn400ProblemDetailForIllegalArgument() {
    String message = "Invalid argument";
    ProblemDetail problem = handler.handleIllegalArgument(new IllegalArgumentException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("OptimisticLockingFailureExceptionをHTTP 409のProblemDetailに変換すること")
  void shouldReturn409ProblemDetailForOptimisticLockingFailure() {
    String message = "Optimistic locking failure";
    ProblemDetail problem =
        handler.handleOptimisticLockingFailure(new OptimisticLockingFailureException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }
}
