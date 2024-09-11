package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalTime;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LocalTimeVerifiersTest {

  @Nested
  class VerifyRangeClosedTest {

    @Test
    void shouldNotThrowExceptionWhenTimeIsWithinRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(15, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimeVerifiers.verifyRangeClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenTimeIsOutsideRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(9, 59);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalTimeVerifiers.verifyRangeClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldNotThrowExceptionWhenTimeIsOnBoundary() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(10, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimeVerifiers.verifyRangeClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenReferenceIsNull() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> LocalTimeVerifiers.verifyRangeClosed(null, exceptionSupplier, min, max));
    }

  }

  @Nested
  class VerifyRangeOpenTest {

    @Test
    void shouldNotThrowExceptionWhenTimeIsWithinRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(15, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenTimeIsOutsideRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(9, 59);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldNotThrowExceptionWhenTimeIsOnBoundary() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldThrowExceptionWhenReferenceIsNull() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimeVerifiers.verifyRangeOpen(null, exceptionSupplier, min, max);
    }

  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @Test
    void shouldNotThrowExceptionWhenTimeIsWithinRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(15, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenTimeIsOutsideRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(9, 59);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldNotThrowExceptionWhenTimeIsOnBoundary() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldNullWhenReferenceIsNull() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;

      assertThat(
          LocalTimeVerifiers.verifyRangeOpenClosed(null, exceptionSupplier, min, max)).isNull();
    }
  }
}


