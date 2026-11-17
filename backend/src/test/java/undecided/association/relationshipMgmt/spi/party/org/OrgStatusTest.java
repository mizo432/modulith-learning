package undecided.association.relationshipMgmt.spi.party.org;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("OrgStatus値オブジェクトのテスト")
class OrgStatusTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効なコードからOrgStatusを正常に作成すること")
    void shouldCreateOrgStatusFromValidString() {
      // Act
      OrgStatus orgStatus = OrgStatus.of("ACTIVE");

      // Assert
      assertThat(orgStatus).isNotNull();
      assertThat(orgStatus.value()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("前後に空白があるコードをトリムして作成すること")
    void shouldCreateOrgStatusWithTrimmedValue() {
      // Act
      OrgStatus orgStatus = OrgStatus.of(" ACTIVE ");

      // Assert
      assertThat(orgStatus.value()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> OrgStatus.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> OrgStatus.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> OrgStatus.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("21文字のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Arrange
      String tooLong = "ABCDEFGHIJKLMNOPQRSTU";

      // Act & Assert
      assertThatThrownBy(() -> OrgStatus.of(tooLong))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("20");
    }

    @Test
    @DisplayName("英小文字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsLowercase() {
      // Act & Assert
      assertThatThrownBy(() -> OrgStatus.of("active"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("uppercase");
    }

    @Test
    @DisplayName("英大文字とアンダースコアのみのコードは作成できること")
    void shouldCreateOrgStatusWithUppercaseAndUnderscore() {
      // Act
      OrgStatus orgStatus = OrgStatus.of("UNDER_REVIEW");

      // Assert
      assertThat(orgStatus.value()).isEqualTo("UNDER_REVIEW");
    }

    @Test
    @DisplayName("1文字のコードは作成できること")
    void shouldCreateOrgStatusWithSingleCharacter() {
      // Act
      OrgStatus orgStatus = OrgStatus.of("A");

      // Assert
      assertThat(orgStatus.value()).isEqualTo("A");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のOrgStatusインスタンスを作成すること")
    void shouldCreateEmptyOrgStatus() {
      // Act
      OrgStatus orgStatus = OrgStatus.empty();

      // Assert
      assertThat(orgStatus).isNotNull();
      assertThat(orgStatus.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      OrgStatus orgStatus = OrgStatus.of("ACTIVE");

      // Act
      String result = orgStatus.value();

      // Assert
      assertThat(result).isEqualTo("ACTIVE");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のOrgStatusはtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      OrgStatus orgStatus = OrgStatus.empty();

      // Assert
      assertThat(orgStatus.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つOrgStatusはfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      OrgStatus orgStatus = OrgStatus.of("ACTIVE");

      // Assert
      assertThat(orgStatus.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のOrgStatusは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      OrgStatus orgStatus1 = OrgStatus.of("ACTIVE");
      OrgStatus orgStatus2 = OrgStatus.of("ACTIVE");

      // Assert
      assertThat(orgStatus1).isEqualTo(orgStatus2);
    }

    @Test
    @DisplayName("異なる値のOrgStatusは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      OrgStatus orgStatus1 = OrgStatus.of("ACTIVE");
      OrgStatus orgStatus2 = OrgStatus.of("INACTIVE");

      // Assert
      assertThat(orgStatus1).isNotEqualTo(orgStatus2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      OrgStatus orgStatus = OrgStatus.of("ACTIVE");

      // Act
      String result = orgStatus.toString();

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
      assertThat(OrgStatus.ACTIVE.value()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("INACTIVE定数が正しく定義されていること")
    void shouldHaveInactiveConstant() {
      assertThat(OrgStatus.INACTIVE.value()).isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("MIN_LENGTHは1であること")
    void shouldHaveMinLengthEqualToOne() {
      assertThat(OrgStatus.MIN_LENGTH).isEqualTo(1);
    }

    @Test
    @DisplayName("MAX_LENGTHは20であること")
    void shouldHaveMaxLengthEqualToTwenty() {
      assertThat(OrgStatus.MAX_LENGTH).isEqualTo(20);
    }
  }
}
