package undecided.shared.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class DoublePreconditionTest {

  private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

  @Test
  void shouldVerifyPositive_InputIsPositive_ReturnsSameValue() {
    Double input = 1.5;
    Double output = DoublePrecondition.verifyPositive(input, exceptionSupplier);
    assertThat(output).isEqualTo(input);
  }

  // <省略している他のテストケース>

  @Test
  void shouldThrowExceptionForVerifyPositive_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyPositive(input, nullSupplier));
  }

  @Test
  void shouldThrowExceptionForVerifyPositiveOrZero_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyPositiveOrZero(input, nullSupplier));
  }

  @Test
  void shouldThrowExceptionForVerifyNegative_ExceptionSupplierIsNull() {
    Double input = -1.5;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyNegative(input, nullSupplier));
  }

  @Test
  void shouldThrowExceptionForVerifyNegativeOrZero_ExceptionSupplierIsNull() {
    Double input = -1.5;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyNegativeOrZero(input, nullSupplier));
  }

  @Test
  void shouldThrowExceptionForVerifyRangeClosed_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double min = 0.0;
    Double max = 2.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyRangeClosed(input, nullSupplier, min, max));
  }

  @Test
  void shouldThrowExceptionForVerifyRangeOpen_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double min = 0.0;
    Double max = 2.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyRangeOpen(input, nullSupplier, min, max));
  }

  @Test
  void shouldThrowExceptionForVerifyRangeClosedOpen_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double min = 0.0;
    Double max = 2.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyRangeClosedOpen(input, nullSupplier, min, max));
  }

  @Test
  void shouldThrowExceptionForVerifyRangeOpenClosed_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double min = 0.0;
    Double max = 2.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyRangeOpenClosed(input, nullSupplier, min, max));
  }

  @Test
  void shouldThrowExceptionForVerifyAtLeast_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double min = 1.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyAtLest(input, nullSupplier, min));
  }

  @Test
  void shouldThrowExceptionForVerifyAtMost_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double max = 2.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyAtMost(input, nullSupplier, max));
  }

  @Test
  void shouldThrowExceptionForVerifyLessThan_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double max = 2.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyLessThan(input, nullSupplier, max));
  }

  @Test
  void shouldThrowExceptionForVerifyGreaterThan_ExceptionSupplierIsNull() {
    Double input = 1.5;
    Double min = 1.0;
    Supplier<RuntimeException> nullSupplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> DoublePrecondition.verifyGreaterThan(input, nullSupplier, min));
  }

  @Test
  void shouldReturnGivenRefForVerifyLessThan_whenRefIsLessThanMax() {
    Double ref = 1.5;
    Double max = 2.0;
    Double output = DoublePrecondition.verifyLessThan(ref, exceptionSupplier, max);
    assertThat(output).isEqualTo(ref);
  }

  @Test
  void shouldThrowIllegalArgumentExceptionForVerifyLessThan_whenRefIsSameAsMax() {
    Double ref = 2.0;
    Double max = 2.0;
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> DoublePrecondition.verifyLessThan(ref, exceptionSupplier, max));
  }

  @Test
  void shouldThrowIllegalArgumentExceptionForVerifyLessThan_whenRefIsGreaterThanMax() {
    Double ref = 2.5;
    Double max = 2.0;
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> DoublePrecondition.verifyLessThan(ref, exceptionSupplier, max));
  }

  @Test
  void shouldReturnNullForVerifyLessThan_whenRefIsNull() {
    Double ref = null;
    Double max = 2.0;
    Double output = DoublePrecondition.verifyLessThan(ref, exceptionSupplier, max);
    assertThat(output).isNull();
  }

  @Test
  void shouldReturnGivenRefForVerifyGreaterThan_whenRefIsGreaterThanMin() {
    Double ref = 2.5;
    Double min = 2.0;
    Double output = DoublePrecondition.verifyGreaterThan(ref, exceptionSupplier, min);
    assertThat(output).isEqualTo(ref);
  }

  @Test
  void shouldThrowIllegalArgumentExceptionForVerifyGreaterThan_whenRefIsSameAsMin() {
    Double ref = 2.0;
    Double min = 2.0;
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> DoublePrecondition.verifyGreaterThan(ref, exceptionSupplier, min));
  }

  @Test
  void shouldThrowIllegalArgumentExceptionForVerifyGreaterThan_whenRefIsLessThanMin() {
    Double ref = 1.5;
    Double min = 2.0;
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> DoublePrecondition.verifyGreaterThan(ref, exceptionSupplier, min));
  }

  @Test
  void shouldReturnNullForVerifyGreaterThan_whenRefIsNull() {
    Double ref = null;
    Double min = 2.0;
    Double output = DoublePrecondition.verifyGreaterThan(ref, exceptionSupplier, min);
    assertThat(output).isNull();
  }
}
