package undecided.association.relationshipMgmt.spi.party.bankAccount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("AccountNumber値オブジェクトのテスト")
class AccountNumberTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な口座番号からAccountNumberを正常に作成すること")
    void shouldCreateAccountNumberFromValidString() {
      // Act
      AccountNumber accountNumber = AccountNumber.of("1234567");

      // Assert
      assertThat(accountNumber).as("AccountNumberインスタンスが作成されること").isNotNull();
      assertThat(accountNumber.value()).as("値が保持されること").isEqualTo("1234567");
    }

    @Test
    @DisplayName("前後に空白がある口座番号をトリムして作成すること")
    void shouldCreateAccountNumberWithTrimmedValue() {
      // Act
      AccountNumber accountNumber = AccountNumber.of(" 1234567 ");

      // Assert
      assertThat(accountNumber.value()).as("前後の空白がトリムされること").isEqualTo("1234567");
    }

    @Test
    @DisplayName("1桁の口座番号も正常に作成すること")
    void shouldCreateAccountNumberWithOneDigit() {
      // Act
      AccountNumber accountNumber = AccountNumber.of("1");

      // Assert
      assertThat(accountNumber.value()).as("1桁の口座番号が作成されること").isEqualTo("1");
    }

    @Test
    @DisplayName("7桁の口座番号も正常に作成すること")
    void shouldCreateAccountNumberWithSevenDigits() {
      // Act
      AccountNumber accountNumber = AccountNumber.of("1234567");

      // Assert
      assertThat(accountNumber.value()).as("7桁の口座番号が作成されること").isEqualTo("1234567");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> AccountNumber.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> AccountNumber.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> AccountNumber.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("8桁以上の口座番号が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberIsTooLong() {
      // Act & Assert
      assertThatThrownBy(() -> AccountNumber.of("12345678"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("桁数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("digits");
    }

    @Test
    @DisplayName("数字以外の文字を含む口座番号が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberContainsNonDigits() {
      // Act & Assert
      assertThatThrownBy(() -> AccountNumber.of("ABCDEFG"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("数字以外が含まれる場合のエラーメッセージが含まれること")
          .hasMessageContaining("digits");
    }

    @Test
    @DisplayName("数字と文字が混在する口座番号が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberContainsMixedCharacters() {
      // Act & Assert
      assertThatThrownBy(() -> AccountNumber.of("123A567"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("数字と文字が混在する場合のエラーメッセージが含まれること")
          .hasMessageContaining("digits");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のAccountNumberインスタンスを返すこと")
    void shouldReturnEmptyInstance() {
      // Act
      AccountNumber accountNumber = AccountNumber.empty();

      // Assert
      assertThat(accountNumber).as("空のインスタンスが返されること").isNotNull();
      assertThat(accountNumber.isEmpty()).as("isEmptyがtrueを返すこと").isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の口座番号値を返すこと")
    void shouldReturnInternalValue() {
      // Arrange
      AccountNumber accountNumber = AccountNumber.of("1234567");

      // Act
      String result = accountNumber.value();

      // Assert
      assertThat(result).as("内部の口座番号値が返されること").isEqualTo("1234567");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のAccountNumberはisEmptyがtrueを返すこと")
    void shouldReturnTrueForEmpty() {
      // Arrange
      AccountNumber accountNumber = AccountNumber.empty();

      // Assert
      assertThat(accountNumber.isEmpty()).as("空の口座番号はisEmptyがtrueを返すこと").isTrue();
    }

    @Test
    @DisplayName("値を持つAccountNumberはisEmptyがfalseを返すこと")
    void shouldReturnFalseForNonEmpty() {
      // Arrange
      AccountNumber accountNumber = AccountNumber.of("1234567");

      // Assert
      assertThat(accountNumber.isEmpty()).as("値を持つ口座番号はisEmptyがfalseを返すこと").isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("口座番号の文字列表現を返すこと")
    void shouldReturnStringRepresentation() {
      // Arrange
      AccountNumber accountNumber = AccountNumber.of("1234567");

      // Act
      String result = accountNumber.toString();

      // Assert
      assertThat(result).as("toString出力に口座番号が含まれること").contains("1234567");
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeメソッドのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のAccountNumberは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      AccountNumber accountNumber1 = AccountNumber.of("1234567");
      AccountNumber accountNumber2 = AccountNumber.of("1234567");

      // Assert
      assertThat(accountNumber1).as("同じ値のAccountNumberは等しいこと").isEqualTo(accountNumber2);
      assertThat(accountNumber1.hashCode())
          .as("同じ値のAccountNumberはhashCodeが等しいこと")
          .isEqualTo(accountNumber2.hashCode());
    }

    @Test
    @DisplayName("異なる値のAccountNumberは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      AccountNumber accountNumber1 = AccountNumber.of("1234567");
      AccountNumber accountNumber2 = AccountNumber.of("7654321");

      // Assert
      assertThat(accountNumber1).as("異なる値のAccountNumberは等しくないこと").isNotEqualTo(accountNumber2);
    }

    @Test
    @DisplayName("nullとの比較はfalseを返すこと")
    void shouldNotBeEqualWithNull() {
      // Arrange
      AccountNumber accountNumber = AccountNumber.of("1234567");

      // Assert
      assertThat(accountNumber).as("nullとの比較はfalseを返すこと").isNotEqualTo(null);
    }

    @Test
    @DisplayName("空のAccountNumber同士は等しいと判定されること")
    void shouldBeEqualWhenBothAreEmpty() {
      // Arrange
      AccountNumber accountNumber1 = AccountNumber.empty();
      AccountNumber accountNumber2 = AccountNumber.empty();

      // Assert
      assertThat(accountNumber1).as("空のAccountNumber同士は等しいこと").isEqualTo(accountNumber2);
    }

    @Test
    @DisplayName("値を持つAccountNumberと空のAccountNumberは等しくないこと")
    void shouldNotBeEqualWhenOneIsEmpty() {
      // Arrange
      AccountNumber accountNumber1 = AccountNumber.of("1234567");
      AccountNumber accountNumber2 = AccountNumber.empty();

      // Assert
      assertThat(accountNumber1)
          .as("値を持つAccountNumberと空のAccountNumberは等しくないこと")
          .isNotEqualTo(accountNumber2);
    }
  }

  @Nested
  @DisplayName("定数のテスト")
  class ConstantsTest {

    @Test
    @DisplayName("MIN_LENGTH定数は1であること")
    void shouldHaveMinLengthEqualToOne() {
      // Assert
      assertThat(AccountNumber.MIN_LENGTH).as("口座番号の最小桁数は1であること").isEqualTo(1);
    }

    @Test
    @DisplayName("MAX_LENGTH定数は7であること")
    void shouldHaveMaxLengthEqualToSeven() {
      // Assert
      assertThat(AccountNumber.MAX_LENGTH).as("口座番号の最大桁数は7であること").isEqualTo(7);
    }
  }
}
