package undecided.shared.common.primitiveOld;

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

    @Test
    @DisplayName("引数がInteger.MAX_VALUEの場合、Integer.MAX_VALUEを返す")
    void shouldReturnMaxValueWhenInputEqualsMaxInt() {
      // Arrange
      long input = Integer.MAX_VALUE;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("引数がInteger.MIN_VALUEの場合、Integer.MIN_VALUEを返す")
    void shouldReturnMinValueWhenInputEqualsMinInt() {
      // Arrange
      long input = Integer.MIN_VALUE;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("引数が0の場合、0を返す")
    void shouldReturnZeroWhenInputIsZero() {
      // Arrange
      long input = 0L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("引数が正の値の場合、そのままの値を返す")
    void shouldReturnSameValueWhenPositive() {
      // Arrange
      long input = 42L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(42);
    }

    @Test
    @DisplayName("引数が負の値の場合、そのままの値を返す")
    void shouldReturnSameValueWhenNegative() {
      // Arrange
      long input = -42L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(-42);
    }

    @Test
    @DisplayName("引数がLong.MAX_VALUEの場合、Integer.MAX_VALUEを返す")
    void shouldReturnMaxIntWhenInputIsLongMaxValue() {
      // Arrange
      long input = Long.MAX_VALUE;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("引数がLong.MIN_VALUEの場合、Integer.MIN_VALUEを返す")
    void shouldReturnMinIntWhenInputIsLongMinValue() {
      // Arrange
      long input = Long.MIN_VALUE;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("Integer範囲の境界値付近の正の値を正しく処理する")
    void shouldHandleBoundaryPositiveValues() {
      // Arrange
      long justBelowMax = (long) Integer.MAX_VALUE - 1;
      long justAboveMax = (long) Integer.MAX_VALUE + 1;

      // Act
      int resultBelowMax = Ints.saturatedCast(justBelowMax);
      int resultAboveMax = Ints.saturatedCast(justAboveMax);

      // Assert
      assertThat(resultBelowMax).isEqualTo(Integer.MAX_VALUE - 1);
      assertThat(resultAboveMax).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Integer範囲の境界値付近の負の値を正しく処理する")
    void shouldHandleBoundaryNegativeValues() {
      // Arrange
      long justAboveMin = (long) Integer.MIN_VALUE + 1;
      long justBelowMin = (long) Integer.MIN_VALUE - 1;

      // Act
      int resultAboveMin = Ints.saturatedCast(justAboveMin);
      int resultBelowMin = Ints.saturatedCast(justBelowMin);

      // Assert
      assertThat(resultAboveMin).isEqualTo(Integer.MIN_VALUE + 1);
      assertThat(resultBelowMin).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("非常に大きな正の値の場合、Integer.MAX_VALUEを返す")
    void shouldSaturateToMaxIntForVeryLargePositiveValue() {
      // Arrange
      long input = 10_000_000_000L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("非常に小さな負の値の場合、Integer.MIN_VALUEを返す")
    void shouldSaturateToMinIntForVeryLargeNegativeValue() {
      // Arrange
      long input = -10_000_000_000L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("1の場合、1を返す")
    void shouldReturnOneWhenInputIsOne() {
      // Arrange
      long input = 1L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("-1の場合、-1を返す")
    void shouldReturnMinusOneWhenInputIsMinusOne() {
      // Arrange
      long input = -1L;

      // Act
      int result = Ints.saturatedCast(input);

      // Assert
      assertThat(result).isEqualTo(-1);
    }
  }
}
