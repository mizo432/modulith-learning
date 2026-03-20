package undecided.erp.common.entity;

import static org.assertj.core.api.Assertions.*;
import static undecided.shared.common.primitiveOld.Objects2.isNull;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("IntegerValueのテスト")
class IntegerValueTest {

  private static final Supplier<RuntimeException> EXCEPTION_SUPPLIER = RuntimeException::new;
  private static final Supplier<RuntimeException> CUSTOM_EXCEPTION_SUPPLIER =
      () -> new IllegalArgumentException("Invalid value");

  // テスト用のIntegerValue実装
  private static class TestIntegerValue implements IntegerValue<TestIntegerValue> {
    private final Integer value;

    public TestIntegerValue(Integer value) {
      this.value = value;
    }

    @Override
    public Integer value() {
      return value;
    }

    @Override
    public boolean isEmpty() {
      return isNull(value);
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkPositiveメソッドのテスト")
  class CheckPositiveTest {

    @Test
    @DisplayName("正の値の場合、その値を返すべき")
    void shouldReturnValueWhenPositive() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(5);

      // Act
      TestIntegerValue result = IntegerValue.IntegerValues.checkPositive(value, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isEqualTo(value);
      assertThat(result.value()).isEqualTo(5);
    }

    @Test
    @DisplayName("ゼロの場合、例外をスローするべき")
    void shouldThrowExceptionWhenZero() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(0);

      // Act & Assert
      assertThatThrownBy(() -> IntegerValue.IntegerValues.checkPositive(value, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("負の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenNegative() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(-5);

      // Act & Assert
      assertThatThrownBy(() -> IntegerValue.IntegerValues.checkPositive(value, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result = IntegerValue.IntegerValues.checkPositive(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Integer.MAX_VALUEの場合、その値を返すべき")
    void shouldReturnValueWhenMaxValue() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(Integer.MAX_VALUE);

      // Act
      TestIntegerValue result = IntegerValue.IntegerValues.checkPositive(value, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result.value()).isEqualTo(Integer.MAX_VALUE);
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkNonNegativeメソッドのテスト")
  class CheckNonNegativeTest {

    @Test
    @DisplayName("正の値の場合、その値を返すべき")
    void shouldReturnValueWhenPositive() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(5);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkNonNegative(value, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result.value()).isEqualTo(5);
    }

    @Test
    @DisplayName("ゼロの場合、その値を返すべき")
    void shouldReturnValueWhenZero() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(0);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkNonNegative(value, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result.value()).isEqualTo(0);
    }

    @Test
    @DisplayName("負の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenNegative() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(-1);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkNonNegative(value, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkNonNegative(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkNegativeメソッドのテスト")
  class CheckNegativeTest {

    @Test
    @DisplayName("負の値の場合、その値を返すべき")
    void shouldReturnValueWhenNegative() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(-5);

      // Act
      TestIntegerValue result = IntegerValue.IntegerValues.checkNegative(value, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result.value()).isEqualTo(-5);
    }

    @Test
    @DisplayName("ゼロの場合、例外をスローするべき")
    void shouldThrowExceptionWhenZero() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(0);

      // Act & Assert
      assertThatThrownBy(() -> IntegerValue.IntegerValues.checkNegative(value, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("正の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenPositive() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(5);

      // Act & Assert
      assertThatThrownBy(() -> IntegerValue.IntegerValues.checkNegative(value, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result = IntegerValue.IntegerValues.checkNegative(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkNegativeOrZeroメソッドのテスト")
  class CheckNegativeOrZeroTest {

    @Test
    @DisplayName("負の値の場合、その値を返すべき")
    void shouldReturnValueWhenNegative() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(-5);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkNegativeOrZero(value, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result.value()).isEqualTo(-5);
    }

    @Test
    @DisplayName("ゼロの場合、その値を返すべき")
    void shouldReturnValueWhenZero() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(0);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkNegativeOrZero(value, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result.value()).isEqualTo(0);
    }

    @Test
    @DisplayName("正の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenPositive() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(1);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkNegativeOrZero(value, EXCEPTION_SUPPLIER))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkNegativeOrZero(null, EXCEPTION_SUPPLIER);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkRangeClosedメソッドのテスト")
  class CheckRangeClosedTest {

    @Test
    @DisplayName("範囲内の値の場合、その値を返すべき")
    void shouldReturnValueWhenInRange() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeClosed(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最小値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(10);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeClosed(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(10);
    }

    @Test
    @DisplayName("最大値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(20);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeClosed(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(20);
    }

    @Test
    @DisplayName("範囲外の値の場合、例外をスローするべき")
    void shouldThrowExceptionWhenOutOfRange() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(25);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkRangeClosed(value, EXCEPTION_SUPPLIER, 10, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeClosed(null, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkRangeOpenメソッドのテスト")
  class CheckRangeOpenTest {

    @Test
    @DisplayName("範囲内の値の場合、その値を返すべき")
    void shouldReturnValueWhenInRange() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeOpen(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最小値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(10);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkRangeOpen(value, EXCEPTION_SUPPLIER, 10, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(20);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkRangeOpen(value, EXCEPTION_SUPPLIER, 10, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeOpen(null, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkRangeClosedOpenメソッドのテスト")
  class CheckRangeClosedOpenTest {

    @Test
    @DisplayName("範囲内の値の場合、その値を返すべき")
    void shouldReturnValueWhenInRange() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeClosedOpen(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最小値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(10);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeClosedOpen(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(10);
    }

    @Test
    @DisplayName("最大値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(20);

      // Act & Assert
      assertThatThrownBy(
              () ->
                  IntegerValue.IntegerValues.checkRangeClosedOpen(
                      value, EXCEPTION_SUPPLIER, 10, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeClosedOpen(null, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkRangeOpenClosedメソッドのテスト")
  class CheckRangeOpenClosedTest {

    @Test
    @DisplayName("範囲内の値の場合、その値を返すべき")
    void shouldReturnValueWhenInRange() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeOpenClosed(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最小値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(10);

      // Act & Assert
      assertThatThrownBy(
              () ->
                  IntegerValue.IntegerValues.checkRangeOpenClosed(
                      value, EXCEPTION_SUPPLIER, 10, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(20);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeOpenClosed(value, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result.value()).isEqualTo(20);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkRangeOpenClosed(null, EXCEPTION_SUPPLIER, 10, 20);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkAtLestメソッドのテスト")
  class CheckAtLestTest {

    @Test
    @DisplayName("最小値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(10);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkAtLest(value, EXCEPTION_SUPPLIER, 10);

      // Assert
      assertThat(result.value()).isEqualTo(10);
    }

    @Test
    @DisplayName("最小値より大きい場合、その値を返すべき")
    void shouldReturnValueWhenGreaterThanMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkAtLest(value, EXCEPTION_SUPPLIER, 10);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(9);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkAtLest(value, EXCEPTION_SUPPLIER, 10))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkAtLest(null, EXCEPTION_SUPPLIER, 10);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkAtMostメソッドのテスト")
  class CheckAtMostTest {

    @Test
    @DisplayName("最大値と等しい場合、その値を返すべき")
    void shouldReturnValueWhenEqualsMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(20);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkAtMost(value, EXCEPTION_SUPPLIER, 20);

      // Assert
      assertThat(result.value()).isEqualTo(20);
    }

    @Test
    @DisplayName("最大値未満の場合、その値を返すべき")
    void shouldReturnValueWhenLessThanMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkAtMost(value, EXCEPTION_SUPPLIER, 20);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(21);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkAtMost(value, EXCEPTION_SUPPLIER, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkAtMost(null, EXCEPTION_SUPPLIER, 20);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkLessThanメソッドのテスト")
  class CheckLessThanTest {

    @Test
    @DisplayName("最大値未満の場合、その値を返すべき")
    void shouldReturnValueWhenLessThanMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkLessThan(value, EXCEPTION_SUPPLIER, 20);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最大値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(20);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkLessThan(value, EXCEPTION_SUPPLIER, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最大値超過の場合、例外をスローするべき")
    void shouldThrowExceptionWhenGreaterThanMax() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(21);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkLessThan(value, EXCEPTION_SUPPLIER, 20))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkLessThan(null, EXCEPTION_SUPPLIER, 20);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("IntegerValues.checkGreaterThanメソッドのテスト")
  class CheckGreaterThanTest {

    @Test
    @DisplayName("最小値より大きい場合、その値を返すべき")
    void shouldReturnValueWhenGreaterThanMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(15);

      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkGreaterThan(value, EXCEPTION_SUPPLIER, 10);

      // Assert
      assertThat(result.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("最小値と等しい場合、例外をスローするべき")
    void shouldThrowExceptionWhenEqualsMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(10);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkGreaterThan(value, EXCEPTION_SUPPLIER, 10))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("最小値未満の場合、例外をスローするべき")
    void shouldThrowExceptionWhenLessThanMin() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(9);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkGreaterThan(value, EXCEPTION_SUPPLIER, 10))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("nullの場合、nullを返すべき")
    void shouldReturnNullWhenNull() {
      // Act
      TestIntegerValue result =
          IntegerValue.IntegerValues.checkGreaterThan(null, EXCEPTION_SUPPLIER, 10);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("カスタム例外のテスト")
  class CustomExceptionTest {

    @Test
    @DisplayName("カスタム例外が正しくスローされるべき")
    void shouldThrowCustomException() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(-1);

      // Act & Assert
      assertThatThrownBy(
              () -> IntegerValue.IntegerValues.checkPositive(value, CUSTOM_EXCEPTION_SUPPLIER))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Invalid value");
    }

    @Test
    @DisplayName("範囲チェックでカスタム例外が正しくスローされるべき")
    void shouldThrowCustomExceptionForRangeCheck() {
      // Arrange
      TestIntegerValue value = new TestIntegerValue(25);

      // Act & Assert
      assertThatThrownBy(
              () ->
                  IntegerValue.IntegerValues.checkRangeClosed(
                      value, CUSTOM_EXCEPTION_SUPPLIER, 10, 20))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Invalid value");
    }
  }
}
