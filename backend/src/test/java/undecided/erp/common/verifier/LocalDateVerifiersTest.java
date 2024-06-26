package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import java.util.function.Supplier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LocalDateVerifiersTest {

  private LocalDate createDate(int year, int month, int day) {
    return LocalDate.of(year, month, day);
  }

  private Supplier<RuntimeException> createException(String message) {
    return () -> new IllegalArgumentException(message);
  }

  @Nested
  class VerifyRangeClosedTest {

    @Test
    void shouldThrowExceptionWhenDateOutOfRange() {
      LocalDate minDate = createDate(2022, 4, 10);
      LocalDate maxDate = createDate(2022, 4, 20);
      Supplier<RuntimeException> exceptionSupplier = createException("LocalDate out of range");

      assertDateWithinRange(createDate(2022, 4, 15), exceptionSupplier, minDate, maxDate);
      assertExceptionOnDateOutOfRange(createDate(2022, 4, 9), exceptionSupplier, minDate, maxDate);
      assertExceptionOnDateOutOfRange(createDate(2022, 4, 21), exceptionSupplier, minDate, maxDate);
    }

    private void assertDateWithinRange(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThat(LocalDateVerifiers.verifyRangeClosed(dateToTest, exceptionSupplier, minDate,
          maxDate)).isEqualTo(dateToTest);
    }

    private void assertExceptionOnDateOutOfRange(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateVerifiers.verifyRangeClosed(dateToTest, exceptionSupplier, minDate,
              maxDate));
    }
  }

  @Nested
  class VerifyRangeOpenTest {

    @Test
    void shouldThrowExceptionWhenDateOutOfRangeOpenRange() {
      LocalDate minDate = createDate(2022, 5, 5);
      LocalDate maxDate = createDate(2022, 5, 15);
      Supplier<RuntimeException> exceptionSupplier = createException(
          "LocalDate is out of open range");

      assertDateWithinOpenRange(createDate(2022, 5, 6), exceptionSupplier, minDate, maxDate);
      assertExceptionOnDateOutOfRangeOpenRange(createDate(2022, 5, 5), exceptionSupplier, minDate,
          maxDate);
      assertExceptionOnDateOutOfRangeOpenRange(createDate(2022, 5, 15), exceptionSupplier, minDate,
          maxDate);
    }

    private void assertDateWithinOpenRange(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThat(
          LocalDateVerifiers.verifyRangeOpen(dateToTest, exceptionSupplier, minDate, maxDate))
          .isEqualTo(dateToTest);
    }

    private void assertExceptionOnDateOutOfRangeOpenRange(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> LocalDateVerifiers.verifyRangeOpen(dateToTest, exceptionSupplier, minDate,
                  maxDate));
    }
  }

  @Nested
  class VerifyRangeClosedOpenTest {

    @Test
    void shouldThrowExceptionWhenDateOutOfRangeClosedOpen() {
      LocalDate minDate = createDate(2023, 6, 10);
      LocalDate maxDate = createDate(2023, 6, 20);
      Supplier<RuntimeException> exceptionSupplier = createException(
          "LocalDate is out of closed-open range");

      assertDateWithinClosedOpenRange(createDate(2023, 6, 10), exceptionSupplier, minDate, maxDate);
      assertDateWithinClosedOpenRange(createDate(2023, 6, 19), exceptionSupplier, minDate, maxDate);
      assertExceptionOnDateOutOfRangeClosedOpen(createDate(2023, 6, 20), exceptionSupplier, minDate,
          maxDate);
      assertExceptionOnDateOutOfRangeClosedOpen(createDate(2023, 6, 9), exceptionSupplier, minDate,
          maxDate);
    }

    private void assertDateWithinClosedOpenRange(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThat(
          LocalDateVerifiers.verifyRangeClosedOpen(dateToTest, exceptionSupplier, minDate, maxDate))
          .isEqualTo(dateToTest);
    }

    private void assertExceptionOnDateOutOfRangeClosedOpen(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> LocalDateVerifiers.verifyRangeClosedOpen(dateToTest, exceptionSupplier, minDate,
                  maxDate));
    }
  }

  @Nested
  class VerifyRangeOpenClosedTest {

    @Test
    void shouldThrowExceptionWhenDateOutOfRangeOpenClosed() {
      LocalDate minDate = createDate(2024, 7, 5);
      LocalDate maxDate = createDate(2024, 7, 15);
      Supplier<RuntimeException> exceptionSupplier = createException(
          "LocalDate is out of open-closed range");

      assertDateWithinOpenClosedRange(createDate(2024, 7, 6), exceptionSupplier, minDate, maxDate);
      assertExceptionOnDateOutOfRangeOpenClosed(createDate(2024, 7, 5), exceptionSupplier, minDate,
          maxDate);
    }

    private void assertDateWithinOpenClosedRange(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThat(
          LocalDateVerifiers.verifyRangeOpenClosed(dateToTest, exceptionSupplier, minDate, maxDate))
          .isEqualTo(dateToTest);
    }

    private void assertExceptionOnDateOutOfRangeOpenClosed(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate, LocalDate maxDate) {
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () -> LocalDateVerifiers.verifyRangeOpenClosed(dateToTest, exceptionSupplier, minDate,
                  maxDate));
    }
  }

  @Nested
  class VerifyAtLestTest {

    @Test
    void shouldThrowExceptionWhenDateIsBeforeMinimum() {
      LocalDate minDate = createDate(2022, 7, 5);
      Supplier<RuntimeException> exceptionSupplier = createException(
          "LocalDate is before minimum valid date");

      assertExceptionOnDateBeforeMinimum(createDate(2022, 7, 4), exceptionSupplier, minDate);
      assertValidMinimumDate(createDate(2022, 7, 5), exceptionSupplier, minDate);
      assertValidMinimumDate(createDate(2022, 7, 6), exceptionSupplier, minDate);
    }

    private void assertExceptionOnDateBeforeMinimum(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate) {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateVerifiers.verifyAtLest(dateToTest, exceptionSupplier, minDate));
    }

    private void assertValidMinimumDate(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate) {
      assertThat(LocalDateVerifiers.verifyAtLest(dateToTest, exceptionSupplier, minDate)).isEqualTo(
          dateToTest);
    }
  }

  @Nested
  class VerifyAtMostTest {

    @Test
    void shouldThrowExceptionWhenDateIsAfterMaximum() {
      LocalDate maxDate = createDate(2023, 8, 10);
      Supplier<RuntimeException> exceptionSupplier = createException(
          "LocalDate is after maximum valid date");

      assertExceptionOnDateAfterMaximum(createDate(2023, 8, 11), exceptionSupplier, maxDate);
      assertValidMaximumDate(createDate(2023, 8, 10), exceptionSupplier, maxDate);
      assertValidMaximumDate(createDate(2023, 8, 9), exceptionSupplier, maxDate);
    }

    private void assertExceptionOnDateAfterMaximum(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate maxDate) {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateVerifiers.verifyAtMost(dateToTest, exceptionSupplier, maxDate));
    }

    private void assertValidMaximumDate(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate maxDate) {
      assertThat(LocalDateVerifiers.verifyAtMost(dateToTest, exceptionSupplier, maxDate)).isEqualTo(
          dateToTest);
    }
  }

  @Nested
  class VerifyLessThanTest {

    @Test
    void shouldThrowExceptionWhenDateIsNotLessThanMaximum() {
      LocalDate maxDate = createDate(2023, 8, 10);
      Supplier<RuntimeException> exceptionSupplier = createException(
          "LocalDate is not less than maximum valid date");

      assertExceptionOnDateNotLessThanMaximum(createDate(2023, 8, 10), exceptionSupplier, maxDate);
      assertExceptionOnDateNotLessThanMaximum(createDate(2023, 8, 11), exceptionSupplier, maxDate);
      assertValidLessThanDate(createDate(2023, 8, 9), exceptionSupplier, maxDate);
    }

    private void assertExceptionOnDateNotLessThanMaximum(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate maxDate) {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateVerifiers.verifyLessThan(dateToTest, exceptionSupplier, maxDate));
    }

    private void assertValidLessThanDate(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate maxDate) {
      assertThat(
          LocalDateVerifiers.verifyLessThan(dateToTest, exceptionSupplier, maxDate)).isEqualTo(
          dateToTest);
    }
  }

  @Nested
  class VerifyGreaterThanTest {

    @Test
    void shouldThrowExceptionWhenDateNotGreaterThanMinimum() {
      LocalDate minDate = createDate(2024, 9, 20);
      Supplier<RuntimeException> exceptionSupplier = createException(
          "LocalDate is not greater than minimum valid date");

      assertExceptionOnDateNotGreaterThanMinimum(createDate(2024, 9, 19), exceptionSupplier,
          minDate);
      assertExceptionOnDateNotGreaterThanMinimum(createDate(2024, 9, 20), exceptionSupplier,
          minDate);
      assertValidGreaterThanDate(createDate(2024, 9, 21), exceptionSupplier, minDate);
    }

    private void assertExceptionOnDateNotGreaterThanMinimum(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate) {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(
          () -> LocalDateVerifiers.verifyGreaterThan(dateToTest, exceptionSupplier, minDate));
    }

    private void assertValidGreaterThanDate(LocalDate dateToTest,
        Supplier<RuntimeException> exceptionSupplier, LocalDate minDate) {
      assertThat(
          LocalDateVerifiers.verifyGreaterThan(dateToTest, exceptionSupplier, minDate)).isEqualTo(
          dateToTest);
    }
  }
}
