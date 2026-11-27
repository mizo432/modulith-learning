package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * BacklogItemExceptionHandlerの単体テスト。
 *
 * <p>Spring Bootコンテキストに依存せず、ハンドラーインスタンスを直接呼び出して検証する。
 * エラーレスポンスは RFC 9457（Problem Details）に統一されていることを確認する。
 */
@Tag("small")
@DisplayName("BacklogItemExceptionHandlerの単体テスト")
class BacklogItemExceptionHandlerTest {

  private final BacklogItemExceptionHandler handler = new BacklogItemExceptionHandler();

  @Nested
  @DisplayName("handlePermissionDeniedのテスト")
  class HandlePermissionDeniedTest {

    @Test
    @DisplayName("BacklogItemPermissionDeniedExceptionをHTTP 403のProblemDetailに変換すること")
    void shouldReturn403ProblemDetailForPermissionDenied() {
      // Arrange
      String message = "Permission denied";
      BacklogItemPermissionDeniedException ex = new BacklogItemPermissionDeniedException(message);

      // Act
      ProblemDetail problem = handler.handlePermissionDenied(ex);

      // Assert
      assertThat(problem.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
      assertThat(problem.getDetail()).isEqualTo(message);
    }
  }

  @Nested
  @DisplayName("handleIllegalStateのテスト")
  class HandleIllegalStateTest {

    @Test
    @DisplayName("IllegalStateExceptionをHTTP 409のProblemDetailに変換すること")
    void shouldReturn409ProblemDetailForIllegalState() {
      // Arrange
      String message = "Invalid status transition";
      IllegalStateException ex = new IllegalStateException(message);

      // Act
      ProblemDetail problem = handler.handleIllegalState(ex);

      // Assert
      assertThat(problem.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
      assertThat(problem.getDetail()).isEqualTo(message);
    }
  }

  @Nested
  @DisplayName("handleIllegalArgumentのテスト")
  class HandleIllegalArgumentTest {

    @Test
    @DisplayName("IllegalArgumentExceptionをHTTP 400のProblemDetailに変換すること")
    void shouldReturn400ProblemDetailForIllegalArgument() {
      // Arrange
      String message = "Invalid argument";
      IllegalArgumentException ex = new IllegalArgumentException(message);

      // Act
      ProblemDetail problem = handler.handleIllegalArgument(ex);

      // Assert
      assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(problem.getDetail()).isEqualTo(message);
    }
  }

  @Nested
  @DisplayName("handleOptimisticLockingFailureのテスト")
  class HandleOptimisticLockingFailureTest {

    @Test
    @DisplayName("OptimisticLockingFailureExceptionをHTTP 409のProblemDetailに変換すること")
    void shouldReturn409ProblemDetailForOptimisticLockingFailure() {
      // Arrange
      String message = "Optimistic locking failure";
      OptimisticLockingFailureException ex = new OptimisticLockingFailureException(message);

      // Act
      ProblemDetail problem = handler.handleOptimisticLockingFailure(ex);

      // Assert
      assertThat(problem.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
      assertThat(problem.getDetail()).isEqualTo(message);
    }

    @Test
    @DisplayName("ObjectOptimisticLockingFailureExceptionをHTTP 409のProblemDetailに変換すること（サブクラスも対象）")
    void shouldReturn409ProblemDetailForObjectOptimisticLockingFailure() {
      // Arrange
      ObjectOptimisticLockingFailureException ex =
          new ObjectOptimisticLockingFailureException(BacklogItem.class, SnowflakeId.of(100L));

      // Act
      ProblemDetail problem = handler.handleOptimisticLockingFailure(ex);

      // Assert
      assertThat(problem.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
      assertThat(problem.getDetail()).isNotNull();
    }
  }
}
