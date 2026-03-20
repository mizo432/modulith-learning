package undecided.shared.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Repeatクラスのテスト")
class RepeatTest {

  @Nested
  @DisplayName("applyメソッドのテスト")
  class ApplyMethodTest {

    @Test
    @DisplayName("正の回数と文字列が渡される場合、文字列が指定された回数繰り返された結果を返す")
    void shouldRepeatStringSpecifiedTimes() {
      // Arrange
      Strings2.Repeat repeat = new Strings2().new Repeat(3);
      String input = "abc";

      // Act
      String result = repeat.apply(input);

      // Assert
      assertThat(result).isEqualTo("abcabcabc");
    }

    @Test
    @DisplayName("回数が1の場合、文字列をそのまま返す")
    void shouldReturnSameStringWhenTimesIsOne() {
      // Arrange
      Strings2.Repeat repeat = new Strings2().new Repeat(1);
      String input = "input";

      // Act
      String result = repeat.apply(input);

      // Assert
      assertThat(result).isEqualTo("input");
    }

    @Test
    @DisplayName("空文字が渡される場合、指定された回数分の空文字を返す")
    void shouldReturnEmptyWhenInputIsEmpty() {
      // Arrange
      Strings2.Repeat repeat = new Strings2().new Repeat(4);
      String input = "";

      // Act
      String result = repeat.apply(input);

      // Assert
      assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("nullが渡される場合、IllegalArgumentExceptionをスローする")
    void shouldThrowIllegalArgumentExceptionWhenInputIsNull() {
      // Arrange
      Strings2.Repeat repeat = new Strings2().new Repeat(2);

      // Act & Assert
      assertThatThrownBy(() -> repeat.apply(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("s must not be null");
    }

    @Test
    @DisplayName("回数が負の場合、IllegalArgumentExceptionをスローする")
    void shouldThrowIllegalArgumentExceptionWhenTimesIsNegative() {
      // Arrange
      Strings2.Repeat repeat = new Strings2().new Repeat(-2);

      // Act & Assert
      assertThatThrownBy(() -> repeat.apply("test"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("times must be positive");
    }

    @Test
    @DisplayName("回数が0の場合、IllegalArgumentExceptionをスローする")
    void shouldThrowIllegalArgumentExceptionWhenTimesIsZero() {
      // Arrange
      Strings2.Repeat repeat = new Strings2().new Repeat(0);

      // Act & Assert
      assertThatThrownBy(() -> repeat.apply("test"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("times must be positive");
    }
  }
}
