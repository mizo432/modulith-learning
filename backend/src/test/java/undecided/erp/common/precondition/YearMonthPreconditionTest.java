package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.YearMonth;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class YearMonthPreconditionTest {

  final Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException(
      "YearMonth value is out of range");
  private final YearMonth ref = YearMonth.of(2024, 6);
  private final YearMonth min = YearMonth.of(2024, 1);
  private final YearMonth max = YearMonth.of(2024, 12);

  @Nested
  class VerifyRangeClosedTest {

    @Test
    void validRange_NoException() {
      YearMonth result = YearMonthPrecondition.checkRangeClosed(ref, exceptionSupplier, min, max);

      assertThat(result).isEqualTo(ref);
    }

    @Test
    void valueSmallerThanRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2023, 12);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.checkRangeClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void valueLargerThanRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2025, 1);

      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.checkRangeClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void minAndMaxSameAsValue_NoException() {
      YearMonth min = YearMonth.of(2024, 6);
      YearMonth max = YearMonth.of(2024, 6);

      YearMonth result = YearMonthPrecondition.checkRangeClosed(ref, exceptionSupplier, min, max);

      assertThat(result).isEqualTo(ref);
    }

  }

  @Nested
  class VerifyRangeOpenTest {

    @Test
    void validRange_NoException() {
      YearMonth result = YearMonthPrecondition.checkRangeOpen(ref, exceptionSupplier, min, max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void valueSmallerThanRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2023, 12);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.checkRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void valueEqualToMinRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2024, 1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.checkRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void valueEqualToMaxRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2024, 12);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.checkRangeOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void minAndMaxSameAsValue_ThrowsException() {
      YearMonth min = YearMonth.of(2024, 6);
      YearMonth max = YearMonth.of(2024, 6);

      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.checkRangeOpen(ref, exceptionSupplier, min, max));
    }

  }

  @Nested
  class VerifyRangeClosedOpenTest {

    @Test
    void validRange_NoException() {
      YearMonth result = YearMonthPrecondition.verifyRangeClosedOpen(ref, exceptionSupplier, min,
          max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void valueSmallerThanRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2023, 12);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.verifyRangeClosedOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void valueEqualToMinRange_NoException() {
      YearMonth ref = YearMonth.of(2024, 1);
      YearMonth result = YearMonthPrecondition.verifyRangeClosedOpen(ref, exceptionSupplier, min,
          max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void valueEqualToMaxRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2024, 12);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.verifyRangeClosedOpen(ref, exceptionSupplier, min, max));
    }

    @Test
    void minAndMaxSameAsValue_ThrowsException() {
      YearMonth min = YearMonth.of(2024, 6);
      YearMonth max = YearMonth.of(2024, 6);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.verifyRangeClosedOpen(ref, exceptionSupplier, min, max));
    }
  }

  @Nested
  class VerifyAtLeastTest {

    @Test
    void validRange_NoException() {
      YearMonth min = YearMonth.of(2024, 5);
      YearMonth result = YearMonthPrecondition.verifyAtLest(ref, exceptionSupplier, min);

      assertThat(result).isEqualTo(ref);
    }

    @Test
    void valueSmallerThanMin_ThrowsException() {
      YearMonth ref = YearMonth.of(2023, 12);

      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.verifyAtLest(ref, exceptionSupplier, min));
    }

    @Test
    void valueEqualToMin_NoException() {
      YearMonth min = YearMonth.of(2024, 6);

      YearMonth result = YearMonthPrecondition.verifyAtLest(ref, exceptionSupplier, min);

      assertThat(result).isEqualTo(ref);
    }
  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @Test
    void validRange_NoException() {
      YearMonth result = YearMonthPrecondition.verifyRangeOpenClosed(ref, exceptionSupplier, min,
          max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void valueSmallerThanRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2023, 12);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.verifyRangeOpenClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void valueEqualToMinRange_ThrowsException() {
      YearMonth ref = YearMonth.of(2024, 1);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.verifyRangeOpenClosed(ref, exceptionSupplier, min, max));
    }

    @Test
    void valueEqualToMaxRange_NoException() {
      YearMonth ref = YearMonth.of(2024, 12);
      YearMonth result = YearMonthPrecondition.verifyRangeOpenClosed(ref, exceptionSupplier, min,
          max);
      assertThat(result).isEqualTo(ref);
    }

    @Test
    void minAndMaxSameAsValue_ThrowsException() {
      YearMonth min = YearMonth.of(2024, 6);
      YearMonth max = YearMonth.of(2024, 6);
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> YearMonthPrecondition.verifyRangeOpenClosed(ref, exceptionSupplier, min, max));
    }
  }


}
