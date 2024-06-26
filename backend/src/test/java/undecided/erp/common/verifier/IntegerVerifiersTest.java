package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IntegerVerifiersTest {

  private final Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException(
      "Number is not in the specified range");

  @Nested
  class VerifyRangeClosedTest {

    @Test
    void whenNumberIsInRange_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerVerifiers.verifyRangeClosed(10, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerVerifiers.verifyRangeClosed(5, exceptionSupplier, 5, 15)).isEqualTo(number);
    }

    @Test
    void whenNumberIsEqualToMax_shouldReturnThatNumber() {
      Integer number = 15;
      assertThat(IntegerVerifiers.verifyRangeClosed(15, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeClosed(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeClosed(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyGreaterThanTest {

    @Test
    void whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerVerifiers.verifyGreaterThan(10, exceptionSupplier, 5)).isEqualTo(number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyGreaterThan(5, exceptionSupplier, 5));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyGreaterThan(4, exceptionSupplier, 5));
    }

  }

  @Nested
  class VerifyRangeOpenTest {

    @Test
    void whenNumberIsInRange_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerVerifiers.verifyRangeOpen(10, exceptionSupplier, 5, 15)).isEqualTo(number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeOpen(5, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsEqualToMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeOpen(15, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeOpen(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeOpen(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyRangeClosedOpenTest {

    @Test
    void whenNumberIsInClosedRange_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerVerifiers.verifyRangeClosedOpen(5, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsInOpenRange_shouldReturnThatNumber() {
      Integer number = 14;
      assertThat(IntegerVerifiers.verifyRangeClosedOpen(14, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeClosedOpen(15, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeClosedOpen(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeClosedOpen(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @Test
    void whenNumberIsInOpenRange_shouldReturnThatNumber() {
      Integer number = 6;
      assertThat(IntegerVerifiers.verifyRangeOpenClosed(6, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsInClosedRange_shouldReturnThatNumber() {
      Integer number = 15;
      assertThat(IntegerVerifiers.verifyRangeOpenClosed(15, exceptionSupplier, 5, 15)).isEqualTo(
          number);
    }

    @Test
    void whenNumberIsEqualToMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeOpenClosed(5, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeOpenClosed(4, exceptionSupplier, 5, 15));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyRangeOpenClosed(16, exceptionSupplier, 5, 15));
    }

  }

  @Nested
  class VerifyAtLestTest {

    @Test
    void whenNumberIsEqualToMin_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerVerifiers.verifyAtLest(5, exceptionSupplier, 5)).isEqualTo(number);
    }

    @Test
    void whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerVerifiers.verifyAtLest(10, exceptionSupplier, 5)).isEqualTo(number);
    }

    @Test
    void whenNumberIsLessThanMin_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyAtLest(4, exceptionSupplier, 5));
    }

  }

  @Nested
  class VerifyAtMostTest {

    @Test
    void whenNumberIsEqualToMax_shouldReturnThatNumber() {
      Integer number = 10;
      assertThat(IntegerVerifiers.verifyAtMost(10, exceptionSupplier, 10)).isEqualTo(number);
    }

    @Test
    void whenNumberIsLessThanMax_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerVerifiers.verifyAtMost(5, exceptionSupplier, 10)).isEqualTo(number);
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyAtMost(15, exceptionSupplier, 10));
    }

  }

  @Nested
  class VerifyPositiveTest {

    @Test
    void whenNumberIsPositive_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerVerifiers.verifyPositive(5, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsZero_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyPositive(0, exceptionSupplier));
    }

    @Test
    void whenNumberIsNegative_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyPositive(-5, exceptionSupplier));
    }

  }

  @Nested
  class VerifyNegativeTest {

    @Test
    void whenNumberIsZero_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyNegative(0, exceptionSupplier));
    }

    @Test
    void whenNumberIsPositive_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyNegative(5, exceptionSupplier));
    }

    @Test
    void whenNumberIsNegative_shouldReturnThatNumber() {
      Integer number = -5;
      assertThat(IntegerVerifiers.verifyNegative(-5, exceptionSupplier)).isEqualTo(number);
    }

  }

  @Nested
  class VerifyPositiveOrZeroTest {

    @Test
    void whenNumberIsPositive_shouldReturnThatNumber() {
      Integer number = 5;
      assertThat(IntegerVerifiers.verifyPositiveOrZero(5, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsZero_shouldReturnThatNumber() {
      Integer number = 0;
      assertThat(IntegerVerifiers.verifyPositiveOrZero(0, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsNegative_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyPositiveOrZero(-5, exceptionSupplier));
    }

  }

  @Nested
  class VerifyNegativeOrZeroTest {

    @Test
    void whenNumberIsZero_shouldReturnThatNumber() {
      Integer number = 0;
      assertThat(IntegerVerifiers.verifyNegativeOrZero(0, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsNegative_shouldReturnThatNumber() {
      Integer number = -5;
      assertThat(IntegerVerifiers.verifyNegativeOrZero(-5, exceptionSupplier)).isEqualTo(number);
    }

    @Test
    void whenNumberIsPositive_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyNegativeOrZero(5, exceptionSupplier));
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
      assertThat(IntegerVerifiers.verifyLessThan(5, exceptionSupplier, 10)).isEqualTo(number);
    }

    @Test
    void whenNumberIsEqualToMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyLessThan(10, exceptionSupplier, 10));
    }

    @Test
    void whenNumberIsGreaterThanMax_shouldThrowException() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> IntegerVerifiers.verifyLessThan(15, exceptionSupplier, 10));
    }

  }
}
