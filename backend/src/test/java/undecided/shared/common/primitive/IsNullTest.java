package undecided.shared.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IsNullTest {

  @Nested
  @DisplayName("IsNullクラスのtestメソッドを検証するテスト")
  class TestMethod {

    @Test
    @DisplayName("入力がnullの場合、trueを返すべき")
    void shouldReturnTrueWhenInputIsNull() {
      // Arrange
      Objects2.IsNull isNull = new Objects2.IsNull();
      Object input = Objects2.NULL;

      // Act
      boolean result = isNull.test(input);

      // Assert
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("入力がnull以外の場合、falseを返すべき")
    void shouldReturnFalseWhenInputIsNotNull() {
      // Arrange
      Objects2.IsNull isNull = new Objects2.IsNull();
      Object input = new Object();

      // Act
      boolean result = isNull.test(input);

      // Assert
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("入力が明示的にnullの場合、trueを返すべき")
    void shouldReturnTrueWhenInputExplicitlyNull() {
      // Arrange
      Objects2.IsNull isNull = new Objects2.IsNull();
      Object input = null;

      // Act
      boolean result = isNull.test(input);

      // Assert
      assertThat(result).isTrue();
    }
  }
}
