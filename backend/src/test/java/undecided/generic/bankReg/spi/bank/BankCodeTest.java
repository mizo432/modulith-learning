package undecided.generic.bankReg.spi.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("BankCode値オブジェクトのテスト")
class BankCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な4桁のコードからBankCodeを正常に作成すること")
    void shouldCreateBankCodeFromValidFourDigitString() {
      // Act
      BankCode bankCode = BankCode.of("0001");

      // Assert
      assertThat(bankCode).as("BankCodeインスタンスが作成されること").isNotNull();
      assertThat(bankCode.value()).as("値がトリムされた状態で保持されること").isEqualTo("0001");
    }

    @Test
    @DisplayName("前後に空白がある4桁のコードをトリムして作成すること")
    void shouldCreateBankCodeWithTrimmedValue() {
      // Act
      BankCode bankCode = BankCode.of(" 0001 ");

      // Assert
      assertThat(bankCode.value()).as("前後の空白がトリムされること").isEqualTo("0001");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> BankCode.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> BankCode.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> BankCode.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("3桁のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooShort() {
      // Act & Assert
      assertThatThrownBy(() -> BankCode.of("001"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("桁数が不足している場合のエラーメッセージが含まれること")
          .hasMessageContaining("4 digits");
    }

    @Test
    @DisplayName("5桁のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Act & Assert
      assertThatThrownBy(() -> BankCode.of("00001"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("桁数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("4 digits");
    }

    @Test
    @DisplayName("数字以外の文字を含む4桁のコードは作成できること")
    void shouldCreateBankCodeWithNonDigitCharacters() {
      // Act
      BankCode bankCode = BankCode.of("ABCD");

      // Assert
      assertThat(bankCode.value()).as("数字以外でも4桁であれば作成できること").isEqualTo("ABCD");
    }
  }

  @Nested
  @DisplayName("asStringメソッドのテスト")
  class AsStringMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      BankCode bankCode = BankCode.of("0001");

      // Act
      String result = bankCode.asString();

      // Assert
      assertThat(result).as("内部の文字列値が返されること").isEqualTo("0001");
    }
  }

  @Nested
  @DisplayName("recordの標準メソッドのテスト")
  class RecordStandardMethodsTest {

    @Test
    @DisplayName("同じ値のBankCodeは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      BankCode bankCode1 = BankCode.of("0001");
      BankCode bankCode2 = BankCode.of("0001");

      // Assert
      assertThat(bankCode1).as("同じ値のBankCodeは等しいこと").isEqualTo(bankCode2);
    }

    @Test
    @DisplayName("異なる値のBankCodeは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      BankCode bankCode1 = BankCode.of("0001");
      BankCode bankCode2 = BankCode.of("0002");

      // Assert
      assertThat(bankCode1).as("異なる値のBankCodeは等しくないこと").isNotEqualTo(bankCode2);
    }

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      BankCode bankCode = BankCode.of("0001");

      // Act
      String result = bankCode.toString();

      // Assert
      assertThat(result).as("toString出力に値が含まれること").contains("0001");
    }
  }

  @Nested
  @DisplayName("LENGTH定数のテスト")
  class LengthConstantTest {

    @Test
    @DisplayName("LENGTH定数は4であること")
    void shouldHaveLengthEqualToFour() {
      // Assert
      assertThat(BankCode.LENGTH).as("金融機関コードの桁数は4であること").isEqualTo(4);
    }
  }
}
