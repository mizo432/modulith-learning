package undecided.generic.bankReg.spi.branch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("BranchCode値オブジェクトのテスト")
class BranchCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な3桁のコードからBranchCodeを正常に作成すること")
    void shouldCreateBranchCodeFromValidThreeDigitString() {
      // Act
      BranchCode branchCode = BranchCode.of("001");

      // Assert
      assertThat(branchCode).as("BranchCodeインスタンスが作成されること").isNotNull();
      assertThat(branchCode.value()).as("値がトリムされた状態で保持されること").isEqualTo("001");
    }

    @Test
    @DisplayName("前後に空白がある3桁のコードをトリムして作成すること")
    void shouldCreateBranchCodeWithTrimmedValue() {
      // Act
      BranchCode branchCode = BranchCode.of(" 001 ");

      // Assert
      assertThat(branchCode.value()).as("前後の空白がトリムされること").isEqualTo("001");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> BranchCode.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> BranchCode.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> BranchCode.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("2桁のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooShort() {
      // Act & Assert
      assertThatThrownBy(() -> BranchCode.of("01"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("桁数が不足している場合のエラーメッセージが含まれること")
          .hasMessageContaining("3 digits");
    }

    @Test
    @DisplayName("4桁のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Act & Assert
      assertThatThrownBy(() -> BranchCode.of("0001"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("桁数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("3 digits");
    }

    @Test
    @DisplayName("数字以外の文字を含む3桁のコードは作成できること")
    void shouldCreateBranchCodeWithNonDigitCharacters() {
      // Act
      BranchCode branchCode = BranchCode.of("ABC");

      // Assert
      assertThat(branchCode.value()).as("数字以外でも3桁であれば作成できること").isEqualTo("ABC");
    }
  }

  @Nested
  @DisplayName("asStringメソッドのテスト")
  class AsStringMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      BranchCode branchCode = BranchCode.of("001");

      // Act
      String result = branchCode.asString();

      // Assert
      assertThat(result).as("内部の文字列値が返されること").isEqualTo("001");
    }
  }

  @Nested
  @DisplayName("recordの標準メソッドのテスト")
  class RecordStandardMethodsTest {

    @Test
    @DisplayName("同じ値のBranchCodeは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      BranchCode branchCode1 = BranchCode.of("001");
      BranchCode branchCode2 = BranchCode.of("001");

      // Assert
      assertThat(branchCode1).as("同じ値のBranchCodeは等しいこと").isEqualTo(branchCode2);
    }

    @Test
    @DisplayName("異なる値のBranchCodeは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      BranchCode branchCode1 = BranchCode.of("001");
      BranchCode branchCode2 = BranchCode.of("002");

      // Assert
      assertThat(branchCode1).as("異なる値のBranchCodeは等しくないこと").isNotEqualTo(branchCode2);
    }

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      BranchCode branchCode = BranchCode.of("001");

      // Act
      String result = branchCode.toString();

      // Assert
      assertThat(result).as("toString出力に値が含まれること").contains("001");
    }
  }

  @Nested
  @DisplayName("LENGTH定数のテスト")
  class LengthConstantTest {

    @Test
    @DisplayName("LENGTH定数は3であること")
    void shouldHaveLengthEqualToThree() {
      // Assert
      assertThat(BranchCode.LENGTH).as("支店コードの桁数は3であること").isEqualTo(3);
    }
  }
}
