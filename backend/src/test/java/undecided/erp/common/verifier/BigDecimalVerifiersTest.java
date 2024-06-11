package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BigDecimalVerifiersTest {

  public static final Supplier<RuntimeException> RUNTIME_EXCEPTION_SUPPLIER = IllegalArgumentException::new;
  private static final BigDecimal ref = new BigDecimal("10");
  private static final BigDecimal min = new BigDecimal("5");
  private static final BigDecimal max = new BigDecimal("15");

  @Nested
  class VerifyGreaterThanTest {

    @Test
    void positive() {
      BigDecimal result = BigDecimalVerifiers.verifyGreaterThan(ref,
          RUNTIME_EXCEPTION_SUPPLIER, min);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void negative() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyGreaterThan(min, RUNTIME_EXCEPTION_SUPPLIER,
              max));
    }

  }

  @Nested
  class VerifyLessThanTest {

    @Test
    void positive() {
      BigDecimal result = BigDecimalVerifiers.verifyLessThan(ref,
          RUNTIME_EXCEPTION_SUPPLIER, max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void negative() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyLessThan(max, RUNTIME_EXCEPTION_SUPPLIER, min));
    }

  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @Test
    void positiveWithinRange() {
      BigDecimal withinRange = new BigDecimal("6");
      BigDecimal result = BigDecimalVerifiers.verifyRangeOpenClosed(withinRange,
          RUNTIME_EXCEPTION_SUPPLIER, min, max);
      assertThat(result).isEqualTo(withinRange);
    }

    @Test
    void negativeOutOfRange() {
      BigDecimal outOfRange = new BigDecimal("16");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyRangeOpenClosed(outOfRange, RUNTIME_EXCEPTION_SUPPLIER,
              min, max));
    }

    @Test
    void negativeEqualToMin() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyRangeOpenClosed(min, RUNTIME_EXCEPTION_SUPPLIER,
              min, max));
    }

    @Test
    void positiveEqualToMax() {
      BigDecimal result = BigDecimalVerifiers.verifyRangeOpenClosed(max,
          RUNTIME_EXCEPTION_SUPPLIER, min, max);
      assertThat(result).isEqualTo(max);
    }
  }

  @Nested
  class VerifyAtMostTest {

    @Test
    void positive() {
      BigDecimal result = BigDecimalVerifiers.verifyAtMost(ref,
          RUNTIME_EXCEPTION_SUPPLIER, max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void negative() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyAtMost(max, RUNTIME_EXCEPTION_SUPPLIER, min));
    }
  }

  @Nested
  class VerifyPositiveTest {

    @Test
    void positive() {
      BigDecimal positiveNum = new BigDecimal("1");
      BigDecimal result = BigDecimalVerifiers.verifyPositive(positiveNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(positiveNum);
    }

    @Test
    void negativeWithZero() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyPositive(BigDecimal.ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    void negativeWithNegative() {
      BigDecimal negativeNum = new BigDecimal("-1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyPositive(negativeNum, RUNTIME_EXCEPTION_SUPPLIER));
    }
  }

  @Nested
  class VerifyPositiveOrZeroTest {

    @Test
    void positive() {
      BigDecimal positiveNum = new BigDecimal("1");
      BigDecimal result = BigDecimalVerifiers.verifyPositiveOrZero(positiveNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(positiveNum);
    }

    @Test
    void zero() {
      BigDecimal result = BigDecimalVerifiers.verifyPositiveOrZero(BigDecimal.ZERO,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void negativeWithNegative() {
      BigDecimal negativeNum = new BigDecimal("-1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyPositiveOrZero(negativeNum, RUNTIME_EXCEPTION_SUPPLIER));
    }
  }

  @Nested
  class VerifyNegativeTest {

    @Test
    void positive() {
      BigDecimal negativeNum = new BigDecimal("-1");
      BigDecimal result = BigDecimalVerifiers.verifyNegative(negativeNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(negativeNum);
    }

    @Test
    void negativeWithZero() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyNegative(BigDecimal.ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    void negativeWithPositive() {
      BigDecimal positiveNum = new BigDecimal("1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyNegative(positiveNum, RUNTIME_EXCEPTION_SUPPLIER));
    }
  }

  
  @Nested
  class VerifyNegativeOrZeroTest {

    @Test
    void positive() {
      BigDecimal negativeNum = new BigDecimal("-1");
      BigDecimal result = BigDecimalVerifiers.verifyNegativeOrZero(negativeNum,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(negativeNum);
    }

    @Test
    void zero() {
      BigDecimal result = BigDecimalVerifiers.verifyNegativeOrZero(BigDecimal.ZERO,
          RUNTIME_EXCEPTION_SUPPLIER);
      assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void negativeWithPositive() {
      BigDecimal positiveNum = new BigDecimal("1");
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> BigDecimalVerifiers.verifyNegativeOrZero(positiveNum, RUNTIME_EXCEPTION_SUPPLIER));
    }
  }
}
