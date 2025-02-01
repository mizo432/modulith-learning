package undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.exception.BusinessException;

@DisplayName("MyCompanyクラスのテスト")
class MyCompanyTest {

  @Nested
  @DisplayName("newInstanceメソッドのテスト")
  class NewInstanceMethodTest {

    @Test
    @DisplayName("正常系: 名前とカナ名が有効な場合にMyCompanyを正しく生成する")
    void shouldCreateMyCompanyWhenNameAndKanaNameAreValid() {
      // Arrange
      String validName = "Valid Company";
      String validKanaName = "ValidKanaName";

      // Act
      MyCompany result = MyCompany.newInstance(validName, validKanaName);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getMyCompanyId()).isNotNull();
      assertThat(result.getName()).isEqualTo(validName);
      assertThat(result.getKanaName()).isEqualTo(validKanaName);
      assertThat(result.getInitials()).isEqualTo(String.valueOf(validKanaName.charAt(0)));
    }

    @Test
    @DisplayName("異常系: 名前がnullの場合に例外をスローする")
    void shouldThrowExceptionWhenNameIsNull() {
      // Arrange
      String invalidName = null;
      String validKanaName = "ValidKanaName";

      // Act & Assert
      assertThatThrownBy(() -> MyCompany.newInstance(invalidName, validKanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("異常系: 名前が空文字の場合に例外をスローする")
    void shouldThrowExceptionWhenNameIsEmpty() {
      // Arrange
      String invalidName = "";
      String validKanaName = "ValidKanaName";

      // Act & Assert
      assertThatThrownBy(() -> MyCompany.newInstance(invalidName, validKanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("異常系: カナ名がnullの場合に例外をスローする")
    void shouldThrowExceptionWhenKanaNameIsNull() {
      // Arrange
      String validName = "Valid Company";
      String invalidKanaName = null;

      // Act & Assert
      assertThatThrownBy(() -> MyCompany.newInstance(validName, invalidKanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("異常系: カナ名が空文字の場合に例外をスローする")
    void shouldThrowExceptionWhenKanaNameIsEmpty() {
      // Arrange
      String validName = "Valid Company";
      String invalidKanaName = "";

      // Act & Assert
      assertThatThrownBy(() -> MyCompany.newInstance(validName, invalidKanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("異常系: カナ名が半角幅制限外の場合に例外をスローする")
    void shouldThrowExceptionWhenKanaNameExceedsHalfWidthLimit() {
      // Arrange
      String validName = "Valid Company";
      String invalidKanaName = "あ".repeat(101);

      // Act & Assert
      assertThatThrownBy(() -> MyCompany.newInstance(validName, invalidKanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("正常系: カナ名が半角幅制限内の場合にMyCompanyを生成する")
    void shouldCreateMyCompanyWhenKanaNameIsWithinHalfWidthLimit() {
      // Arrange
      String validName = "Valid Company";
      String validKanaName = "Kana1234";

      // Act
      MyCompany result = MyCompany.newInstance(validName, validKanaName);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getMyCompanyId()).isNotNull();
      assertThat(result.getName()).isEqualTo(validName);
      assertThat(result.getKanaName()).isEqualTo(validKanaName);
      assertThat(result.getInitials()).isEqualTo(String.valueOf(validKanaName.charAt(0)));
    }
  }
}
