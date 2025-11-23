package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("BigDecimalPreconditionのテスト")
class BigDecimalPreconditionTest {

  @Nested
  @DisplayName("checkPositiveメソッドのテスト")
  class CheckPositiveTest {

    @Test
    @DisplayName("正の値の場合、その値が返される")
    void shouldReturnSameBigDecimalWhenValueIsPositive() {
      // Arrange
      BigDecimal input = BigDecimal.valueOf(10.0);
      Supplier<RuntimeException> exceptionSupplier =
          () -> new IllegalArgumentException("Value should be positive");

      // Act
      BigDecimal result = BigDecimalPrecondition.checkPositive(input, exceptionSupplier);

      // Assert
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("負の値の場合、例外がスローされる")
    void shouldThrowExceptionWhenValueIsNegative() {
      // Arrange
      BigDecimal input = BigDecimal.valueOf(-5.0);
      Supplier<RuntimeException> exceptionSupplier =
          () -> new IllegalArgumentException("Value should be positive");

      // Act & Assert
      assertThatThrownBy(() -> BigDecimalPrecondition.checkPositive(input, exceptionSupplier))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Value should be positive");
    }

    @Test
    @DisplayName("ゼロの場合、例外がスローされる")
    void shouldThrowExceptionWhenValueIsZero() {
      // Arrange
      BigDecimal input = BigDecimal.ZERO;
      Supplier<RuntimeException> exceptionSupplier =
          () -> new IllegalArgumentException("Value should be positive");

      // Act & Assert
      assertThatThrownBy(() -> BigDecimalPrecondition.checkPositive(input, exceptionSupplier))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Value should be positive");
    }

    @Test
    @DisplayName("nullの場合、nullが返される")
    void shouldReturnNullWhenValueIsNull() {
      // Arrange
      BigDecimal input = null;
      Supplier<RuntimeException> exceptionSupplier =
          () -> new IllegalArgumentException("Value should be positive");

      // Act
      BigDecimal result = BigDecimalPrecondition.checkPositive(input, exceptionSupplier);

      // Assert
      assertThat(result).isNull();
    }
  }
}
