package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.*;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("整数の事前条件に関するテスト")
class IntegerPreconditionTest {

  private static final Supplier<RuntimeException> EXCEPTION_SUPPLIER = RuntimeException::new;
  private static final Supplier<RuntimeException> CUSTOM_EXCEPTION_SUPPLIER =
      () -> new IllegalArgumentException("Out of range");

  @Nested
  @DisplayName("checkPositiveメソッドのテスト")
  class CheckPositiveTest {

    @Test
    @DisplayName("正の値の場合、その値を返すべき")
    void shouldReturnValueWhenPositive() {
      // Arrange
      Integer positiveValue = 5;

      // Act
      Integer result = IntegerPrecondition.checkPositive(positiveValue, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(positiveValue);
    }

    @Test
    @DisplayName("ゼロの場合、例外をスローするべき")
    void shouldThrowExceptionWhenZero() {
      // Arrange
      Integer zero = 0;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkPositive(zero, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("負の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenNegative() {
      // Arrange
      Integer negativeValue = -5;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkPositive(negativeValue, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      Integer result = IntegerPrecondition.checkPositive(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MAX_VALUEの場合、その値を返すべき")
    void shouldReturnValueWhenMaxValue() {
      // Arrange
      Integer maxValue = Integer.MAX_VALUE;

      // Act
      Integer result = IntegerPrecondition.checkPositive(maxValue, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(maxValue);
    }
  }

  @Nested
  @DisplayName("checkNonNegativeメソッドのテスト")
  class CheckNonNegativeTest {

    @Test
    @DisplayName("正の値の場合、その値を返すべき")
    void shouldReturnValueWhenPositive() {
      // Arrange
      Integer positiveValue = 5;

      // Act
      Integer result = IntegerPrecondition.checkNonNegative(positiveValue, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(positiveValue);
    }

    @Test
    @DisplayName("ゼロの場合、その値を返すべき")
    void shouldReturnValueWhenZero() {
      // Arrange
      Integer zero = 0;

      // Act
      Integer result = IntegerPrecondition.checkNonNegative(zero, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(zero);
    }

    @Test
    @DisplayName("負の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenNegative() {
      // Arrange
      Integer negativeValue = -1;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkNonNegative(negativeValue, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      Integer result = IntegerPrecondition.checkNonNegative(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MIN_VALUEの場合、例外をスローするべき")
    void shouldThrowExceptionWhenMinValue() {
      // Arrange
      Integer minValue = Integer.MIN_VALUE;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkNonNegative(minValue, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }
  }

  @Nested
  @DisplayName("checkNegativeメソッドのテスト")
  class CheckNegativeTest {

    @Test
    @DisplayName("負の値の場合、その値を返すべき")
    void shouldReturnValueWhenNegative() {
      // Arrange
      Integer negativeValue = -5;

      // Act
      Integer result = IntegerPrecondition.checkNegative(negativeValue, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(negativeValue);
    }

    @Test
    @DisplayName("ゼロの場合、例外をスローするべき")
    void shouldThrowExceptionWhenZero() {
      // Arrange
      Integer zero = 0;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkNegative(zero, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("正の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenPositive() {
      // Arrange
      Integer positiveValue = 5;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkNegative(positiveValue, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      Integer result = IntegerPrecondition.checkNegative(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MIN_VALUEの場合、その値を返すべき")
    void shouldReturnValueWhenMinValue() {
      // Arrange
      Integer minValue = Integer.MIN_VALUE;

      // Act
      Integer result = IntegerPrecondition.checkNegative(minValue, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(minValue);
    }
  }

  @Nested
  @DisplayName("checkNegativeOrZeroメソッドのテスト")
  class CheckNegativeOrZeroTest {

    @Test
    @DisplayName("負の値の場合、その値を返すべき")
    void shouldReturnValueWhenNegative() {
      // Arrange
      Integer negativeValue = -5;

      // Act
      Integer result = IntegerPrecondition.checkNegativeOrZero(negativeValue, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(negativeValue);
    }

    @Test
    @DisplayName("ゼロの場合、その値を返すべき")
    void shouldReturnValueWhenZero() {
      // Arrange
      Integer zero = 0;

      // Act
      Integer result = IntegerPrecondition.checkNegativeOrZero(zero, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(zero);
    }

    @Test
    @DisplayName("正の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenPositive() {
      // Arrange
      Integer positiveValue = 1;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkNegativeOrZero(positiveValue, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      Integer result = IntegerPrecondition.checkNegativeOrZero(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("checkRangeClosedメソッドのテスト")
  class CheckRangeClosedTest {

    @Test
    @DisplayName("範囲内の値の場合、その値を返すべき")
    void shouldReturnValueWhenInRange() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 15;

      // Act
      Integer result = IntegerPrecondition.checkRangeClosed(value, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 10;

      // Act
      Integer result = IntegerPrecondition.checkRangeClosed(value, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最大値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 20;

      // Act
      Integer result = IntegerPrecondition.checkRangeClosed(value, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 9;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeClosed(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 21;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeClosed(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Arrange
      int min = 10;
      int max = 20;

      // Act
      Integer result = IntegerPrecondition.checkRangeClosed(null, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("checkRangeOpenメソッドのテスト")
  class CheckRangeOpenTest {

    @Test
    @DisplayName("範囲内の値の場合、その値を返すべき")
    void shouldReturnValueWhenInRange() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 15;

      // Act
      Integer result = IntegerPrecondition.checkRangeOpen(value, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 10;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeOpen(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 20;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeOpen(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 9;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeOpen(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 21;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeOpen(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Arrange
      int min = 10;
      int max = 20;

      // Act
      Integer result = IntegerPrecondition.checkRangeOpen(null, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("checkRangeClosedOpenメソッドのテスト")
  class CheckRangeClosedOpenTest {

    @Test
    @DisplayName("範囲内の値の場合、その値を返すべき")
    void shouldReturnValueWhenInRange() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 15;

      // Act
      Integer result =
          IntegerPrecondition.checkRangeClosedOpen(value, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 10;

      // Act
      Integer result =
          IntegerPrecondition.checkRangeClosedOpen(value, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最大値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 20;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeClosedOpen(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 9;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeClosedOpen(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 21;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeClosedOpen(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Arrange
      int min = 10;
      int max = 20;

      // Act
      Integer result =
          IntegerPrecondition.checkRangeClosedOpen(null, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("checkRangeOpenClosedメソッドのテスト")
  class CheckRangeOpenClosedTest {

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
      Integer result =
          IntegerPrecondition.checkRangeOpenClosed(input, exceptionSupplier, min, max);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("最小値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 10;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeOpenClosed(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 20;

      // Act
      Integer result =
          IntegerPrecondition.checkRangeOpenClosed(value, EXCEPTION_SUPPLIER, min, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 9;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeOpenClosed(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 21;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeOpenClosed(value, EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(RuntimeException.class);
    }
  }

  @Nested
  @DisplayName("checkAtLeastメソッドのテスト")
  class CheckAtLeastTest {

    @Test
    @DisplayName("最小値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMin() {
      // Arrange
      int min = 10;
      Integer value = 10;

      // Act
      Integer result = IntegerPrecondition.checkAtLeast(value, EXCEPTION_SUPPLIER, min);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値より大きい場合、その値を返すべき")
    void shouldReturnValueWhenGreaterThanMin() {
      // Arrange
      int min = 10;
      Integer value = 15;

      // Act
      Integer result = IntegerPrecondition.checkAtLeast(value, EXCEPTION_SUPPLIER, min);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      int min = 10;
      Integer value = 9;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkAtLeast(value, EXCEPTION_SUPPLIER, min))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Arrange
      int min = 10;

      // Act
      Integer result = IntegerPrecondition.checkAtLeast(null, EXCEPTION_SUPPLIER, min);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MAX_VALUEが最小値の場合、MAX_VALUEを返すべき")
    void shouldReturnMaxValueWhenMinIsMaxValue() {
      // Arrange
      int min = Integer.MAX_VALUE;
      Integer value = Integer.MAX_VALUE;

      // Act
      Integer result = IntegerPrecondition.checkAtLeast(value, EXCEPTION_SUPPLIER, min);

      // Assert
      assertThat(result).isEqualTo(value);
    }
  }

  @Nested
  @DisplayName("checkAtMostメソッドのテスト")
  class CheckAtMostTest {

    @Test
    @DisplayName("最大値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMax() {
      // Arrange
      int max = 20;
      Integer value = 20;

      // Act
      Integer result = IntegerPrecondition.checkAtMost(value, EXCEPTION_SUPPLIER, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最大値未満の場合、その値を返すべき")
    void shouldReturnValueWhenLessThanMax() {
      // Arrange
      int max = 20;
      Integer value = 15;

      // Act
      Integer result = IntegerPrecondition.checkAtMost(value, EXCEPTION_SUPPLIER, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      int max = 20;
      Integer value = 21;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkAtMost(value, EXCEPTION_SUPPLIER, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Arrange
      int max = 20;

      // Act
      Integer result = IntegerPrecondition.checkAtMost(null, EXCEPTION_SUPPLIER, max);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MIN_VALUEが最大値の場合、MIN_VALUEを返すべき")
    void shouldReturnMinValueWhenMaxIsMinValue() {
      // Arrange
      int max = Integer.MIN_VALUE;
      Integer value = Integer.MIN_VALUE;

      // Act
      Integer result = IntegerPrecondition.checkAtMost(value, EXCEPTION_SUPPLIER, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }
  }

  @Nested
  @DisplayName("checkLessThanメソッドのテスト")
  class CheckLessThanTest {

    @Test
    @DisplayName("最大値未満の場合、その値を返すべき")
    void shouldReturnValueWhenLessThanMax() {
      // Arrange
      int max = 20;
      Integer value = 15;

      // Act
      Integer result = IntegerPrecondition.checkLessThan(value, EXCEPTION_SUPPLIER, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最大値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMax() {
      // Arrange
      int max = 20;
      Integer value = 20;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkLessThan(value, EXCEPTION_SUPPLIER, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      int max = 20;
      Integer value = 21;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkLessThan(value, EXCEPTION_SUPPLIER, max))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Arrange
      int max = 20;

      // Act
      Integer result = IntegerPrecondition.checkLessThan(null, EXCEPTION_SUPPLIER, max);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MIN_VALUEの場合、その値を返すべき")
    void shouldReturnMinValueWhenLessThanMax() {
      // Arrange
      int max = 0;
      Integer value = Integer.MIN_VALUE;

      // Act
      Integer result = IntegerPrecondition.checkLessThan(value, EXCEPTION_SUPPLIER, max);

      // Assert
      assertThat(result).isEqualTo(value);
    }
  }

  @Nested
  @DisplayName("checkGreaterThanメソッドのテスト")
  class CheckGreaterThanTest {

    @Test
    @DisplayName("最小値より大きい場合、その値を返すべき")
    void shouldReturnValueWhenGreaterThanMin() {
      // Arrange
      int min = 10;
      Integer value = 15;

      // Act
      Integer result = IntegerPrecondition.checkGreaterThan(value, EXCEPTION_SUPPLIER, min);

      // Assert
      assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("最小値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMin() {
      // Arrange
      int min = 10;
      Integer value = 10;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkGreaterThan(value, EXCEPTION_SUPPLIER, min))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      int min = 10;
      Integer value = 9;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkGreaterThan(value, EXCEPTION_SUPPLIER, min))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Arrange
      int min = 10;

      // Act
      Integer result = IntegerPrecondition.checkGreaterThan(null, EXCEPTION_SUPPLIER, min);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MAX_VALUEの場合、その値を返すべき")
    void shouldReturnMaxValueWhenGreaterThanMin() {
      // Arrange
      int min = 0;
      Integer value = Integer.MAX_VALUE;

      // Act
      Integer result = IntegerPrecondition.checkGreaterThan(value, EXCEPTION_SUPPLIER, min);

      // Assert
      assertThat(result).isEqualTo(value);
    }
  }

  @Nested
  @DisplayName("カスタム例外のテスト")
  class CustomExceptionTest {

    @Test
    @DisplayName("カスタム例外が正しくスローされるべき")
    void shouldThrowCustomException() {
      // Arrange
      Integer value = -1;

      // Act & Assert
      assertThatThrownBy(() -> IntegerPrecondition.checkPositive(value, CUSTOM_EXCEPTION_SUPPLIER))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Out of range");
    }

    @Test
    @DisplayName("範囲チェックでカスタム例外が正しくスローされるべき")
    void shouldThrowCustomExceptionForRangeCheck() {
      // Arrange
      int min = 10;
      int max = 20;
      Integer value = 25;

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerPrecondition.checkRangeClosed(value, CUSTOM_EXCEPTION_SUPPLIER, min, max))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Out of range");
    }
  }
}
