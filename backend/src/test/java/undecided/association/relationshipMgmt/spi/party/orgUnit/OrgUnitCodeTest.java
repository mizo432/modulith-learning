package undecided.association.relationshipMgmt.spi.party.orgUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("OrgUnitCode値オブジェクトのテスト")
class OrgUnitCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効なコードからOrgUnitCodeを正常に作成すること")
    void shouldCreateOrgUnitCodeFromValidString() {
      // Act
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT001");

      // Assert
      assertThat(orgUnitCode).isNotNull();
      assertThat(orgUnitCode.value()).isEqualTo("DEPT001");
    }

    @Test
    @DisplayName("前後に空白があるコードをトリムして作成すること")
    void shouldCreateOrgUnitCodeWithTrimmedValue() {
      // Act
      OrgUnitCode orgUnitCode = OrgUnitCode.of(" DEPT001 ");

      // Assert
      assertThat(orgUnitCode.value()).isEqualTo("DEPT001");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitCode.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitCode.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitCode.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("31文字のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Arrange
      String tooLong = "1234567890123456789012345678901";

      // Act & Assert
      assertThatThrownBy(() -> OrgUnitCode.of(tooLong))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("30");
    }

    @Test
    @DisplayName("特殊文字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsSpecialCharacters() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitCode.of("DEPT@001"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("alphanumeric");
    }

    @Test
    @DisplayName("英数字とアンダースコアのみのコードは作成できること")
    void shouldCreateOrgUnitCodeWithAlphanumericAndUnderscore() {
      // Act
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT_001_test");

      // Assert
      assertThat(orgUnitCode.value()).isEqualTo("DEPT_001_test");
    }

    @Test
    @DisplayName("1文字のコードは作成できること")
    void shouldCreateOrgUnitCodeWithSingleCharacter() {
      // Act
      OrgUnitCode orgUnitCode = OrgUnitCode.of("A");

      // Assert
      assertThat(orgUnitCode.value()).isEqualTo("A");
    }

    @Test
    @DisplayName("30文字のコードは作成できること")
    void shouldCreateOrgUnitCodeWithMaxCharacters() {
      // Arrange
      String maxCode = "123456789012345678901234567890";

      // Act
      OrgUnitCode orgUnitCode = OrgUnitCode.of(maxCode);

      // Assert
      assertThat(orgUnitCode.value()).isEqualTo(maxCode);
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のOrgUnitCodeインスタンスを作成すること")
    void shouldCreateEmptyOrgUnitCode() {
      // Act
      OrgUnitCode orgUnitCode = OrgUnitCode.empty();

      // Assert
      assertThat(orgUnitCode).isNotNull();
      assertThat(orgUnitCode.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT001");

      // Act
      String result = orgUnitCode.value();

      // Assert
      assertThat(result).isEqualTo("DEPT001");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のOrgUnitCodeはtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      OrgUnitCode orgUnitCode = OrgUnitCode.empty();

      // Assert
      assertThat(orgUnitCode.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つOrgUnitCodeはfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT001");

      // Assert
      assertThat(orgUnitCode.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のOrgUnitCodeは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      OrgUnitCode orgUnitCode1 = OrgUnitCode.of("DEPT001");
      OrgUnitCode orgUnitCode2 = OrgUnitCode.of("DEPT001");

      // Assert
      assertThat(orgUnitCode1).isEqualTo(orgUnitCode2);
    }

    @Test
    @DisplayName("異なる値のOrgUnitCodeは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      OrgUnitCode orgUnitCode1 = OrgUnitCode.of("DEPT001");
      OrgUnitCode orgUnitCode2 = OrgUnitCode.of("DEPT002");

      // Assert
      assertThat(orgUnitCode1).isNotEqualTo(orgUnitCode2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT001");

      // Act
      String result = orgUnitCode.toString();

      // Assert
      assertThat(result).contains("DEPT001");
    }
  }

  @Nested
  @DisplayName("定数のテスト")
  class ConstantsTest {

    @Test
    @DisplayName("MIN_LENGTHは1であること")
    void shouldHaveMinLengthEqualToOne() {
      assertThat(OrgUnitCode.MIN_LENGTH).isEqualTo(1);
    }

    @Test
    @DisplayName("MAX_LENGTHは30であること")
    void shouldHaveMaxLengthEqualToThirty() {
      assertThat(OrgUnitCode.MAX_LENGTH).isEqualTo(30);
    }
  }
}
