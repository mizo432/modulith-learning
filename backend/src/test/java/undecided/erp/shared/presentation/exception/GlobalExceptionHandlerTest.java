package undecided.erp.shared.presentation.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("GlobalExceptionHandlerの単体テスト")
class GlobalExceptionHandlerTest {

  @Autowired private WebApplicationContext context;

  @Nested
  @DisplayName("handleExceptionメソッドのテスト")
  class HandleExceptionTests {

    @Test
    @DisplayName("例外が投げられた場合、エラーレスポンスを返却する")
    void shouldReturnErrorResponseWhenExceptionIsThrown() {
      // Arrange
      GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
      Exception exception = new Exception("サーバーでエラーが発生しました");

      // Act
      ResponseEntity<Map<String, Object>> response =
          globalExceptionHandler.handleException(exception);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody()).containsKey(" error");
      assertThat(response.getBody().get(" error")).isEqualTo("サーバーでエラーが発生しました");
    }
  }
}
