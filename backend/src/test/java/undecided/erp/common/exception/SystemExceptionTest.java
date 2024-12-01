package undecided.erp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SystemException のテスト")
class SystemExceptionTest {

  @Nested
  @DisplayName("getCode メソッドのテスト")
  class GetCodeTests {

    @Test
    @DisplayName("コードがnullの場合のテスト")
    void shouldReturnNullCode() {
      // Arrange
      SystemException exception = new SystemException(null, "Message", new Throwable());

      // Act
      String code = exception.getCode();

      // Assert
      assertThat(code).isNull();
    }

    @Test
    @DisplayName("コードが指定された場合のテスト")
    void shouldReturnSpecifiedCode() {
      // Arrange
      String expectedCode = "ERR001";
      SystemException exception = new SystemException(expectedCode, "Message", new Throwable());

      // Act
      String actualCode = exception.getCode();

      // Assert
      assertThat(actualCode).isEqualTo(expectedCode);
    }
  }
}
