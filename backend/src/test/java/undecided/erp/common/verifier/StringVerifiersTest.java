package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StringVerifiersTest {

  @Nested
  class VerifyNonEmptyTest {

    @Test
    void whenInputIsNonEmptyShouldReturnSameString() {
      String input = "Test";
      NoSuchElementException e = new NoSuchElementException();

      String result = StringVerifiers.verifyNonEmpty(input, () -> e);

      assertThat(result).isEqualTo(input);
    }

    @Test
    void whenInputIsEmptyShouldThrowException() {
      String input = "";
      NoSuchElementException e = new NoSuchElementException();

      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        StringVerifiers.verifyNonEmpty(input, () -> e);
      });
    }

    @Test
    void whenInputIsNullShouldReturnNull() {
      String input = null;
      NoSuchElementException e = new NoSuchElementException();

      String result = StringVerifiers.verifyNonEmpty(input, () -> e);

      assertThat(result).isEqualTo(null);
    }

  }

  @Nested
  class verifyHalfWidthLengthOpenTest {

    @Test
    void whenStringIsWithinRangeShouldReturnSameString() {
      String input = "Test";
      int min = 0;
      int max = 10;
      NoSuchElementException e = new NoSuchElementException();

      String result = StringVerifiers.verifyHalfWidthLengthOpen(input, () -> e, min, max);

      assertThat(result).isEqualTo(input);
    }

    @Test
    void whenStringIsNotWithinRangeShouldThrowException() {
      String input = "Test";
      int min = 5;
      int max = 10;
      NoSuchElementException e = new NoSuchElementException();

      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        StringVerifiers.verifyHalfWidthLengthOpen(input, () -> e, min, max);
      });
    }

    @Test
    void whenStringIsNullShouldReturnNull() {
      String input = null;
      int min = 0;
      int max = 10;
      NoSuchElementException e = new NoSuchElementException();

      String result = StringVerifiers.verifyHalfWidthLengthOpen(input, () -> e, min, max);

      assertThat(result).isNull();
      ;
    }

  }

  @Nested
  class VerifyHalfWidthLengthAtLestTest {

    @Test
    void whenStringWithinMinRangeShouldReturnSameString() {
      String input = "Test";
      int min = 3;
      NoSuchElementException e = new NoSuchElementException();

      String result = StringVerifiers.verifyHalfWidthLengthAtLest(input, () -> e, min);

      assertThat(result).isEqualTo(input);
    }

    @Test
    void whenStringBelowMinRangeShouldThrowException() {
      String input = "Te";
      int min = 3;
      NoSuchElementException e = new NoSuchElementException();

      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
        StringVerifiers.verifyHalfWidthLengthAtLest(input, () -> e, min);
      });
    }

    @Test
    void whenInputIsNullShouldReturnNull() {
      String input = null;
      int min = 0;
      NoSuchElementException e = new NoSuchElementException();

      String result = StringVerifiers.verifyHalfWidthLengthAtLest(input, () -> e, min);

      assertThat(result).isNull();
    }

  }

}

@Nested
class VerifyHalfWidthLengthClosedTest {

  @Test
  void whenStringLengthIsWithinIntervalShouldReturnSameString() {
    String input = "Test";
    int min = 1;
    int max = 7;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthClosed(input, () -> e, min, max);

    assertThat(result).isEqualTo(input);
  }

  @Test
  void whenStringLengthExceedsIntervalShouldThrowException() {
    String input = "This Input is definitely too long";
    int min = 1;
    int max = 7;
    NoSuchElementException e = new NoSuchElementException();

    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
      StringVerifiers.verifyHalfWidthLengthClosed(input, () -> e, min, max);
    });
  }

  @Test
  void whenStringBelowIntervalShouldThrowException() {
    String input = "T";
    int min = 2;
    int max = 7;
    NoSuchElementException e = new NoSuchElementException();

    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
      StringVerifiers.verifyHalfWidthLengthClosed(input, () -> e, min, max);
    });
  }

  @Test
  void whenInputIsNullShouldReturnNull() {
    String input = null;
    int min = 1;
    int max = 7;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthClosed(input, () -> e, min, max);

    assertThat(result).isNull();
  }
}

@Nested
class VerifyHalfWidthLengthAtMostTest {

  @Test
  void whenStringWithinMaxRangeShouldReturnSameString() {
    String input = "Test";
    int max = 7;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthAtMost(input, () -> e, max);

    assertThat(result).isEqualTo(input);
  }

  @Test
  void whenStringExceedsMaxRangeShouldThrowException() {
    String input = "This Input is definitely too long";
    int max = 7;
    NoSuchElementException e = new NoSuchElementException();

    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
      StringVerifiers.verifyHalfWidthLengthAtMost(input, () -> e, max);
    });
  }

  @Test
  void whenInputIsNullShouldReturnNull() {
    String input = null;
    int max = 7;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthAtMost(input, () -> e, max);

    assertThat(result).isNull();
  }
}

@Nested
class VerifyHalfWidthLengthLessThanTest {

  @Test
  void whenStringIsLessThanMaxLengthShouldReturnSameString() {
    String input = "Test";
    int max = 5;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthLessThan(input, () -> e, max);

    assertThat(result).isEqualTo(input);
  }

  @Test
  void whenStringIsNotLessThanMaxLengthShouldThrowException() {
    String input = "This string is definitely too long";
    int max = 5;
    NoSuchElementException e = new NoSuchElementException();

    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
      StringVerifiers.verifyHalfWidthLengthLessThan(input, () -> e, max);
    });
  }

  @Test
  void whenInputIsNullShouldReturnNull() {
    String input = null;
    int max = 5;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthLessThan(input, () -> e, max);

    assertThat(result).isNull();
  }
}

@Nested
class VerifyHalfWidthLengthGraterThanTest {

  @Test
  void whenStringIsGreaterThanMinLengthShouldReturnSameString() {
    String input = "Test";
    int min = 3;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthGraterThan(input, () -> e, min);

    assertThat(result).isEqualTo(input);
  }

  @Test
  void whenStringIsNotGreaterThanMinLengthShouldThrowException() {
    String input = "Te";
    int min = 3;
    NoSuchElementException e = new NoSuchElementException();

    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
      StringVerifiers.verifyHalfWidthLengthGraterThan(input, () -> e, min);
    });
  }

  @Test
  void whenInputIsNullShouldReturnNull() {
    String input = null;
    int min = 0;
    NoSuchElementException e = new NoSuchElementException();

    String result = StringVerifiers.verifyHalfWidthLengthGraterThan(input, () -> e, min);

    assertThat(result).isNull();
  }
}
