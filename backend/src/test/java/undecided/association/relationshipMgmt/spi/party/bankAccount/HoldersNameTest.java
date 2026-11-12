package undecided.association.relationshipMgmt.spi.party.bankAccount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("HoldersName値オブジェクトのテスト")
class HoldersNameTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な日本語名からHoldersNameを正常に作成すること")
    void shouldCreateHoldersNameFromValidJapaneseName() {
      // Act
      HoldersName holdersName = HoldersName.of("田中太郎");

      // Assert
      assertThat(holdersName).as("HoldersNameインスタンスが作成されること").isNotNull();
      assertThat(holdersName.value()).as("値が保持されること").isEqualTo("田中太郎");
    }

    @Test
    @DisplayName("ひらがなの名前も正常に作成すること")
    void shouldCreateHoldersNameFromHiragana() {
      // Act
      HoldersName holdersName = HoldersName.of("たなかたろう");

      // Assert
      assertThat(holdersName.value()).as("ひらがなの名前が作成されること").isEqualTo("たなかたろう");
    }

    @Test
    @DisplayName("カタカナの名前も正常に作成すること")
    void shouldCreateHoldersNameFromKatakana() {
      // Act
      HoldersName holdersName = HoldersName.of("タナカタロウ");

      // Assert
      assertThat(holdersName.value()).as("カタカナの名前が作成されること").isEqualTo("タナカタロウ");
    }

    @Test
    @DisplayName("半角英数字の名前も正常に作成すること")
    void shouldCreateHoldersNameFromAlphanumeric() {
      // Act
      HoldersName holdersName = HoldersName.of("TANAKA TARO");

      // Assert
      assertThat(holdersName.value()).as("半角英数字の名前が作成されること").isEqualTo("TANAKA TARO");
    }

    @Test
    @DisplayName("前後に空白がある名前をトリムして作成すること")
    void shouldCreateHoldersNameWithTrimmedValue() {
      // Act
      HoldersName holdersName = HoldersName.of(" 田中太郎 ");

      // Assert
      assertThat(holdersName.value()).as("前後の空白がトリムされること").isEqualTo("田中太郎");
    }

    @Test
    @DisplayName("100文字の名前も正常に作成すること")
    void shouldCreateHoldersNameWithMaxLength() {
      // Arrange
      String longName = "あ".repeat(100);

      // Act
      HoldersName holdersName = HoldersName.of(longName);

      // Assert
      assertThat(holdersName.value()).as("100文字の名前が作成されること").isEqualTo(longName);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> HoldersName.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> HoldersName.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> HoldersName.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("101文字以上の名前が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameIsTooLong() {
      // Arrange
      String tooLongName = "あ".repeat(101);

      // Act & Assert
      assertThatThrownBy(() -> HoldersName.of(tooLongName))
          .isInstanceOf(IllegalArgumentException.class)
          .as("文字数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("characters");
    }

    @Test
    @DisplayName("許可されていない文字を含む名前が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameContainsInvalidCharacters() {
      // Act & Assert
      assertThatThrownBy(() -> HoldersName.of("田中@太郎"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("無効文字が含まれる場合のエラーメッセージが含まれること")
          .hasMessageContaining("invalid");
    }

    @Test
    @DisplayName("絵文字を含む名前が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameContainsEmoji() {
      // Act & Assert
      assertThatThrownBy(() -> HoldersName.of("田中🎉太郎"))
          .isInstanceOf(IllegalArgumentException.class)
          .as("絵文字が含まれる場合のエラーメッセージが含まれること")
          .hasMessageContaining("invalid");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のHoldersNameインスタンスを返すこと")
    void shouldReturnEmptyInstance() {
      // Act
      HoldersName holdersName = HoldersName.empty();

      // Assert
      assertThat(holdersName).as("空のインスタンスが返されること").isNotNull();
      assertThat(holdersName.isEmpty()).as("isEmptyがtrueを返すこと").isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の名前値を返すこと")
    void shouldReturnInternalValue() {
      // Arrange
      HoldersName holdersName = HoldersName.of("田中太郎");

      // Act
      String result = holdersName.value();

      // Assert
      assertThat(result).as("内部の名前値が返されること").isEqualTo("田中太郎");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のHoldersNameはisEmptyがtrueを返すこと")
    void shouldReturnTrueForEmpty() {
      // Arrange
      HoldersName holdersName = HoldersName.empty();

      // Assert
      assertThat(holdersName.isEmpty()).as("空の名前はisEmptyがtrueを返すこと").isTrue();
    }

    @Test
    @DisplayName("値を持つHoldersNameはisEmptyがfalseを返すこと")
    void shouldReturnFalseForNonEmpty() {
      // Arrange
      HoldersName holdersName = HoldersName.of("田中太郎");

      // Assert
      assertThat(holdersName.isEmpty()).as("値を持つ名前はisEmptyがfalseを返すこと").isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("名前の文字列表現を返すこと")
    void shouldReturnStringRepresentation() {
      // Arrange
      HoldersName holdersName = HoldersName.of("田中太郎");

      // Act
      String result = holdersName.toString();

      // Assert
      assertThat(result).as("toString出力に名前が含まれること").contains("田中太郎");
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeメソッドのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のHoldersNameは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      HoldersName holdersName1 = HoldersName.of("田中太郎");
      HoldersName holdersName2 = HoldersName.of("田中太郎");

      // Assert
      assertThat(holdersName1).as("同じ値のHoldersNameは等しいこと").isEqualTo(holdersName2);
      assertThat(holdersName1.hashCode())
          .as("同じ値のHoldersNameはhashCodeが等しいこと")
          .isEqualTo(holdersName2.hashCode());
    }

    @Test
    @DisplayName("異なる値のHoldersNameは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      HoldersName holdersName1 = HoldersName.of("田中太郎");
      HoldersName holdersName2 = HoldersName.of("鈴木花子");

      // Assert
      assertThat(holdersName1).as("異なる値のHoldersNameは等しくないこと").isNotEqualTo(holdersName2);
    }

    @Test
    @DisplayName("nullとの比較はfalseを返すこと")
    void shouldNotBeEqualWithNull() {
      // Arrange
      HoldersName holdersName = HoldersName.of("田中太郎");

      // Assert
      assertThat(holdersName).as("nullとの比較はfalseを返すこと").isNotEqualTo(null);
    }

    @Test
    @DisplayName("空のHoldersName同士は等しいと判定されること")
    void shouldBeEqualWhenBothAreEmpty() {
      // Arrange
      HoldersName holdersName1 = HoldersName.empty();
      HoldersName holdersName2 = HoldersName.empty();

      // Assert
      assertThat(holdersName1).as("空のHoldersName同士は等しいこと").isEqualTo(holdersName2);
    }

    @Test
    @DisplayName("値を持つHoldersNameと空のHoldersNameは等しくないこと")
    void shouldNotBeEqualWhenOneIsEmpty() {
      // Arrange
      HoldersName holdersName1 = HoldersName.of("田中太郎");
      HoldersName holdersName2 = HoldersName.empty();

      // Assert
      assertThat(holdersName1)
          .as("値を持つHoldersNameと空のHoldersNameは等しくないこと")
          .isNotEqualTo(holdersName2);
    }
  }

  @Nested
  @DisplayName("定数のテスト")
  class ConstantsTest {

    @Test
    @DisplayName("MAX_LENGTH定数は100であること")
    void shouldHaveMaxLengthEqualToHundred() {
      // Assert
      assertThat(HoldersName.MAX_LENGTH).as("口座名義人の最大文字数は100であること").isEqualTo(100);
    }
  }
}
