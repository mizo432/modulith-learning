package undecided.erp.common.precondition;

import java.util.function.Supplier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IntegerPreconditionTest {

  private final Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException(
      "Number is not in the specified range");

  @Nested
  class VerifyRangeClosedTest {

    @Test
    void whenNumberIsInRange_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerPrecondition.checkRangeClosed(10, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerPrecondition.checkRangeClosed(5, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMax_shouldReturnThatNumber() {
      Integer number = 15;
      assertThat(IntegerPrecondition.checkRangeClosed(15, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeClosed(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeClosed(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyGreaterThanTest {

    @Test
    void whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerPrecondition.checkGreaterThan(10, exceptionSupplier, 5)).isEqualTo(number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkGreaterThan(5, exceptionSupplier, 5));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkGreaterThan(4, exceptionSupplier, 5));
    }

  }

  @Nested
  class VerifyRangeOpenTest {

    @Test
    void whenNumberIsInRange_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerPrecondition.checkRangeOpen(10, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeOpen(5, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsEqualToMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeOpen(15, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeOpen(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeOpen(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyRangeClosedOpenTest {

    @Test
    void whenNumberIsInClosedRange_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerPrecondition.checkRangeClosedOpen(5, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsInOpenRange_shouldReturnThatNumber() {
      Integer number = 14;
      assertThat(IntegerPrecondition.checkRangeClosedOpen(14, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeClosedOpen(15, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeClosedOpen(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeClosedOpen(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @Test
    void whenNumberIsInOpenRange_shouldReturnThatNumber() {
      Integer number = 6;
      assertThat(IntegerPrecondition.checkRangeOpenClosed(6, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsInClosedRange_shouldReturnThatNumber() {
      Integer number = 15;
      assertThat(IntegerPrecondition.checkRangeOpenClosed(15, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeOpenClosed(5, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeOpenClosed(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkRangeOpenClosed(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyAtLestTest {

    @Test
    void whenNumberIsEqualToMin_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerPrecondition.checkAtLest(5, exceptionSupplier, 5)).isEqualTo(number);
    }

    @Test
    void whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerPrecondition.checkAtLest(10, exceptionSupplier, 5)).isEqualTo(number);
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkAtLest(4, exceptionSupplier, 5));
    }

  }

  @Nested
  class VerifyAtMostTest {

    @Test
    void whenNumberIsEqualToMax_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerPrecondition.checkAtMost(10, exceptionSupplier, 10)).isEqualTo(number);
    }

    @Test
    void whenNumberIsLessThanMax_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerPrecondition.checkAtMost(5, exceptionSupplier, 10)).isEqualTo(number);
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkAtMost(15, exceptionSupplier, 10));
    }

  }

  @Nested
  class VerifyPositiveTest {

    @Test
    void whenNumberIsPositive_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerPrecondition.checkPositive(5, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsZero_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkPositive(0, exceptionSupplier));
    }

    @Test
    void whenNumberIsNegative_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkPositive(-5, exceptionSupplier));
    }

  }

  @Nested
  class VerifyNegativeTest {

    @Test
    void whenNumberIsZero_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkNegative(0, exceptionSupplier));
    }

    @Test
    void whenNumberIsPositive_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkNegative(5, exceptionSupplier));
    }

    @Test
    void whenNumberIsNegative_shouldReturnThatNumber() {
      Integer number = -5;
      assertThat(IntegerPrecondition.checkNegative(-5, exceptionSupplier)).isEqualTo(number);
    }

  }

  @Nested
  class VerifyPositiveOrZeroTest {

    @Test
    void whenNumberIsPositive_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerPrecondition.checkPositiveOrZero(5, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsZero_shouldReturnThatNumber() {
      Integer number = 0;
      assertThat(IntegerPrecondition.checkPositiveOrZero(0, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsNegative_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkPositiveOrZero(-5, exceptionSupplier));
    }

  }

  @Nested
  class VerifyNegativeOrZeroTest {

    @Test
    void whenNumberIsZero_shouldReturnThatNumber() {
      Integer number = 0;
      assertThat(IntegerPrecondition.checkNegativeOrZero(0, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsNegative_shouldReturnThatNumber() {
      Integer number = -5;
      assertThat(IntegerPrecondition.checkNegativeOrZero(-5, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsPositive_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkNegativeOrZero(5, exceptionSupplier));
    }

  }


  /**
   * このクラスには、VerifyLessThanメソッドのテストケースが含まれています。
   */
  @Nested
  class VerifyLessThan {

    @Test
    void whenNumberIsLessThanMax_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerPrecondition.checkLessThan(5, exceptionSupplier, 10)).isEqualTo(number);
    }

    @Test
    void whenNumberIsEqualToMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkLessThan(10, exceptionSupplier, 10));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerPrecondition.checkLessThan(15, exceptionSupplier, 10));
    }

  }
}
