package undecided.erp.shared.presentation.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import undecided.erp.common.exception.BusinessException;

@SpringBootTest
@DisplayName("GlobalExceptionHandlerのテスト")
class GlobalExceptionHandlerTest {

  @Autowired private GlobalExceptionHandler globalExceptionHandler;

  @Nested
  @DisplayName("handleBusinessExceptionメソッドのテスト")
  class HandleBusinessExceptionTests {

    @Test
    @DisplayName("BusinessExceptionを適切に処理できる")
    void shouldHandleBusinessExceptionProperly() {
      // Arrange
      String errorMessage = "Business logic failed.";
      BusinessException businessException = new BusinessException(errorMessage);

      // Act
      ResponseEntity<ProblemDetail> response =
          globalExceptionHandler.handleBusinessException(businessException);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getBody().getDetail()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Nullの例外を処理しない")
    void shouldNotHandleNullException() {
      // Arrange & Act
      ResponseEntity<ProblemDetail> response = null;

      try {
        response = globalExceptionHandler.handleBusinessException(null);
      } catch (Exception e) {
        // Catches NullPointerException since the input exception is null
        assertThat(e).isInstanceOf(NullPointerException.class);
      }

      // Assert
      assertThat(response).isNull();
    }
  }
}
