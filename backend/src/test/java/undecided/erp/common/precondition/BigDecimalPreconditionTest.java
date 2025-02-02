package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BigDecimalPreconditionTest {

  public static final Supplier<RuntimeException> RUNTIME_EXCEPTION_SUPPLIER = IllegalArgumentException::new;
  private static final BigDecimal ref = new BigDecimal("10");
  private static final BigDecimal min = new BigDecimal("5");
  private static final BigDecimal max = new BigDecimal("15");

  @Nested
  class VerifyGreaterThanTest {

    @Test
    void positive() {
      BigDecimal result = BigDecimalPrecondition.verifyGreaterThan(ref,
          RUNTIME_EXCEPTION_SUPPLIER, min);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void negative() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyGreaterThan(min, RUNTIME_EXCEPTION_SUPPLIER,
              max));
    }

  }

  @Nested
  class VerifyLessThanTest {

    @Test
    void positive() {
      BigDecimal result = BigDecimalPrecondition.verifyLessThan(ref,
          RUNTIME_EXCEPTION_SUPPLIER, max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void negative() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyLessThan(max, RUNTIME_EXCEPTION_SUPPLIER, min));
    }

  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @Test
    void positiveWithinRange() {
      BigDecimal withinRange = new BigDecimal("6");
      BigDecimal result = BigDecimalPrecondition.verifyRangeOpenClosed(withinRange,
          RUNTIME_EXCEPTION_SUPPLIER, min, max);
      assertThat(result).isEqualTo(withinRange);
    }

    @Test
    void negativeOutOfRange() {
      BigDecimal outOfRange = new BigDecimal("16");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyRangeOpenClosed(outOfRange, RUNTIME_EXCEPTION_SUPPLIER,
              min, max));
    }

    @Test
    void negativeEqualToMin() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyRangeOpenClosed(min, RUNTIME_EXCEPTION_SUPPLIER,
              min, max));
    }

    @Test
    void positiveEqualToMax() {
      BigDecimal result = BigDecimalPrecondition.verifyRangeOpenClosed(max,
          RUNTIME_EXCEPTION_SUPPLIER, min, max);
      assertThat(result).isEqualTo(max);
    }
  }

  @Nested
  class VerifyAtMostTest {

    @Test
    void positive() {
      BigDecimal result = BigDecimalPrecondition.verifyAtMost(ref,
          RUNTIME_EXCEPTION_SUPPLIER, max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void negative() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyAtMost(max, RUNTIME_EXCEPTION_SUPPLIER, min));
    }
  }

  @Nested
  class VerifyPositiveTest {

    @Test
    void positive() {
      BigDecimal positiveNum = new BigDecimal("1");
      BigDecimal result = BigDecimalPrecondition.verifyPositive(positiveNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(positiveNum);
    }

    @Test
    void negativeWithZero() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyPositive(BigDecimal.ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    void negativeWithNegative() {
      BigDecimal negativeNum = new BigDecimal("-1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyPositive(negativeNum, RUNTIME_EXCEPTION_SUPPLIER));
    }
  }

  @Nested
  class VerifyPositiveOrZeroTest {

    @Test
    void positive() {
      BigDecimal positiveNum = new BigDecimal("1");
      BigDecimal result = BigDecimalPrecondition.verifyPositiveOrZero(positiveNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(positiveNum);
    }

    @Test
    void zero() {
      BigDecimal result = BigDecimalPrecondition.verifyPositiveOrZero(BigDecimal.ZERO,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void negativeWithNegative() {
      BigDecimal negativeNum = new BigDecimal("-1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyPositiveOrZero(negativeNum,
              RUNTIME_EXCEPTION_SUPPLIER));
    }
  }

  @Nested
  class VerifyNegativeTest {

    @Test
    void positive() {
      BigDecimal negativeNum = new BigDecimal("-1");
      BigDecimal result = BigDecimalPrecondition.verifyNegative(negativeNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(negativeNum);
    }

    @Test
    void negativeWithZero() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyNegative(BigDecimal.ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    void negativeWithPositive() {
      BigDecimal positiveNum = new BigDecimal("1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyNegative(positiveNum, RUNTIME_EXCEPTION_SUPPLIER));
    }
  }


  @Nested
  class VerifyNegativeOrZeroTest {

    @Test
    void positive() {
      BigDecimal negativeNum = new BigDecimal("-1");
      BigDecimal result = BigDecimalPrecondition.verifyNegativeOrZero(negativeNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(negativeNum);
    }

    @Test
    void zero() {
      BigDecimal result = BigDecimalPrecondition.verifyNegativeOrZero(BigDecimal.ZERO,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void negativeWithPositive() {
      BigDecimal positiveNum = new BigDecimal("1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalPrecondition.verifyNegativeOrZero(positiveNum,
              RUNTIME_EXCEPTION_SUPPLIER));
    }
  }
}
