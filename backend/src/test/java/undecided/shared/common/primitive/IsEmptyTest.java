package undecided.shared.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@code IsEmptyTest} クラスは、{@link Strings2.IsEmpty#test(String)} メソッドの動作を確認するためのテストクラスです。
 * このテストでは、入力値が空文字列または {@code null} の場合に期待どおりの出力を生成するかどうかを検証します。
 */
class IsEmptyTest {

  @Nested
  @DisplayName("IsEmptyクラス testメソッドのテスト")
  class TestMethod {

    @Test
    @DisplayName("入力がnullの場合trueを返す")
    void shouldReturnTrueWhenInputIsNull() {
      // Arrange
      Strings2.IsEmpty isEmpty = new Strings2.IsEmpty();

      // Act
      boolean result = isEmpty.test(null);

      // Assert
      assertThat(result).as("nullをテストするとtrueが返ることを期待").isTrue();
    }

    @Test
    @DisplayName("入力が空文字の場合trueを返す")
    void shouldReturnTrueWhenInputIsEmptyString() {
      // Arrange
      Strings2.IsEmpty isEmpty = new Strings2.IsEmpty();
      String input = "";

      // Act
      boolean result = isEmpty.test(input);

      // Assert
      assertThat(result).as("空文字をテストするとtrueが返ることを期待").isTrue();
    }

    @Test
    @DisplayName("入力が空白文字のみの場合falseを返す")
    void shouldReturnFalseWhenInputIsWhiteSpaces() {
      // Arrange
      Strings2.IsEmpty isEmpty = new Strings2.IsEmpty();
      String input = "   ";

      // Act
      boolean result = isEmpty.test(input);

      // Assert
      assertThat(result).as("空白文字のみの文字列をテストするとfalseが返ることを期待").isFalse();
    }

    @Test
    @DisplayName("入力が文字列の場合falseを返す")
    void shouldReturnFalseWhenInputIsNonEmptyString() {
      // Arrange
      Strings2.IsEmpty isEmpty = new Strings2.IsEmpty();
      String input = "test";

      // Act
      boolean result = isEmpty.test(input);

      // Assert
      assertThat(result).as("非空文字列をテストするとfalseが返ることを期待").isFalse();
    }
  }
}
