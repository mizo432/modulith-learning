package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigInteger;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BigIntegerVerifiersTest {

  private static final BigInteger ZERO = BigInteger.ZERO;
  private static final BigInteger POSITIVE = BigInteger.ONE;
  private static final BigInteger NEGATIVE = BigInteger.valueOf(-1);
  private static final Supplier<RuntimeException> RUNTIME_EXCEPTION_SUPPLIER = RuntimeException::new;

  @Nested
  class VerifyPositiveTest {


    @Test
    public void whenPositiveValue() {
      assertThat(BigIntegerVerifiers.verifyPositive(POSITIVE,
          RUNTIME_EXCEPTION_SUPPLIER)).isEqualTo(POSITIVE);
    }

    @Test
    public void whenZeroValue() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyPositive(ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNegativeValue() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyPositive(NEGATIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNull() {
      assertThat(BigIntegerVerifiers.verifyPositive(null, RUNTIME_EXCEPTION_SUPPLIER)).isNull();
    }

  }

  @Nested
  class VerifyPositiveOrZeroTest {

    @Test
    public void whenPositiveValue() {
      assertThat(BigIntegerVerifiers.verifyPositiveOrZero(POSITIVE,
          RUNTIME_EXCEPTION_SUPPLIER)).isEqualTo(POSITIVE);
    }

    @Test
    public void whenZeroValue() {
      assertThat(
          BigIntegerVerifiers.verifyPositiveOrZero(ZERO, RUNTIME_EXCEPTION_SUPPLIER)).isEqualTo(
          ZERO);
    }

    @Test
    public void whenNegativeValue() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyPositiveOrZero(NEGATIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNull() {
      assertThat(
          BigIntegerVerifiers.verifyPositiveOrZero(null, RUNTIME_EXCEPTION_SUPPLIER)).isNull();
    }

  }

  @Nested
  class VerifyNegativeTest {

    @Test
    public void whenPositiveValue() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyNegative(POSITIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenZeroValue() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyNegative(ZERO, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenNegativeValue() {
      assertThat(BigIntegerVerifiers.verifyNegative(NEGATIVE,
          RUNTIME_EXCEPTION_SUPPLIER)).isEqualTo(NEGATIVE);
    }

    @Test
    public void whenNull() {
      assertThat(BigIntegerVerifiers.verifyNegative(null, RUNTIME_EXCEPTION_SUPPLIER)).isNull();
    }
  }

  @Nested
  class VerifyNegativeOrZeroTest {

    @Test
    public void whenPositiveValue() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyNegativeOrZero(POSITIVE, RUNTIME_EXCEPTION_SUPPLIER));
    }

    @Test
    public void whenZeroValue() {
      assertThat(
          BigIntegerVerifiers.verifyNegativeOrZero(ZERO, RUNTIME_EXCEPTION_SUPPLIER)).isEqualTo(
          ZERO);
    }

    @Test
    public void whenNegativeValue() {
      assertThat(
          BigIntegerVerifiers.verifyNegativeOrZero(NEGATIVE, RUNTIME_EXCEPTION_SUPPLIER)).isEqualTo(
          NEGATIVE);
    }

    @Test
    public void whenNull() {
      assertThat(
          BigIntegerVerifiers.verifyNegativeOrZero(null, RUNTIME_EXCEPTION_SUPPLIER)).isNull();
    }
  }

  @Nested
  class VerifyLessThanTest {

    @Test
    public void whenValueLessThanMax() {
      BigInteger ref = BigInteger.ONE;
      BigInteger max = BigInteger.TEN;
      assertThat(BigIntegerVerifiers.verifyLessThan(ref, RUNTIME_EXCEPTION_SUPPLIER, max))
          .isEqualTo(ref);
    }

    @Test
    public void whenValueEqualsToMax() {
      BigInteger ref = BigInteger.TEN;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyLessThan(ref, RUNTIME_EXCEPTION_SUPPLIER, ref));
    }

    @Test
    public void whenValueGreaterThanMax() {
      BigInteger ref = BigInteger.TEN;
      BigInteger max = BigInteger.ONE;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyLessThan(ref, RUNTIME_EXCEPTION_SUPPLIER, max));
    }

    @Test
    public void whenNull() {
      BigInteger max = BigInteger.TEN;
      assertThat(BigIntegerVerifiers.verifyLessThan(null, RUNTIME_EXCEPTION_SUPPLIER, max))
          .isNull();
    }
  }

  @Nested
  class VerifyGreaterThanTest {

    @Test
    public void whenValueGreaterThanMin() {
      BigInteger ref = BigInteger.TEN;
      BigInteger min = BigInteger.ONE;
      assertThat(BigIntegerVerifiers.verifyGreaterThan(ref, RUNTIME_EXCEPTION_SUPPLIER, min))
          .isEqualTo(ref);
    }

    @Test
    public void whenValueEqualsToMin() {
      BigInteger ref = BigInteger.ONE;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyGreaterThan(ref, RUNTIME_EXCEPTION_SUPPLIER, ref));
    }

    @Test
    public void whenValueLessThanMax() {
      BigInteger ref = BigInteger.ONE;
      BigInteger min = BigInteger.TEN;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> BigIntegerVerifiers.verifyGreaterThan(ref, RUNTIME_EXCEPTION_SUPPLIER, min));
    }

    @Test
    public void whenNull() {
      BigInteger min = BigInteger.ONE;
      assertThat(BigIntegerVerifiers.verifyGreaterThan(null, RUNTIME_EXCEPTION_SUPPLIER, min))
          .isNull();
    }
  }
}
