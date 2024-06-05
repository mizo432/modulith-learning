package undecided.erp.common.verifier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ObjectVerifiersTest {

  @Test
  void verifyStateWithBooleanAndSupplier_ExceptionThrown() {
    Supplier<IllegalStateException> exceptionSupplier = () -> new IllegalStateException(
        "State not expected");

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectVerifiers.verifyState(false, exceptionSupplier);
    });

    assertTrue(exception.getMessage().contains("State not expected"));
  }

  @Test
  void verifyStateWithBooleanAndSupplier_NoExceptionThrown() {
    Supplier<IllegalStateException> exceptionSupplier = () -> new IllegalStateException(
        "State not expected");

    assertDoesNotThrow(() -> {
      ObjectVerifiers.verifyState(true, exceptionSupplier);
    });
  }

  @Test
  void verifyStateWithBooleanAndString_ExceptionThrown() {
    String label = "test state";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectVerifiers.verifyState(false, label);
    });

    assertTrue(exception.getMessage().contains(label + " の状態が不正です。"));
  }

  @Test
  void verifyStateWithBooleanAndString_NoExceptionThrown() {
    String label = "test state";

    assertDoesNotThrow(() -> {
      ObjectVerifiers.verifyState(true, label);
    });
  }

  @Test
  void verifyArgumentWithBooleanAndSupplier_ExceptionThrown() {
    final Supplier<IllegalArgumentException> exceptionSupplier = () -> new IllegalArgumentException(
        "Invalid argument");

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectVerifiers.verifyArgument(false, exceptionSupplier);
    });

    assertTrue(exception.getMessage().contains("Invalid argument"));
  }

  @Test
  void verifyArgumentWithBooleanAndSupplier_NoExceptionThrown() {
    final Supplier<IllegalArgumentException> exceptionSupplier = () -> new IllegalArgumentException(
        "Invalid argument");

    assertDoesNotThrow(() -> {
      ObjectVerifiers.verifyArgument(true, exceptionSupplier);
    });
  }

  @Test
  void verifyArgumentWithBooleanAndString_ExceptionThrown() {
    String label = "test argument";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectVerifiers.verifyArgument(false, label);
    });

    assertTrue(exception.getMessage().contains(String.format("引数: %s が不正です。", label)));
  }

  @Test
  void verifyArgumentWithBooleanAndString_NoExceptionThrown() {
    String label = "test argument";

    assertDoesNotThrow(() -> {
      ObjectVerifiers.verifyArgument(true, label);
    });
  }

  @Test
  void verifyNotNullWithReferAndSupplier_NullReference_ExceptionThrown() {
    final Supplier<NullPointerException> exceptionSupplier = () -> new NullPointerException(
        "Null reference");

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectVerifiers.verifyNotNull(null, exceptionSupplier);
    });

    assertTrue(exception.getMessage().contains("Null reference"));
  }

  @Test
  void verifyNotNullWithReferAndSupplier_ValidReference_NoExceptionThrown() {
    final Supplier<NullPointerException> exceptionSupplier = () -> new NullPointerException(
        "Null reference");

    assertDoesNotThrow(() -> {
      ObjectVerifiers.verifyNotNull(new Object(), exceptionSupplier);
    });
  }

  @Test
  void verifyNotNullWithReferAndString_NullReference_ExceptionThrown() {
    String label = "test reference";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectVerifiers.verifyNotNull(null, label);
    });

    assertTrue(exception.getMessage().contains(String.format("%s がnullです。", label)));
  }

  @Test
  void verifyNotNullWithReferAndString_ValidReference_NoExceptionThrown() {
    String label = "test reference";

    assertDoesNotThrow(() -> {
      ObjectVerifiers.verifyNotNull(new Object(), label);
    });
  }
}
