package undecided.association.relationshipMgmt.spi.party.orgUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("OrgUnitStatus値オブジェクトのテスト")
class OrgUnitStatusTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効なコードからOrgUnitStatusを正常に作成すること")
    void shouldCreateOrgUnitStatusFromValidString() {
      // Act
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.of("ACTIVE");

      // Assert
      assertThat(orgUnitStatus).isNotNull();
      assertThat(orgUnitStatus.value()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("前後に空白があるコードをトリムして作成すること")
    void shouldCreateOrgUnitStatusWithTrimmedValue() {
      // Act
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.of(" ACTIVE ");

      // Assert
      assertThat(orgUnitStatus.value()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitStatus.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitStatus.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitStatus.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("21文字のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Arrange
      String tooLong = "ABCDEFGHIJKLMNOPQRSTU";

      // Act & Assert
      assertThatThrownBy(() -> OrgUnitStatus.of(tooLong))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("20");
    }

    @Test
    @DisplayName("英小文字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsLowercase() {
      // Act & Assert
      assertThatThrownBy(() -> OrgUnitStatus.of("active"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("uppercase");
    }

    @Test
    @DisplayName("英大文字とアンダースコアのみのコードは作成できること")
    void shouldCreateOrgUnitStatusWithUppercaseAndUnderscore() {
      // Act
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.of("UNDER_REVIEW");

      // Assert
      assertThat(orgUnitStatus.value()).isEqualTo("UNDER_REVIEW");
    }

    @Test
    @DisplayName("1文字のコードは作成できること")
    void shouldCreateOrgUnitStatusWithSingleCharacter() {
      // Act
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.of("A");

      // Assert
      assertThat(orgUnitStatus.value()).isEqualTo("A");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のOrgUnitStatusインスタンスを作成すること")
    void shouldCreateEmptyOrgUnitStatus() {
      // Act
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.empty();

      // Assert
      assertThat(orgUnitStatus).isNotNull();
      assertThat(orgUnitStatus.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.of("ACTIVE");

      // Act
      String result = orgUnitStatus.value();

      // Assert
      assertThat(result).isEqualTo("ACTIVE");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のOrgUnitStatusはtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.empty();

      // Assert
      assertThat(orgUnitStatus.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つOrgUnitStatusはfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.of("ACTIVE");

      // Assert
      assertThat(orgUnitStatus.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のOrgUnitStatusは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      OrgUnitStatus orgUnitStatus1 = OrgUnitStatus.of("ACTIVE");
      OrgUnitStatus orgUnitStatus2 = OrgUnitStatus.of("ACTIVE");

      // Assert
      assertThat(orgUnitStatus1).isEqualTo(orgUnitStatus2);
    }

    @Test
    @DisplayName("異なる値のOrgUnitStatusは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      OrgUnitStatus orgUnitStatus1 = OrgUnitStatus.of("ACTIVE");
      OrgUnitStatus orgUnitStatus2 = OrgUnitStatus.of("INACTIVE");

      // Assert
      assertThat(orgUnitStatus1).isNotEqualTo(orgUnitStatus2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      OrgUnitStatus orgUnitStatus = OrgUnitStatus.of("ACTIVE");

      // Act
      String result = orgUnitStatus.toString();

      // Assert
      assertThat(result).contains("ACTIVE");
    }
  }

  @Nested
  @DisplayName("定数のテスト")
  class ConstantsTest {

    @Test
    @DisplayName("ACTIVE定数が正しく定義されていること")
    void shouldHaveActiveConstant() {
      assertThat(OrgUnitStatus.ACTIVE.value()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("INACTIVE定数が正しく定義されていること")
    void shouldHaveInactiveConstant() {
      assertThat(OrgUnitStatus.INACTIVE.value()).isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("MIN_LENGTHは1であること")
    void shouldHaveMinLengthEqualToOne() {
      assertThat(OrgUnitStatus.MIN_LENGTH).isEqualTo(1);
    }

    @Test
    @DisplayName("MAX_LENGTHは20であること")
    void shouldHaveMaxLengthEqualToTwenty() {
      assertThat(OrgUnitStatus.MAX_LENGTH).isEqualTo(20);
    }
  }
}
