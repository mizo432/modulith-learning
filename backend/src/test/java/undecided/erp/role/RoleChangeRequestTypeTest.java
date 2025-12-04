package undecided.erp.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RoleChangeRequestTypeクラスのテスト")
class RoleChangeRequestTypeTest {

  @Nested
  @DisplayName("valueOfCodeメソッドのテスト")
  class ValueOfCodeTest {

    @Test
    @DisplayName("codeがnullの場合、IllegalArgumentExceptionがスローされるべき")
    void shouldThrowIllegalArgumentExceptionWhenCodeIsNull() {
      // Arrange
      String code = null;

      // Act & Assert
      assertThatThrownBy(() -> RoleChangeRequestType.valueOfCode(code))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("code must not be null.");
    }

    @Test
    @DisplayName("codeが空文字の場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeIsEmpty() {
      // Arrange
      String code = "";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("codeが'10'の場合、CREATEが返されるべき")
    void shouldReturnCreateWhenCodeIs10() {
      // Arrange
      String code = "10";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.CREATE);
    }

    @Test
    @DisplayName("codeが'20'の場合、UPDATEが返されるべき")
    void shouldReturnUpdateWhenCodeIs20() {
      // Arrange
      String code = "20";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UPDATE);
    }

    @Test
    @DisplayName("codeが'30'の場合、DELETEが返されるべき")
    void shouldReturnDeleteWhenCodeIs30() {
      // Arrange
      String code = "30";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.DELETE);
    }

    @Test
    @DisplayName("codeが不正な値の場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeIsInvalid() {
      // Arrange
      String code = "99";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }
  }
}
