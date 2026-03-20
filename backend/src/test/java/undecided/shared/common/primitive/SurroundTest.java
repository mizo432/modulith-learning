package undecided.shared.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static undecided.shared.common.primitive.Strings2.SURROUND;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Surroundクラスのテスト")
class SurroundTest {

  @Nested
  @DisplayName("applyメソッドのテスト")
  class ApplyMethodTest {

    @Test
    @DisplayName("有効な文字列と囲み文字列を渡すと、指定された囲み文字列で囲まれた結果を返す")
    void shouldReturnStringSurroundedByGivenSurroundString() {
      // Arrange

      String input = "test";
      String surroundString = "*";

      // Act
      String result = SURROUND.apply(input, surroundString);

      // Assert
      assertThat(result).isEqualTo("*test*");
    }

    @Test
    @DisplayName("入力文字列がnullの場合、囲み文字列のみで囲まれた結果を返す")
    void shouldReturnEmptyStringSurroundedByGivenSurroundStringWhenInputIsNull() {
      // Arrange

      String input = null;
      String surroundString = "*";

      // Act
      String result = SURROUND.apply(input, surroundString);

      // Assert
      assertThat(result).isEqualTo("**");
    }

    @Test
    @DisplayName("囲み文字列がnullの場合、入力文字列を空文字で囲んだ結果を返す")
    void shouldReturnStringSurroundedByEmptyWhenSurroundStringIsNull() {
      // Arrange
      String input = "test";
      String surroundString = null;

      // Act
      String result = SURROUND.apply(input, surroundString);

      // Assert
      assertThat(result).isEqualTo("test");
    }

    @Test
    @DisplayName("両方の文字列がnullの場合、空文字列を返す")
    void shouldReturnEmptyStringWhenBothStringsAreNull() {
      // Arrange
      String input = null;
      String surroundString = null;

      // Act
      String result = SURROUND.apply(input, surroundString);

      // Assert
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("入力文字列が空文字の場合、囲み文字列で囲まれた空文字を返す")
    void shouldReturnSurroundedEmptyStringWhenInputIsEmpty() {
      // Arrange
      String input = "";
      String surroundString = "*";

      // Act
      String result = SURROUND.apply(input, surroundString);

      // Assert
      assertThat(result).isEqualTo("**");
    }

    @Test
    @DisplayName("囲み文字列が空文字の場合、入力文字列そのものを返す")
    void shouldReturnInputStringWhenSurroundStringIsEmpty() {
      // Arrange
      String input = "test";
      String surroundString = "";

      // Act
      String result = SURROUND.apply(input, surroundString);

      // Assert
      assertThat(result).isEqualTo("test");
    }
  }
}
