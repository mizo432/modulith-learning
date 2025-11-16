package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Intsのテスト")
class IntsTest {

  @Nested
  @DisplayName("saturatedCastメソッドのテスト")
  class SaturatedCastTests {

    @Test
    @DisplayName("引数がInteger.MAX_VALUEを超える場合、Integer.MAX_VALUEを返す")
    void shouldReturnMaxValueWhenInputExceedsMaxInt() {
      // Arrange
      long input = (long) Integer.MAX_VALUE + 1;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("引数がInteger.MIN_VALUE未満の場合、Integer.MIN_VALUEを返す")
    void shouldReturnMinValueWhenInputBelowMinInt() {
      // Arrange
      long input = (long) Integer.MIN_VALUE - 1;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("引数がint範囲内の場合、そのままの値を返す")
    void shouldReturnSameValueWhenInputWithinIntRange() {
      // Arrange
      long input = 123L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(123);
    }
  }
}
