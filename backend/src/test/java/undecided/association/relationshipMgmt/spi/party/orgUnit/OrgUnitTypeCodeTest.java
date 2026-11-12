package undecided.association.relationshipMgmt.spi.party.orgUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("OrgUnitTypeCode値オブジェクトのテスト")
class OrgUnitTypeCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効なコードからOrgUnitTypeCodeを正常に作成すること")
    void shouldCreateOrgUnitTypeCodeFromValidString() {
      // Act
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("DEPARTMENT");

      // Assert
      assertThat(orgUnitTypeCode).isNotNull();
      assertThat(orgUnitTypeCode.value()).isEqualTo("DEPARTMENT");
    }

    @Test
    @DisplayName("前後に空白があるコードをトリムして作成すること")
    void shouldCreateOrgUnitTypeCodeWithTrimmedValue() {
      // Act
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of(" DEPARTMENT ");

      // Assert
      assertThat(orgUnitTypeCode.value()).isEqualTo("DEPARTMENT");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitTypeCode.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitTypeCode.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitTypeCode.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("21文字のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Arrange
      String tooLong = "ABCDEFGHIJKLMNOPQRSTU";

      // Act & Assert
      assertThatThrownBy(() -> OrgUnitTypeCode.of(tooLong))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("20");
    }

    @Test
    @DisplayName("英小文字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsLowercase() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitTypeCode.of("department"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("uppercase");
    }

    @Test
    @DisplayName("数字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsNumbers() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitTypeCode.of("DEPARTMENT1"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("uppercase");
    }

    @Test
    @DisplayName("英大文字とアンダースコアのみのコードは作成できること")
    void shouldCreateOrgUnitTypeCodeWithUppercaseAndUnderscore() {
      // Act
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("SALES_DEPT");

      // Assert
      assertThat(orgUnitTypeCode.value()).isEqualTo("SALES_DEPT");
    }

    @Test
    @DisplayName("1文字のコードは作成できること")
    void shouldCreateOrgUnitTypeCodeWithSingleCharacter() {
      // Act
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("A");

      // Assert
      assertThat(orgUnitTypeCode.value()).isEqualTo("A");
    }

    @Test
    @DisplayName("20文字のコードは作成できること")
    void shouldCreateOrgUnitTypeCodeWithMaxCharacters() {
      // Arrange
      String maxCode = "ABCDEFGHIJKLMNOPQRST";

      // Act
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of(maxCode);

      // Assert
      assertThat(orgUnitTypeCode.value()).isEqualTo(maxCode);
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のOrgUnitTypeCodeインスタンスを作成すること")
    void shouldCreateEmptyOrgUnitTypeCode() {
      // Act
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.empty();

      // Assert
      assertThat(orgUnitTypeCode).isNotNull();
      assertThat(orgUnitTypeCode.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("DEPARTMENT");

      // Act
      String result = orgUnitTypeCode.value();

      // Assert
      assertThat(result).isEqualTo("DEPARTMENT");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のOrgUnitTypeCodeはtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.empty();

      // Assert
      assertThat(orgUnitTypeCode.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つOrgUnitTypeCodeはfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("DEPARTMENT");

      // Assert
      assertThat(orgUnitTypeCode.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のOrgUnitTypeCodeは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      OrgUnitTypeCode orgUnitTypeCode1 = OrgUnitTypeCode.of("DEPARTMENT");
      OrgUnitTypeCode orgUnitTypeCode2 = OrgUnitTypeCode.of("DEPARTMENT");

      // Assert
      assertThat(orgUnitTypeCode1).isEqualTo(orgUnitTypeCode2);
    }

    @Test
    @DisplayName("異なる値のOrgUnitTypeCodeは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      OrgUnitTypeCode orgUnitTypeCode1 = OrgUnitTypeCode.of("DEPARTMENT");
      OrgUnitTypeCode orgUnitTypeCode2 = OrgUnitTypeCode.of("TEAM");

      // Assert
      assertThat(orgUnitTypeCode1).isNotEqualTo(orgUnitTypeCode2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("DEPARTMENT");

      // Act
      String result = orgUnitTypeCode.toString();

      // Assert
      assertThat(result).contains("DEPARTMENT");
    }
  }

  @Nested
  @DisplayName("定数のテスト")
  class ConstantsTest {

    @Test
    @DisplayName("DEPARTMENT定数が正しく定義されていること")
    void shouldHaveDepartmentConstant() {
      assertThat(OrgUnitTypeCode.DEPARTMENT.value()).isEqualTo("DEPARTMENT");
    }

    @Test
    @DisplayName("TEAM定数が正しく定義されていること")
    void shouldHaveTeamConstant() {
      assertThat(OrgUnitTypeCode.TEAM.value()).isEqualTo("TEAM");
    }

    @Test
    @DisplayName("SECTION定数が正しく定義されていること")
    void shouldHaveSectionConstant() {
      assertThat(OrgUnitTypeCode.SECTION.value()).isEqualTo("SECTION");
    }

    @Test
    @DisplayName("MIN_LENGTHは1であること")
    void shouldHaveMinLengthEqualToOne() {
      assertThat(OrgUnitTypeCode.MIN_LENGTH).isEqualTo(1);
    }

    @Test
    @DisplayName("MAX_LENGTHは20であること")
    void shouldHaveMaxLengthEqualToTwenty() {
      assertThat(OrgUnitTypeCode.MAX_LENGTH).isEqualTo(20);
    }
  }
}
