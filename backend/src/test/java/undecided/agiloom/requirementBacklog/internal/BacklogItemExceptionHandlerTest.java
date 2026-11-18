package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * BacklogItemExceptionHandlerの単体テスト。
 *
 * <p>Spring Bootコンテキストに依存せず、ハンドラーインスタンスを直接呼び出して検証する。
 */
@Tag("small")
@DisplayName("BacklogItemExceptionHandlerの単体テスト")
class BacklogItemExceptionHandlerTest {

  private final BacklogItemExceptionHandler handler = new BacklogItemExceptionHandler();

  @Nested
  @DisplayName("handlePermissionDeniedのテスト")
  class HandlePermissionDeniedTest {

    @Test
    @DisplayName("BacklogItemPermissionDeniedExceptionをHTTP 403に変換すること")
    void shouldReturn403ForPermissionDenied() {
      // Arrange
      String message = "Permission denied";
      BacklogItemPermissionDeniedException ex = new BacklogItemPermissionDeniedException(message);

      // Act
      ResponseEntity<Map<String, String>> response = handler.handlePermissionDenied(ex);

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
      assertThat(response.getBody()).containsEntry("message", message);
    }
  }

  @Nested
  @DisplayName("handleIllegalStateのテスト")
  class HandleIllegalStateTest {

    @Test
    @DisplayName("IllegalStateExceptionをHTTP 409に変換すること")
    void shouldReturn409ForIllegalState() {
      // Arrange
      String message = "Invalid status transition";
      IllegalStateException ex = new IllegalStateException(message);

      // Act
      ResponseEntity<Map<String, String>> response = handler.handleIllegalState(ex);

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
      assertThat(response.getBody()).containsEntry("message", message);
    }
  }

  @Nested
  @DisplayName("handleIllegalArgumentのテスト")
  class HandleIllegalArgumentTest {

    @Test
    @DisplayName("IllegalArgumentExceptionをHTTP 400に変換すること")
    void shouldReturn400ForIllegalArgument() {
      // Arrange
      String message = "Invalid argument";
      IllegalArgumentException ex = new IllegalArgumentException(message);

      // Act
      ResponseEntity<Map<String, String>> response = handler.handleIllegalArgument(ex);

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertThat(response.getBody()).containsEntry("message", message);
    }
  }

  @Nested
  @DisplayName("handleOptimisticLockingFailureのテスト")
  class HandleOptimisticLockingFailureTest {

    @Test
    @DisplayName("OptimisticLockingFailureExceptionをHTTP 409に変換すること")
    void shouldReturn409ForOptimisticLockingFailure() {
      // Arrange
      String message = "Optimistic locking failure";
      OptimisticLockingFailureException ex = new OptimisticLockingFailureException(message);

      // Act
      ResponseEntity<Map<String, String>> response = handler.handleOptimisticLockingFailure(ex);

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
      assertThat(response.getBody()).containsEntry("message", message);
    }

    @Test
    @DisplayName("ObjectOptimisticLockingFailureExceptionをHTTP 409に変換すること（サブクラスも対象）")
    void shouldReturn409ForObjectOptimisticLockingFailure() {
      // Arrange
      ObjectOptimisticLockingFailureException ex =
          new ObjectOptimisticLockingFailureException(BacklogItem.class, SnowflakeId.of(100L));

      // Act
      ResponseEntity<Map<String, String>> response = handler.handleOptimisticLockingFailure(ex);

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
      assertThat(response.getBody()).isNotNull();
    }
  }
}
