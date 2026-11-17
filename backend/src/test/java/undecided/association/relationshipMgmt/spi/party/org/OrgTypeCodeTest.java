package undecided.association.relationshipMgmt.spi.party.org;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("OrgTypeCode値オブジェクトのテスト")
class OrgTypeCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効なコードからOrgTypeCodeを正常に作成すること")
    void shouldCreateOrgTypeCodeFromValidString() {
      // Act
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");

      // Assert
      assertThat(orgTypeCode).isNotNull();
      assertThat(orgTypeCode.value()).isEqualTo("COMPANY");
    }

    @Test
    @DisplayName("前後に空白があるコードをトリムして作成すること")
    void shouldCreateOrgTypeCodeWithTrimmedValue() {
      // Act
      OrgTypeCode orgTypeCode = OrgTypeCode.of(" COMPANY ");

      // Assert
      assertThat(orgTypeCode.value()).isEqualTo("COMPANY");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> OrgTypeCode.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> OrgTypeCode.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> OrgTypeCode.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("21文字のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Arrange
      String tooLong = "ABCDEFGHIJKLMNOPQRSTU";

      // Act & Assert
      assertThatThrownBy(() -> OrgTypeCode.of(tooLong))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("20");
    }

    @Test
    @DisplayName("英小文字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsLowercase() {
      // Act & Assert
      assertThatThrownBy(() -> OrgTypeCode.of("company"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("uppercase");
    }

    @Test
    @DisplayName("数字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsNumbers() {
      // Act & Assert
      assertThatThrownBy(() -> OrgTypeCode.of("COMPANY1"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("uppercase");
    }

    @Test
    @DisplayName("英大文字とアンダースコアのみのコードは作成できること")
    void shouldCreateOrgTypeCodeWithUppercaseAndUnderscore() {
      // Act
      OrgTypeCode orgTypeCode = OrgTypeCode.of("HEAD_OFFICE");

      // Assert
      assertThat(orgTypeCode.value()).isEqualTo("HEAD_OFFICE");
    }

    @Test
    @DisplayName("1文字のコードは作成できること")
    void shouldCreateOrgTypeCodeWithSingleCharacter() {
      // Act
      OrgTypeCode orgTypeCode = OrgTypeCode.of("A");

      // Assert
      assertThat(orgTypeCode.value()).isEqualTo("A");
    }

    @Test
    @DisplayName("20文字のコードは作成できること")
    void shouldCreateOrgTypeCodeWithMaxCharacters() {
      // Arrange
      String maxCode = "ABCDEFGHIJKLMNOPQRST";

      // Act
      OrgTypeCode orgTypeCode = OrgTypeCode.of(maxCode);

      // Assert
      assertThat(orgTypeCode.value()).isEqualTo(maxCode);
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のOrgTypeCodeインスタンスを作成すること")
    void shouldCreateEmptyOrgTypeCode() {
      // Act
      OrgTypeCode orgTypeCode = OrgTypeCode.empty();

      // Assert
      assertThat(orgTypeCode).isNotNull();
      assertThat(orgTypeCode.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");

      // Act
      String result = orgTypeCode.value();

      // Assert
      assertThat(result).isEqualTo("COMPANY");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のOrgTypeCodeはtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      OrgTypeCode orgTypeCode = OrgTypeCode.empty();

      // Assert
      assertThat(orgTypeCode.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つOrgTypeCodeはfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");

      // Assert
      assertThat(orgTypeCode.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のOrgTypeCodeは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      OrgTypeCode orgTypeCode1 = OrgTypeCode.of("COMPANY");
      OrgTypeCode orgTypeCode2 = OrgTypeCode.of("COMPANY");

      // Assert
      assertThat(orgTypeCode1).isEqualTo(orgTypeCode2);
    }

    @Test
    @DisplayName("異なる値のOrgTypeCodeは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      OrgTypeCode orgTypeCode1 = OrgTypeCode.of("COMPANY");
      OrgTypeCode orgTypeCode2 = OrgTypeCode.of("STORE");

      // Assert
      assertThat(orgTypeCode1).isNotEqualTo(orgTypeCode2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");

      // Act
      String result = orgTypeCode.toString();

      // Assert
      assertThat(result).contains("COMPANY");
    }
  }

  @Nested
  @DisplayName("定数のテスト")
  class ConstantsTest {

    @Test
    @DisplayName("COMPANY定数が正しく定義されていること")
    void shouldHaveCompanyConstant() {
      assertThat(OrgTypeCode.COMPANY.value()).isEqualTo("COMPANY");
    }

    @Test
    @DisplayName("STORE定数が正しく定義されていること")
    void shouldHaveStoreConstant() {
      assertThat(OrgTypeCode.STORE.value()).isEqualTo("STORE");
    }

    @Test
    @DisplayName("DEPARTMENT定数が正しく定義されていること")
    void shouldHaveDepartmentConstant() {
      assertThat(OrgTypeCode.DEPARTMENT.value()).isEqualTo("DEPARTMENT");
    }

    @Test
    @DisplayName("MIN_LENGTHは1であること")
    void shouldHaveMinLengthEqualToOne() {
      assertThat(OrgTypeCode.MIN_LENGTH).isEqualTo(1);
    }

    @Test
    @DisplayName("MAX_LENGTHは20であること")
    void shouldHaveMaxLengthEqualToTwenty() {
      assertThat(OrgTypeCode.MAX_LENGTH).isEqualTo(20);
    }
  }
}
