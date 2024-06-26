package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BooleanVerifiersTest {

  @Nested
  class VerifyTrue {

    @Test
    void shouldNotThrowExceptionWhenVerifyTrueWithTrueBoolean() {
      AtomicBoolean exceptionThrown = new AtomicBoolean(false);
      try {
        BooleanVerifiers.verifyTrue(true,
            () -> new RuntimeException("Exception thrown on verify true"));
      } catch (RuntimeException e) {
        exceptionThrown.set(true);
      }
      assertThat(exceptionThrown.get()).as(
          "Exception unexpectedly thrown on verify true method with true boolean").isFalse();
    }

    @Test
    void shouldThrowExceptionWhenVerifyTrueWithFalseBoolean() {
      AtomicBoolean exceptionThrown = new AtomicBoolean(false);
      try {
        BooleanVerifiers.verifyTrue(false,
            () -> new RuntimeException("Exception thrown on verify true"));
      } catch (RuntimeException e) {
        exceptionThrown.set(true);
      }
      assertThat(exceptionThrown.get()).as(
          "Expected exception on verify true method with false boolean").isTrue();
    }

    @Test
    void shouldReturnExpectedValueWhenValidateNullWithVerifyTrue() {
      AtomicBoolean exceptionThrown = new AtomicBoolean(false);
      Boolean result = false;
      try {
        result = BooleanVerifiers.verifyTrue(null,
            () -> new RuntimeException("Exception on verify true"));
      } catch (RuntimeException e) {
        exceptionThrown.set(true);
      }
      assertThat(result).as("Result should have been null").isNull();
      assertThat(exceptionThrown.get()).as(
          "Exception unexpectedly thrown on verify true method with null boolean").isFalse();
    }

  }


  @Nested
  class VerifyFalse {

    @Test
    void shouldNotThrowExceptionWhenVerifyFalseWithFalseBoolean() {
      AtomicBoolean exceptionThrown = new AtomicBoolean(false);
      try {
        BooleanVerifiers.verifyFalse(false,
            () -> new RuntimeException("Exception thrown on verify false"));
      } catch (RuntimeException e) {
        exceptionThrown.set(true);
      }
      assertThat(exceptionThrown.get()).as(
          "Exception unexpectedly thrown on verify false method with false boolean").isFalse();
    }

    @Test
    void shouldThrowExceptionWhenVerifyFalseWithTrueBoolean() {
      AtomicBoolean exceptionThrown = new AtomicBoolean(false);
      try {
        BooleanVerifiers.verifyFalse(true,
            () -> new RuntimeException("Exception thrown on verify false"));
      } catch (RuntimeException e) {
        exceptionThrown.set(true);
      }
      assertThat(exceptionThrown.get()).as(
          "Expected exception on verify false method with true boolean").isTrue();
    }

    @Test
    void shouldReturnExpectedValueWhenValidateNullWithVerifyFalse() {
      AtomicBoolean exceptionThrown = new AtomicBoolean(false);
      Boolean result = false;
      try {
        result = BooleanVerifiers.verifyFalse(null,
            () -> new RuntimeException("Exception on verify false"));
      } catch (RuntimeException e) {
        exceptionThrown.set(true);
      }
      assertThat(result).as("Result should have been null").isNull();
      assertThat(exceptionThrown.get()).as(
          "Exception unexpectedly thrown on verify false method with null boolean").isFalse();
    }

  }
}
