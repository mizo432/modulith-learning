package undecided.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import undecided.config.presentation.exception.GlobalExceptionHandler;
import undecided.shared.message.ResultMessage;
import undecided.shared.message.ResultMessages;

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
      ProblemDetail response = globalExceptionHandler.handleBusinessException(exception);

      // Assert

      assertThat(response).isNotNull();
      assertThat(response.getDetail()).isEqualTo(errorMessage);
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
