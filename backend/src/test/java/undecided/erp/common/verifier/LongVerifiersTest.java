package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LongVerifiersTest {

  @Nested
  class VerifyPositiveTest {

    @Test
    public void withPositiveValue() {
      long input = 5;
      long output = LongVerifiers.verifyPositive(input, NoSuchElementException::new);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withZero() {
      long input = 0;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        LongVerifiers.verifyPositive(input, NoSuchElementException::new);
      });
    }

    @Test
    public void withNegativeValue() {
      long input = -5;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        LongVerifiers.verifyPositive(input, NoSuchElementException::new);
      });
    }

    @Test
    public void withNull() {
      Long input = null;
      Long output = LongVerifiers.verifyPositive(input, NoSuchElementException::new);
      assertThat(output).isNull();
    }

  }

  @Nested
  class VerifyPositiveOrZeroTest {

    @Test
    public void withPositiveValue() {
      Long input = 5L;
      Long output = LongVerifiers.verifyPositiveOrZero(input, NoSuchElementException::new);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withZero() {
      Long input = 0L;
      Long output = LongVerifiers.verifyPositiveOrZero(input, NoSuchElementException::new);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withNegativeValue() {
      Long input = -5L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
          () -> LongVerifiers.verifyPositiveOrZero(input, NoSuchElementException::new));
    }

    @Test
    public void withNull() {
      Long input = null;
      Long output = LongVerifiers.verifyPositiveOrZero(input, NoSuchElementException::new);
      assertThat(output).isNull();
    }
  }

  @Nested
  class VerifyNegativeTest {

    @Test
    public void withNegativeValue() {
      Long input = -5L;
      Long output = LongVerifiers.verifyNegative(input, NoSuchElementException::new);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withZero() {
      Long input = 0L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
          () -> LongVerifiers.verifyNegative(input, NoSuchElementException::new));
    }

    @Test
    public void withPositiveValue() {
      Long input = 5L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
          () -> LongVerifiers.verifyNegative(input, NoSuchElementException::new));
    }

    @Test
    public void withNull() {
      Long input = null;
      Long output = LongVerifiers.verifyNegative(input, NoSuchElementException::new);
      assertThat(output).isNull();
    }
  }

  @Nested
  class VerifyNegativeOrZeroTest {

    @Test
    public void withNegativeValue() {
      Long input = -5L;
      Long output = LongVerifiers.verifyNegativeOrZero(input, NoSuchElementException::new);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withZero() {
      Long input = 0L;
      Long output = LongVerifiers.verifyNegativeOrZero(input, NoSuchElementException::new);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withPositiveValue() {
      Long input = 5L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
          () -> LongVerifiers.verifyNegativeOrZero(input, NoSuchElementException::new));
    }

    @Test
    public void withNull() {
      Long input = null;
      Long output = LongVerifiers.verifyNegativeOrZero(input, NoSuchElementException::new);
      assertThat(output).isNull();
    }
  }

  @Nested
  class VerifyRangeClosedOpenTest {

    @Test
    public void withInRangeValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = 2L;
      Long output = LongVerifiers.verifyRangeClosedOpen(input, NoSuchElementException::new, min,
          max);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withAboveRangeValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = 11L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        LongVerifiers.verifyRangeClosedOpen(input, NoSuchElementException::new, min, max);
      });
    }

    @Test
    public void withNullValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = null;
      Long output = LongVerifiers.verifyRangeClosedOpen(input, NoSuchElementException::new, min,
          max);
      assertThat(output).isNull();
    }

    @Test
    public void withMinValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = min;
      Long output = LongVerifiers.verifyRangeClosedOpen(input, NoSuchElementException::new, min,
          max);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withMaxValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = max;
      Long output = LongVerifiers.verifyRangeClosedOpen(input, NoSuchElementException::new, min,
          max);
      assertThat(output).isEqualTo(input);
    }
  }

  @Nested
  class VerifyRangeOpenTest {

    @Test
    public void withInRangeValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = 2L;
      Long output = LongVerifiers.verifyRangeOpen(input, NoSuchElementException::new, min, max);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withBelowRangeValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = 1L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        LongVerifiers.verifyRangeOpen(input, NoSuchElementException::new, min, max);
      });
    }

    @Test
    public void withAboveRangeValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = 10L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        LongVerifiers.verifyRangeOpen(input, NoSuchElementException::new, min, max);
      });
    }

    @Test
    public void withNullValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = null;
      Long output = LongVerifiers.verifyRangeOpen(input, NullPointerException::new, min, max);
      assertThat(output).isNull();
    }

    @Test
    public void withMinPlusOneValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = min + 1;
      Long output = LongVerifiers.verifyRangeOpen(input, NoSuchElementException::new, min, max);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withMaxMinusOneValue() {
      Long min = 1L;
      Long max = 10L;
      Long input = max - 1;
      Long output = LongVerifiers.verifyRangeOpen(input, NoSuchElementException::new, min, max);
      assertThat(output).isEqualTo(input);
    }
  }

  @Nested
  class VerifyGreaterThanTest {

    @Test
    public void withLessValue() {
      Long min = 5L;
      Long input = 4L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
          () -> LongVerifiers.verifyGreaterThan(input, NoSuchElementException::new, min));
    }

    @Test
    public void withEqualValue() {
      Long min = 5L;
      Long input = 5L;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
          () -> LongVerifiers.verifyGreaterThan(input, NoSuchElementException::new, min));
    }

    @Test
    public void withGreaterValue() {
      Long min = 5L;
      Long input = 6L;
      Long output = LongVerifiers.verifyGreaterThan(input, NoSuchElementException::new, min);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withMinPlusOneValue() {
      Long min = 5L;
      Long input = min + 1;
      Long output = LongVerifiers.verifyGreaterThan(input, NoSuchElementException::new, min);
      assertThat(output).isEqualTo(input);
    }

    @Test
    public void withMinValue() {
      Long min = 5L;
      Long input = min;
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
          () -> LongVerifiers.verifyGreaterThan(input, NoSuchElementException::new, min));
    }
  }
}
