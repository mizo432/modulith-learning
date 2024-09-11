package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class FloatVerifiersTest {

  private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

  @Test
  void shouldVerifyPositive_InputIsPositive_ReturnsSameValue() {
    Float input = 1.5f;
    Float output = FloatVerifiers.verifyPositive(input, exceptionSupplier);
    assertThat(output).isEqualTo(input);
  }

  // <省略している他のテストケース>

  @Test
  void shouldThrowExceptionForVerifyPositive_ExceptionSupplierIsNull() {
    Float input = 1.5f;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(
        () -> FloatVerifiers.verifyPositive(input, nullSupplier));
  }

  @Test
  void shouldThrowExceptionForVerifyAtMost_ExceptionSupplierIsNull() {
    Float input = 1.5f;
    Float max = 2.0f;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(
        () -> FloatVerifiers.verifyAtMost(input, nullSupplier, max));
  }

  //added
  @Test
  void shouldVerifyPositive_InputIsNegative_ThrowsException() {
    Float input = -1.5f;
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> FloatVerifiers.verifyPositive(input, exceptionSupplier));
  }

  @Test
  void shouldVerifyPositive_InputIsZero_ThrowsException() {
    Float input = 0.0f;
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> FloatVerifiers.verifyPositive(input, exceptionSupplier));
  }

  @Test
  void shouldVerifyPositiveOrZero_InputIsPositive_ReturnsSameValue() {
    Float input = 1.5f;
    Float output = FloatVerifiers.verifyPositiveOrZero(input, exceptionSupplier);
    assertThat(output).isEqualTo(input);
  }

  @Test
  void shouldVerifyPositiveOrZero_InputIsZero_ReturnsSameValue() {
    Float input = 0.0f;
    Float output = FloatVerifiers.verifyPositiveOrZero(input, exceptionSupplier);
    assertThat(output).isEqualTo(input);
  }

  @Test
  void shouldVerifyPositiveOrZero_InputIsNegative_ThrowsException() {
    Float input = -1.5f;
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> FloatVerifiers.verifyPositiveOrZero(input, exceptionSupplier));
  }

  @Test
  void shouldVerifyNegative_InputIsNegative_ReturnsSameValue() {
    Float input = -1.5f;
    Float output = FloatVerifiers.verifyNegative(input, exceptionSupplier);
    assertThat(output).isEqualTo(input);
  }

  @Test
  void shouldVerifyNegative_InputIsPositive_ThrowsException() {
    Float input = 1.5f;
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> FloatVerifiers.verifyNegative(input, exceptionSupplier));
  }

  @Test
  void shouldVerifyNegative_InputIsZero_ThrowsException() {
    Float input = 0.0f;
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> FloatVerifiers.verifyNegative(input, exceptionSupplier));
  }

  @Test
  void shouldVerifyNegativeOrZero_InputIsNegative_ReturnsSameValue() {
    Float input = -1.5f;
    Float output = FloatVerifiers.verifyNegativeOrZero(input, exceptionSupplier);
    assertThat(output).isEqualTo(input);
  }

  @Test
  void shouldVerifyNegativeOrZero_InputIsZero_ReturnsSameValue() {
    Float input = 0.0f;
    Float output = FloatVerifiers.verifyNegativeOrZero(input, exceptionSupplier);
    assertThat(output).isEqualTo(input);
  }

  @Test
  void shouldVerifyNegativeOrZero_InputIsPositive_ThrowsException() {
    Float input = 1.5f;
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> FloatVerifiers.verifyNegativeOrZero(input, exceptionSupplier));
  }

  @Test
  void shouldVerifyNegativeOrZero_InputIsNull_ReturnsNull() {
    Float input = null;
    Float expectedOutput = null;
    Float output = FloatVerifiers.verifyNegativeOrZero(input, exceptionSupplier);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  void shouldVerifyRangeClosed_InputWithinRange_ReturnsSameValue() {
    Float input = 1.5f;
    Float min = 0.0f;
    Float max = 2.0f;
    Float output = FloatVerifiers.verifyRangeClosed(input, exceptionSupplier, min, max);
    assertThat(output).isEqualTo(input);
  }

  @Test
  void shouldVerifyRangeClosed_InputBelowRange_ThrowsException() {
    Float input = -1.0f;
    Float min = 0.0f;
    Float max = 2.0f;
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> FloatVerifiers.verifyRangeClosed(input, exceptionSupplier, min, max));
  }

  @Test
  void shouldVerifyRangeClosed_InputAboveRange_ThrowsException() {
    Float input = 3.0f;
    Float min = 0.0f;
    Float max = 2.0f;
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> FloatVerifiers.verifyRangeClosed(input, exceptionSupplier, min, max));
  }

  @Test
  void shouldVerifyRangeClosed_InputIsNull_ReturnsNull() {
    Float input = null;
    Float min = 0.0f;
    Float max = 2.0f;
    Float output = FloatVerifiers.verifyRangeClosed(input, exceptionSupplier, min, max);
    assertThat(output).isNull();
  }

  @Test
  void shouldVerifyRangeClosed_MinEqualsMax_InputEqualsMin_ReturnsSameValue() {
    Float input = 1.0f;
    Float min = 1.0f;
    Float max = 1.0f;
    Float output = FloatVerifiers.verifyRangeClosed(input, exceptionSupplier, min, max);
    assertThat(output).isEqualTo(input);
  }

  @Test
  void shouldVerifyRangeClosed_MinEqualsMax_InputGreaterThanMin_ThrowsException() {
    Float input = 2.0f;
    Float min = 1.0f;
    Float max = 1.0f;
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> FloatVerifiers.verifyRangeClosed(input, exceptionSupplier, min, max));
  }

  @Test
  void shouldVerifyRangeClosed_MinEqualsMax_InputLessThanMin_ThrowsException() {
    Float input = 0.5f;
    Float min = 1.0f;
    Float max = 1.0f;
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> FloatVerifiers.verifyRangeClosed(input, exceptionSupplier, min, max));
  }

  @Test
  void shouldVerifyRangeClosed_ExceptionSupplierIsNull_ThrowsNullPointerException() {
    Float input = 0.0f;
    Float min = -1.0f;
    Float max = 1.0f;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(
        () -> FloatVerifiers.verifyRangeClosed(input, nullSupplier, min, max));
  }
}
