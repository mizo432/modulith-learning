package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LocalDateTimeVerifiersTest {

  private LocalDateTime ref = LocalDateTime.now();
  private LocalDateTime min = ref.minusDays(1);
  private LocalDateTime max = ref.plusDays(1);
  private final Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;

  @Nested
  class VerifyRangeClosedTest {

    @BeforeEach
    void setUp() {
      ref = LocalDateTime.now();
      min = ref.minusDays(1);
      max = ref.plusDays(1);

    }

    @Test
    void whenRefIsInRange() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeClosed(ref, exceptionSupplier, min,
          max);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsOnLowerBound() {
      LocalDateTime min = ref;
      LocalDateTime max = ref.plusDays(1);
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeClosed(ref, exceptionSupplier, min,
          max);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsOnUpperBound() {
      LocalDateTime max = ref;
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeClosed(ref, exceptionSupplier, min,
          max);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsOutOfRange() {
      LocalDateTime min = ref.plusDays(1);
      LocalDateTime max = ref.plusDays(2);
      Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeClosed(ref, exceptionSupplier, min, max));
    }

  }

  @Nested
  class VerifyRangeOpenTest {

    @BeforeEach
    void setUp() {
      ref = LocalDateTime.now();
      min = ref.minusDays(1);
      max = ref.plusDays(1);
    }

    @Test
    void whenRefIsInRange() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min,
          max);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsJustBelowLowerBound() {
      LocalDateTime min = ref.plusSeconds(1);
      LocalDateTime max = ref.plusDays(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void whenRefIsJustAboveUpperBound() {
      LocalDateTime min = ref.minusDays(1);
      LocalDateTime max = ref.minusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void whenRefIsOnLowerBound() {
      LocalDateTime min = ref;
      LocalDateTime max = ref.plusDays(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void whenRefIsOnUpperBound() {
      LocalDateTime min = ref.minusDays(1);
      LocalDateTime max = ref;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeOpen(ref, exceptionSupplier, min, max));
    }
  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @BeforeEach
    void setUp() {
      ref = LocalDateTime.now();
      min = ref.minusDays(1);
      max = ref.plusDays(1);
    }

    @Test
    void whenRefIsInRange() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier,
          min, max);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsAtLeastMin() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyAtLest(ref, exceptionSupplier, min);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsBelowMin() {
      LocalDateTime min = ref.plusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyAtLest(ref, exceptionSupplier, min));
    }

    @Test
    void whenRefIsJustBelowLowerBound() {
      LocalDateTime min = ref.plusSeconds(1);
      LocalDateTime max = ref.plusDays(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void whenRefIsJustAboveUpperBound() {
      LocalDateTime min = ref.minusDays(1);
      LocalDateTime max = ref.minusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void whenRefIsOnLowerBound() {
      LocalDateTime min = ref;
      LocalDateTime max = ref.plusDays(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void whenRefIsOnUpperBound() {
      LocalDateTime min = ref.minusDays(1);
      LocalDateTime max = ref;
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeOpenClosed(ref, exceptionSupplier,
          min, max);
      assertThat(actual).isEqualTo(ref);
    }
  }

  @Nested
  class VerifyRangeClosedOpenTest {

    @BeforeEach
    void setUp() {
      ref = LocalDateTime.now();
      min = ref.minusDays(1);
      max = ref.plusDays(1);
    }

    @Test
    void whenRefIsInRange() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeClosedOpen(ref, exceptionSupplier,
          min, max);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsOnLowerBound() {
      LocalDateTime min = ref;
      LocalDateTime actual = LocalDateTimeVerifiers.verifyRangeClosedOpen(ref, exceptionSupplier,
          min, max);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsOutsideUpperBound() {
      LocalDateTime max = ref.minusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeClosedOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void whenRefIsOnUpperBound() {
      LocalDateTime max = ref;
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeClosedOpen(ref, exceptionSupplier, min, max));

    }

    @Test
    void whenRefIsOutsideLowerBound() {
      LocalDateTime min = ref.plusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyRangeClosedOpen(ref, exceptionSupplier, min, max));
    }
  }

  @Nested
  class VerifyAtLeastTest {

    @BeforeEach
    void setUp() {
      ref = LocalDateTime.now();
      min = ref.minusDays(1);
      max = ref.plusDays(1);
    }

    @Test
    void whenRefIsAtLeastMin() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyAtLest(ref, exceptionSupplier, min);
      assertThat(actual).isEqualTo(ref);
    }

    @Test
    void whenRefIsBelowMin() {
      LocalDateTime min = ref.plusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyAtLest(ref, exceptionSupplier, min));
    }

    @Nested
    class VerifyAtMostTest {

      @BeforeEach
      void setUp() {
        ref = LocalDateTime.now();
        LocalDateTime max = ref.plusDays(1);
      }

      @Test
      void whenRefIsAtMostMax() {
        LocalDateTime actual = LocalDateTimeVerifiers.verifyAtMost(ref, exceptionSupplier, max);
        assertThat(actual).isEqualTo(ref);
      }

      @Test
      void whenRefIsAboveMax() {
        LocalDateTime max = ref.minusSeconds(1);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
            () -> LocalDateTimeVerifiers.verifyAtMost(ref, exceptionSupplier, max));
      }
    }
  }

  @Nested
  class VerifyLessThanTest {

    @BeforeEach
    void setUp() {
      ref = LocalDateTime.now();
      LocalDateTime max = ref.plusDays(1);
    }

    @Test
    void whenRefIsLessThanMax() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyLessThan(ref.minusDays(1),
          exceptionSupplier, max);
      assertThat(actual).isEqualTo(ref.minusDays(1));
    }

    @Test
    void whenRefIsNotLessThanMax() {
      LocalDateTime max = ref.minusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyLessThan(ref, exceptionSupplier, max));
    }
  }

  @Nested
  class VerifyGreaterThanTest {

    @BeforeEach
    void setUp() {
      ref = LocalDateTime.now();
      LocalDateTime min = ref.minusDays(1);
    }

    @Test
    void whenRefIsGreaterThanMin() {
      LocalDateTime actual = LocalDateTimeVerifiers.verifyGreaterThan(ref.plusDays(1),
          exceptionSupplier, min);
      assertThat(actual).isEqualTo(ref.plusDays(1));
    }

    @Test
    void whenRefIsNotGreaterThanMin() {
      LocalDateTime min = ref.plusSeconds(1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateTimeVerifiers.verifyGreaterThan(ref, exceptionSupplier, min));
    }
  }
}
