package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.*;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("整数の事前条件に関するテスト")
class IntegerPreconditionTest {

  @Nested
  @DisplayName("checkRangeOpenClosedメソッドのテスト")
  class CheckRangeOpenClosed {

    @Test
    @DisplayName("最小値を除外し、最大値を含む範囲の整数を許可するべき")
    void shouldAllowIntegerInRange() {
      // Arrange
      int min = 10;
      int max = 20;
      int validValue = 15;
      Supplier<RuntimeException> exceptionSupplier =
          () -> new IllegalArgumentException("Out of range");

      // Act
      Integer result =
          IntegerPrecondition.checkRangeOpenClosed(validValue, exceptionSupplier, min, max);

      // Assert
      assertThat(result).isEqualTo(validValue);
    }

    @Test
    @DisplayName("範囲外の整数が例外をスローするべき")
    void shouldThrowExceptionForOutOfRangeInteger() {
      // Arrange
      int min = 10;
      int max = 20;
      int invalidValue = 25;
      Supplier<RuntimeException> exceptionSupplier =
          () -> new IllegalArgumentException("Out of range");

      // Act & Assert
      assertThatThrownBy(
              () ->
                  IntegerPrecondition.checkRangeOpenClosed(
                      invalidValue, exceptionSupplier, min, max))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Out of range");
    }

    @Test
    @DisplayName("nullの入力はnullをそのまま返すべき")
    void shouldReturnNullForNullInput() {
      // Arrange
      Integer input = null;
      Supplier<RuntimeException> exceptionSupplier =
          () -> new IllegalArgumentException("Out of range");
      int min = 10;
      int max = 20;

      // Act
      Integer result = IntegerPrecondition.checkRangeOpenClosed(input, exceptionSupplier, min, max);

      // Assert
      assertThat(result).isNull();
    }
  }
}
