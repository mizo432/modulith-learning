package undecided.erp.shared.presentation.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import undecided.erp.common.exception.BusinessException;
import undecided.shared.common.message.ResultMessage;
import undecided.shared.common.message.ResultMessages;

@DisplayName("GlobalExceptionHandlerTestクラスのテスト")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

  @Nested
  @DisplayName("handleBusinessExceptionメソッド")
  class HandleBusinessException {

    @Test
    @DisplayName("BusinessExceptionを適切に処理できること")
    void shouldHandleBusinessExceptionProperly() {
      // Arrange
      String errorMessage = "Business error occurred";
      ResultMessages resultMessages =
          ResultMessages.error().add(ResultMessage.fromText(errorMessage));

      BusinessException exception = new BusinessException(resultMessages);
      ResultMessage lastMessage = ResultMessage.fromText(errorMessage);

      // Act
      ResponseEntity<ProblemDetail> response =
          globalExceptionHandler.handleBusinessException(exception);

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getDetail()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("引数がnullのBusinessExceptionの場合にNullPointerExceptionをスローすること")
    void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
      // Arrange
      BusinessException exception = null;

      // Act & Assert000
      assertThatThrownBy(() -> globalExceptionHandler.handleBusinessException(exception))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("e must not be null.");
    }
  }
}
