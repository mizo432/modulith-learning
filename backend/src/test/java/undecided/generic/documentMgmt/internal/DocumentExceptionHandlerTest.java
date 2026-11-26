package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * DocumentExceptionHandlerの単体テスト。
 *
 * <p>Spring Bootコンテキストに依存せず、ハンドラーインスタンスを直接呼び出して検証する。
 * エラーレスポンスは RFC 9457（Problem Details）に統一されていることを確認する。
 */
@Tag("small")
@DisplayName("DocumentExceptionHandlerの単体テスト")
class DocumentExceptionHandlerTest {

  private final DocumentExceptionHandler handler = new DocumentExceptionHandler();

  @Test
  @DisplayName("InvalidDocumentFileExceptionをHTTP 400のProblemDetailに変換すること")
  void shouldReturn400ProblemDetailForInvalidDocumentFile() {
    String message = "Invalid document file";
    ProblemDetail problem = handler.handleInvalidDocumentFile(new InvalidDocumentFileException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("DocumentNotFoundExceptionをHTTP 404のProblemDetailに変換すること")
  void shouldReturn404ProblemDetailForDocumentNotFound() {
    String message = "Document not found";
    ProblemDetail problem = handler.handleDocumentNotFound(new DocumentNotFoundException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("DocumentPermissionDeniedExceptionをHTTP 403のProblemDetailに変換すること")
  void shouldReturn403ProblemDetailForPermissionDenied() {
    String message = "Permission denied";
    ProblemDetail problem =
        handler.handlePermissionDenied(new DocumentPermissionDeniedException(message));
    assertThat(problem.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    assertThat(problem.getDetail()).isEqualTo(message);
  }

  @Test
  @DisplayName("IllegalStateExceptionをHTTP 409のProblemDetailに変換すること")
  void shouldReturn409ProblemDetailForIllegalState() {
    String message = "Document already deleted";
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
}
