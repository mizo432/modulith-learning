package undecided.shared.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalTime;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LocalTimePreconditionTest {

  @Nested
  class VerifyRangeClosedTest {

    @Test
    void shouldNotThrowExceptionWhenTimeIsWithinRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(15, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimePrecondition.checkRangeClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenTimeIsOutsideRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(9, 59);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> LocalTimePrecondition.checkRangeClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldNotThrowExceptionWhenTimeIsOnBoundary() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(10, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimePrecondition.checkRangeClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenReferenceIsNull() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(
              () -> LocalTimePrecondition.checkRangeClosed(null, exceptionSupplier, min, max));
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
      LocalTimePrecondition.checkRangeOpen(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenTimeIsOutsideRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(9, 59);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(() -> LocalTimePrecondition.checkRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldNotThrowExceptionWhenTimeIsOnBoundary() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(() -> LocalTimePrecondition.checkRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldThrowExceptionWhenReferenceIsNull() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimePrecondition.checkRangeOpen(null, exceptionSupplier, min, max);
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
      LocalTimePrecondition.checkRangeOpenClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldThrowExceptionWhenTimeIsOutsideRange() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(9, 59);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> LocalTimePrecondition.checkRangeOpenClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void shouldNotThrowExceptionWhenTimeIsOnBoundary() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      LocalTime ref = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      LocalTimePrecondition.checkRangeOpenClosed(ref, exceptionSupplier, min, max);
    }

    @Test
    void shouldNullWhenReferenceIsNull() {
      LocalTime min = LocalTime.of(10, 0);
      LocalTime max = LocalTime.of(20, 0);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;

      assertThat(LocalTimePrecondition.checkRangeOpenClosed(null, exceptionSupplier, min, max))
          .isNull();
    }
  }
}
