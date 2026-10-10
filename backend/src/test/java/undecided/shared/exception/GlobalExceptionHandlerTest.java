package undecided.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import undecided.config.presentation.exception.GlobalExceptionHandler;
import undecided.config.presentation.web.idempotency.IdempotencyKeyException;
import undecided.shared.message.ResultMessage;
import undecided.shared.message.ResultMessages;

@Tag("small")
@DisplayName("GlobalExceptionHandlerの単体テスト")
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock private ExceptionLogger exceptionLogger;

  private GlobalExceptionHandler globalExceptionHandler;

  @BeforeEach
  void setUp() {
    globalExceptionHandler = new GlobalExceptionHandler(exceptionLogger);
  }

  @Nested
  @DisplayName("handleBusinessExceptionメソッドのテスト")
  class HandleBusinessExceptionTest {

    @Test
    @DisplayName("BusinessExceptionを適切に処理し、BAD_REQUESTのProblemDetailを返すこと")
    void shouldReturnBadRequestProblemDetailWhenBusinessExceptionOccurs() {
      // Arrange
      String errorMessage = "Business error occurred";
      ResultMessages resultMessages =
          ResultMessages.error().add(ResultMessage.fromText(errorMessage));
      BusinessException exception = new BusinessException(resultMessages);

      // Act
      ProblemDetail response = globalExceptionHandler.handleBusinessException(exception);

      // Assert
      assertThat(response).as("ProblemDetailがnullでないこと").isNotNull();
      assertThat(response.getStatus())
          .as("HTTPステータスが400 BAD_REQUESTであること")
          .isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getDetail())
          .as("エラーメッセージが最後のResultMessageのテキストと一致すること")
          .isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("引数がnullの場合にNullPointerExceptionをスローすること")
    void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> globalExceptionHandler.handleBusinessException(null))
          .as("null引数時にNullPointerExceptionが発生すること")
          .isInstanceOf(NullPointerException.class)
          .hasMessage("e must not be null.");
    }
  }

  @Nested
  @DisplayName("handleValidationメソッドのテスト")
  class HandleValidationTest {

    @Test
    @DisplayName("MethodArgumentNotValidExceptionを適切に処理し、フィールドエラーを含むProblemDetailを返すこと")
    void shouldReturnProblemDetailWithFieldErrorsWhenValidationFails() {
      // Arrange
      FieldError fieldError = new FieldError("objectName", "fieldName", "Field must not be empty");
      BindingResult bindingResult = mock(BindingResult.class);
      when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

      org.springframework.web.bind.MethodArgumentNotValidException ex =
          new org.springframework.web.bind.MethodArgumentNotValidException(null, bindingResult);
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(request.getRequestURI()).thenReturn("/api/test");

      // Act
      ProblemDetail response = globalExceptionHandler.handleValidation(ex, request);

      // Assert
      assertThat(response).as("ProblemDetailがnullでないこと").isNotNull();
      assertThat(response.getStatus())
          .as("HTTPステータスが400 BAD_REQUESTであること")
          .isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getDetail())
          .as("詳細メッセージがValidation failedであること")
          .isEqualTo("Validation failed");
      assertThat(response.getTitle())
          .as("タイトルがValidation Errorであること")
          .isEqualTo("Validation Error");
      @SuppressWarnings("unchecked")
      List<Map<String, String>> fieldErrors =
          (List<Map<String, String>>) response.getProperties().get("fieldErrors");
      assertThat(fieldErrors).as("フィールドエラーが1件含まれていること").hasSize(1);
      assertThat(fieldErrors.get(0).get("field")).isEqualTo("fieldName");
      assertThat(fieldErrors.get(0).get("message")).isEqualTo("Field must not be empty");
    }

    @Test
    @DisplayName("フィールドエラーが空の場合、空のリストを返すこと")
    void shouldReturnEmptyFieldErrorsListWhenNoFieldErrors() {
      // Arrange
      BindingResult bindingResult = mock(BindingResult.class);
      when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

      org.springframework.web.bind.MethodArgumentNotValidException ex =
          new org.springframework.web.bind.MethodArgumentNotValidException(null, bindingResult);
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(request.getRequestURI()).thenReturn("/api/test");

      // Act
      ProblemDetail response = globalExceptionHandler.handleValidation(ex, request);

      // Assert
      @SuppressWarnings("unchecked")
      List<Map<String, String>> fieldErrors =
          (List<Map<String, String>>) response.getProperties().get("fieldErrors");
      assertThat(fieldErrors).as("フィールドエラーが空のリストであること").isEmpty();
    }
  }

  @Nested
  @DisplayName("handleConstraintViolationメソッドのテスト")
  class HandleConstraintViolationTest {

    @Test
    @DisplayName("ConstraintViolationExceptionを適切に処理し、違反情報を含むProblemDetailを返すこと")
    void shouldReturnProblemDetailWithViolationsWhenConstraintViolationOccurs() {
      // Arrange
      ConstraintViolation<?> violation = mock(ConstraintViolation.class);
      Path violationPath = mock(Path.class);
      when(violationPath.toString()).thenReturn("fieldName");
      when(violation.getPropertyPath()).thenReturn(violationPath);
      when(violation.getMessage()).thenReturn("Must not be null");

      ConstraintViolationException ex =
          new ConstraintViolationException(Collections.singleton(violation));
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(request.getRequestURI()).thenReturn("/api/test");

      // Act
      ProblemDetail response = globalExceptionHandler.handleConstraintViolation(ex, request);

      // Assert
      assertThat(response).as("ProblemDetailがnullでないこと").isNotNull();
      assertThat(response.getStatus())
          .as("HTTPステータスが400 BAD_REQUESTであること")
          .isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getTitle())
          .as("タイトルがValidation Errorであること")
          .isEqualTo("Validation Error");
      @SuppressWarnings("unchecked")
      List<Map<String, String>> violations =
          (List<Map<String, String>>) response.getProperties().get("violations");
      assertThat(violations).as("違反情報が1件含まれていること").hasSize(1);
      assertThat(violations.get(0).get("path")).isEqualTo("fieldName");
      assertThat(violations.get(0).get("message")).isEqualTo("Must not be null");
    }

    @Test
    @DisplayName("違反が空の場合、空のリストを返すこと")
    void shouldReturnEmptyViolationsListWhenNoViolations() {
      // Arrange
      ConstraintViolationException ex = new ConstraintViolationException(Collections.emptySet());
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(request.getRequestURI()).thenReturn("/api/test");

      // Act
      ProblemDetail response = globalExceptionHandler.handleConstraintViolation(ex, request);

      // Assert
      @SuppressWarnings("unchecked")
      List<Map<String, String>> violations =
          (List<Map<String, String>>) response.getProperties().get("violations");
      assertThat(violations).as("違反情報が空のリストであること").isEmpty();
    }
  }

  @Nested
  @DisplayName("handleIdempotencyKeyメソッドのテスト")
  class HandleIdempotencyKeyTest {

    @Test
    @DisplayName("IdempotencyKeyExceptionを適切に処理し、409 ConflictのProblemDetailを返すこと")
    void shouldReturnConflictProblemDetailWhenIdempotencyKeyExceptionOccurs() {
      // Arrange
      String message = "Idempotency key already exists";
      IdempotencyKeyException exception = new IdempotencyKeyException(message);

      // Act
      ProblemDetail response = globalExceptionHandler.handleIdempotencyKey(exception);

      // Assert
      assertThat(response).as("ProblemDetailがnullでないこと").isNotNull();
      assertThat(response.getStatus())
          .as("HTTPステータスが409 CONFLICTであること")
          .isEqualTo(HttpStatus.CONFLICT.value());
      assertThat(response.getDetail()).as("詳細メッセージが例外メッセージと一致すること").isEqualTo(message);
      assertThat(response.getTitle())
          .as("タイトルがIdempotency Key Conflictであること")
          .isEqualTo("Idempotency Key Conflict");
      assertThat(response.getProperties().get("errorCode"))
          .as("エラーコードがIDEMPOTENCY_KEY_CONFLICTであること")
          .isEqualTo("IDEMPOTENCY_KEY_CONFLICT");
    }
  }

  @Nested
  @DisplayName("handleUnexpectedメソッドのテスト")
  class HandleUnexpectedTest {

    @Test
    @DisplayName("未処理のExceptionを適切に処理し、500 Internal Server ErrorのProblemDetailを返すこと")
    void shouldReturnInternalServerErrorProblemDetailWhenUnexpectedExceptionOccurs() {
      // Arrange
      Exception exception = new RuntimeException("Unexpected error");
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");

      // Act
      ProblemDetail response = globalExceptionHandler.handleUnexpected(exception, request);

      // Assert
      assertThat(response).as("ProblemDetailがnullでないこと").isNotNull();
      assertThat(response.getStatus())
          .as("HTTPステータスが500 INTERNAL_SERVER_ERRORであること")
          .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
      assertThat(response.getTitle())
          .as("タイトルがInternal Server Errorであること")
          .isEqualTo("Internal Server Error");
      assertThat(response.getProperties().get("errorCode"))
          .as("エラーコードがINTERNAL_ERRORであること")
          .isEqualTo("INTERNAL_ERROR");
      verify(exceptionLogger, times(1)).error(exception);
    }

    @Test
    @DisplayName("ExceptionLoggerのerrorメソッドが呼び出されること")
    void shouldCallExceptionLoggerErrorMethodWhenUnexpectedExceptionOccurs() {
      // Arrange
      Exception exception = new RuntimeException("Unexpected error");
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(request.getMethod()).thenReturn("POST");
      when(request.getRequestURI()).thenReturn("/api/test");

      // Act
      globalExceptionHandler.handleUnexpected(exception, request);

      // Assert
      verify(exceptionLogger, times(1)).error(exception);
    }
  }
}
