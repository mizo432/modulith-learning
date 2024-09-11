package undecided.erp.shared.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.primitive.Objects2;
import undecided.erp.shared.entity.LongValue.LongValues;

@DisplayName("LongValuesクラス")
class LongValuesTest {

  private final Supplier<? extends RuntimeException> exceptionSupplier = IllegalArgumentException::new;

  private static class TestLongValue implements LongValue<TestLongValue> {

    private final Long value;

    public TestLongValue(Long value) {
      this.value = value;
    }

    @Override
    public Long getValue() {
      return value;
    }

    @Override
    public boolean isEmpty() {
      return Objects2.isNull(value);
    }
  }

  @Nested
  @DisplayName("checkPositiveメソッド")
  class CheckPositive {

    @Test
    @DisplayName("正の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenPositiveValueGiven() {
      var ref = new TestLongValue(5L);
      var result = LongValues.checkPositive(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("ゼロを指定すると例外がスローされること")
    void shouldThrowExceptionWhenZeroGiven() {
      var ref = new TestLongValue(0L);
      assertThatThrownBy(() -> LongValues.checkPositive(ref, exceptionSupplier))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("負の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenNegativeValueGiven() {
      var ref = new TestLongValue(-1L);
      assertThatThrownBy(() -> LongValues.checkPositive(ref, exceptionSupplier))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkPositive(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkNonNegativeメソッド")
  class CheckNonNegative {

    @Test
    @DisplayName("ゼロまたは正の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenNonNegativeValueGiven() {
      var ref = new TestLongValue(0L);
      var result = LongValues.checkNonNegative(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);

      ref = new TestLongValue(5L);
      result = LongValues.checkNonNegative(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("負の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenNegativeValueGiven() {
      var ref = new TestLongValue(-1L);
      assertThatThrownBy(() -> LongValues.checkNonNegative(ref, exceptionSupplier))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkNonNegative(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkNegativeメソッド")
  class CheckNegative {

    @Test
    @DisplayName("負の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenNegativeValueGiven() {
      var ref = new TestLongValue(-5L);
      var result = LongValues.checkNegative(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("正の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenPositiveValueGiven() {
      var ref = new TestLongValue(5L);
      assertThatThrownBy(() -> LongValues.checkNegative(ref, exceptionSupplier))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("ゼロを指定すると例外がスローされること")
    void shouldThrowExceptionWhenZeroGiven() {
      var ref = new TestLongValue(0L);
      assertThatThrownBy(() -> LongValues.checkNegative(ref, exceptionSupplier))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkNegative(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkNegativeOrZeroメソッド")
  class CheckNegativeOrZero {

    @Test
    @DisplayName("ゼロを指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenZeroGiven() {
      var ref = new TestLongValue(0L);
      var result = LongValues.checkNegativeOrZero(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("負の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenNegativeValueGiven() {
      var ref = new TestLongValue(-5L);
      var result = LongValues.checkNegativeOrZero(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("正の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenPositiveValueGiven() {
      var ref = new TestLongValue(5L);
      assertThatThrownBy(() -> LongValues.checkNegativeOrZero(ref, exceptionSupplier))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkNegativeOrZero(ref, exceptionSupplier);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkRangeClosedメソッド")
  class CheckRangeClosedTest {

    @Test
    @DisplayName("範囲内の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenInRange() {
      var ref = new TestLongValue(5L);
      var result = LongValues.checkRangeClosed(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("範囲の境界値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenBoundaryValueGiven() {
      var ref = new TestLongValue(3L);
      var result = LongValues.checkRangeClosed(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);

      ref = new TestLongValue(7L);
      result = LongValues.checkRangeClosed(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("範囲外の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenOutOfRange() {
      var ref = new TestLongValue(10L);
      assertThatThrownBy(() -> LongValues.checkRangeClosed(ref, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkRangeClosed(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkRangeOpenメソッド")
  class CheckRangeOpenTest {

    @Test
    @DisplayName("範囲内（範囲外の値は除く）の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenInRange() {
      var ref = new TestLongValue(5L);
      var result = LongValues.checkRangeOpen(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("範囲の境界値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenBoundaryValueGiven() {
      var ref1 = new TestLongValue(3L);
      assertThatThrownBy(() -> LongValues.checkRangeOpen(ref1, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);

      var ref2 = new TestLongValue(7L);
      assertThatThrownBy(() -> LongValues.checkRangeOpen(ref2, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("範囲外の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenOutOfRange() {
      var ref = new TestLongValue(10L);
      assertThatThrownBy(() -> LongValues.checkRangeOpen(ref, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkRangeOpen(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkRangeClosedOpenメソッド")
  class CheckRangeClosedOpenTest {

    @Test
    @DisplayName("範囲内の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenInRange() {
      var ref = new TestLongValue(5L);
      var result = LongValues.checkRangeClosedOpen(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("範囲の下限を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenLowerBoundaryValueGiven() {
      var ref = new TestLongValue(3L);
      var result = LongValues.checkRangeClosedOpen(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("範囲の上限を指定すると例外がスローされること")
    void shouldThrowExceptionWhenUpperBoundaryValueGiven() {
      var ref = new TestLongValue(7L);
      assertThatThrownBy(() -> LongValues.checkRangeClosedOpen(ref, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("範囲外の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenOutOfRange() {
      var ref = new TestLongValue(10L);
      assertThatThrownBy(() -> LongValues.checkRangeClosedOpen(ref, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkRangeClosedOpen(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkGreaterThanメソッド")
  class CheckGreaterThanTest {

    @Test
    @DisplayName("指定値より大きい値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenGivenValueIsGreaterThanSpecified() {
      var ref = new TestLongValue(5L);
      var result = LongValues.checkGreaterThan(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("指定値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenGivenValueIsEqualToSpecified() {
      var ref = new TestLongValue(3L);
      assertThatThrownBy(() -> LongValues.checkGreaterThan(ref, exceptionSupplier, 3L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("指定値より小さい値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenGivenValueIsLessThanSpecified() {
      var ref = new TestLongValue(2L);
      assertThatThrownBy(() -> LongValues.checkGreaterThan(ref, exceptionSupplier, 3L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkGreaterThan(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkLessThanメソッド")
  class CheckLessThanTest {

    @Test
    @DisplayName("指定値より小さい値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenGivenValueIsLessThanSpecified() {
      var ref = new TestLongValue(2L);
      var result = LongValues.checkLessThan(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("指定値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenGivenValueIsEqualToSpecified() {
      var ref = new TestLongValue(3L);
      assertThatThrownBy(() -> LongValues.checkLessThan(ref, exceptionSupplier, 3L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("指定値より大きい値を指定すると例外が要求されること")
    void shouldThrowExceptionWhenGivenValueIsGreaterThanSpecified() {
      var ref = new TestLongValue(5L);
      assertThatThrownBy(() -> LongValues.checkLessThan(ref, exceptionSupplier, 3L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkLessThan(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkAtMostメソッド")
  class CheckAtMostTest {

    @Test
    @DisplayName("指定値より小さい値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenGivenValueIsLessThanSpecified() {
      var ref = new TestLongValue(2L);
      var result = LongValues.checkAtMost(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("指定値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenGivenValueIsEqualToSpecified() {
      var ref = new TestLongValue(3L);
      var result = LongValues.checkAtMost(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("指定値より大きな値を指定すると例外が要求されること")
    void shouldThrowExceptionWhenGivenValueIsGreaterThanSpecified() {
      var ref = new TestLongValue(5L);
      assertThatThrownBy(() -> LongValues.checkAtMost(ref, exceptionSupplier, 3L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkAtMost(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkAtLestメソッド")
  class CheckAtLestTest {

    @Test
    @DisplayName("指定値以上の値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenGivenValueIsGreaterThanOrEqualToSpecified() {
      var ref = new TestLongValue(5L);
      var result = LongValues.checkAtLest(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("指定値を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenGivenValueIsEqualToSpecified() {
      var ref = new TestLongValue(3L);
      var result = LongValues.checkAtLest(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("指定値より小さい値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenGivenValueIsLessThanSpecified() {
      var ref = new TestLongValue(2L);
      assertThatThrownBy(() -> LongValues.checkAtLest(ref, exceptionSupplier, 3L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkAtLest(ref, exceptionSupplier, 3L);
      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  @DisplayName("checkRangeOpenClosedメソッド")
  class CheckRangeOpenClosedTest {

    @Test
    @DisplayName("範囲内（境界外の値は除く）の値を指定すると該当の値が戻されること。")
    void shouldReturnGivenValueWhenInRange() {
      var ref = new TestLongValue(5L);
      var result = LongValues.checkRangeOpenClosed(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("範囲の下限を指定すると例外がスローされること")
    void shouldThrowExceptionWhenLowerBoundaryGiven() {
      var ref = new TestLongValue(3L);
      assertThatThrownBy(() -> LongValues.checkRangeOpenClosed(ref, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("範囲の上限を指定すると該当の値が返ること")
    void shouldReturnGivenValueWhenUpperBoundaryGiven() {
      var ref = new TestLongValue(7L);
      var result = LongValues.checkRangeOpenClosed(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    @DisplayName("範囲外の値を指定すると例外がスローされること")
    void shouldThrowExceptionWhenOutOfRange() {
      var ref = new TestLongValue(10L);
      assertThatThrownBy(() -> LongValues.checkRangeOpenClosed(ref, exceptionSupplier, 3L, 7L))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("nullを指定するとnullが返ること")
    void shouldReturnNullWhenNullGiven() {
      TestLongValue ref = null;
      var result = LongValues.checkRangeOpenClosed(ref, exceptionSupplier, 3L, 7L);
      assertThat(result).isEqualTo(ref);
    }
  }
}
