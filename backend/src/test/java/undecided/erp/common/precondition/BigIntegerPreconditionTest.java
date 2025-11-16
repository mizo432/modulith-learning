package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigInteger;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BigIntegerPreconditionTest {

  private static final BigInteger ZERO = BigInteger.ZERO;
  private static final BigInteger POSITIVE = BigInteger.ONE;
  private static final BigInteger NEGATIVE = BigInteger.valueOf(-1);
  private static final Supplier<RuntimeException> RUNTIME_EXCEPTION_SUPPLIER =
      RuntimeException::new;

  @Nested
  class VerifyPositiveTest {

    @Test
    public void whenPositiveValue() {
      assertThat(BigIntegerPrecondition.checkPositive(POSITIVE, RUNTIME_EXCEPTION_SUPPLIER))
          .isEqualTo(POSITIVE);
    }

    @Test
    public void whenZeroValue() {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(() -> BigIntegerPrecondition.checkPositive(ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNegativeValue() {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkPositive(NEGATIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNull() {
      assertThat(BigIntegerPrecondition.checkPositive(null, RUNTIME_EXCEPTION_SUPPLIER)).isNull();
    }
  }

  @Nested
  class VerifyPositiveOrZeroTest {

    @Test
    public void whenPositiveValue() {
      assertThat(BigIntegerPrecondition.checkNonNegative(POSITIVE, RUNTIME_EXCEPTION_SUPPLIER))
          .isEqualTo(POSITIVE);
    }

    @Test
    public void whenZeroValue() {
      assertThat(BigIntegerPrecondition.checkNonNegative(ZERO, RUNTIME_EXCEPTION_SUPPLIER))
          .isEqualTo(ZERO);
    }

    @Test
    public void whenNegativeValue() {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkNonNegative(NEGATIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNull() {
      assertThat(BigIntegerPrecondition.checkNonNegative(null, RUNTIME_EXCEPTION_SUPPLIER))
          .isNull();
    }
  }

  @Nested
  class VerifyNegativeTest {

    @Test
    public void whenPositiveValue() {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkNegative(POSITIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenZeroValue() {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(() -> BigIntegerPrecondition.checkNegative(ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNegativeValue() {
      assertThat(BigIntegerPrecondition.checkNegative(NEGATIVE, RUNTIME_EXCEPTION_SUPPLIER))
          .isEqualTo(NEGATIVE);
    }

    @Test
    public void whenNull() {
      assertThat(BigIntegerPrecondition.checkNegative(null, RUNTIME_EXCEPTION_SUPPLIER)).isNull();
    }
  }

  @Nested
  class VerifyNegativeOrZeroTest {

    @Test
    public void whenPositiveValue() {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkNonPositive(POSITIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenZeroValue() {
      assertThat(BigIntegerPrecondition.checkNonPositive(ZERO, RUNTIME_EXCEPTION_SUPPLIER))
          .isEqualTo(ZERO);
    }

    @Test
    public void whenNegativeValue() {
      assertThat(BigIntegerPrecondition.checkNonPositive(NEGATIVE, RUNTIME_EXCEPTION_SUPPLIER))
          .isEqualTo(NEGATIVE);
    }

    @Test
    public void whenNull() {
      assertThat(BigIntegerPrecondition.checkNonPositive(null, RUNTIME_EXCEPTION_SUPPLIER))
          .isNull();
    }
  }

  @Nested
  class VerifyLessThanTest {

    @Test
    public void whenValueLessThanMax() {
      BigInteger ref = BigInteger.ONE;
      BigInteger max = BigInteger.TEN;
      assertThat(BigIntegerPrecondition.checkLessThan(ref, RUNTIME_EXCEPTION_SUPPLIER, max))
          .isEqualTo(ref);
    }

    @Test
    public void whenValueEqualsToMax() {
      BigInteger ref = BigInteger.TEN;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkLessThan(ref, RUNTIME_EXCEPTION_SUPPLIER, ref));
    }

    @Test
    public void whenValueGreaterThanMax() {
      BigInteger ref = BigInteger.TEN;
      BigInteger max = BigInteger.ONE;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkLessThan(ref, RUNTIME_EXCEPTION_SUPPLIER, max));
    }

    @Test
    public void whenNull() {
      BigInteger max = BigInteger.TEN;
      assertThat(BigIntegerPrecondition.checkLessThan(null, RUNTIME_EXCEPTION_SUPPLIER, max))
          .isNull();
    }
  }

  @Nested
  class VerifyGreaterThanTest {

    @Test
    public void whenValueGreaterThanMin() {
      BigInteger ref = BigInteger.TEN;
      BigInteger min = BigInteger.ONE;
      assertThat(BigIntegerPrecondition.checkGreaterThan(ref, RUNTIME_EXCEPTION_SUPPLIER, min))
          .isEqualTo(ref);
    }

    @Test
    public void whenValueEqualsToMin() {
      BigInteger ref = BigInteger.ONE;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkGreaterThan(ref, RUNTIME_EXCEPTION_SUPPLIER, ref));
    }

    @Test
    public void whenValueLessThanMax() {
      BigInteger ref = BigInteger.ONE;
      BigInteger min = BigInteger.TEN;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> BigIntegerPrecondition.checkGreaterThan(ref, RUNTIME_EXCEPTION_SUPPLIER, min));
    }

    @Test
    public void whenNull() {
      BigInteger min = BigInteger.ONE;
      assertThat(BigIntegerPrecondition.checkGreaterThan(null, RUNTIME_EXCEPTION_SUPPLIER, min))
          .isNull();
    }
  }
}
