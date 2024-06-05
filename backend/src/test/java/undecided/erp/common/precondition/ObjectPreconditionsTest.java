package undecided.erp.common.precondition;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ObjectPreconditionsTest {

  @Test
  void checkStateWithBooleanAndSupplier_ExceptionThrown() {
    Supplier<IllegalStateException> exceptionSupplier = () -> new IllegalStateException(
        "State not expected");

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectPreconditions.checkState(false, exceptionSupplier);
    });

    assertTrue(exception.getMessage().contains("State not expected"));
  }

  @Test
  void checkStateWithBooleanAndSupplier_NoExceptionThrown() {
    Supplier<IllegalStateException> exceptionSupplier = () -> new IllegalStateException(
        "State not expected");

    assertDoesNotThrow(() -> {
      ObjectPreconditions.checkState(true, exceptionSupplier);
    });
  }

  @Test
  void checkStateWithBooleanAndString_ExceptionThrown() {
    String label = "test state";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectPreconditions.checkState(false, label);
    });

    assertTrue(exception.getMessage().contains(label + " の状態が不正です。"));
  }

  @Test
  void checkStateWithBooleanAndString_NoExceptionThrown() {
    String label = "test state";

    assertDoesNotThrow(() -> {
      ObjectPreconditions.checkState(true, label);
    });
  }

  @Test
  void checkArgumentWithBooleanAndSupplier_ExceptionThrown() {
    final Supplier<IllegalArgumentException> exceptionSupplier = () -> new IllegalArgumentException(
        "Invalid argument");

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectPreconditions.checkArgument(false, exceptionSupplier);
    });

    assertTrue(exception.getMessage().contains("Invalid argument"));
  }

  @Test
  void checkArgumentWithBooleanAndSupplier_NoExceptionThrown() {
    final Supplier<IllegalArgumentException> exceptionSupplier = () -> new IllegalArgumentException(
        "Invalid argument");

    assertDoesNotThrow(() -> {
      ObjectPreconditions.checkArgument(true, exceptionSupplier);
    });
  }

  @Test
  void checkArgumentWithBooleanAndString_ExceptionThrown() {
    String label = "test argument";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectPreconditions.checkArgument(false, label);
    });

    assertTrue(exception.getMessage().contains(String.format("引数: %s が不正です。", label)));
  }

  @Test
  void checkArgumentWithBooleanAndString_NoExceptionThrown() {
    String label = "test argument";

    assertDoesNotThrow(() -> {
      ObjectPreconditions.checkArgument(true, label);
    });
  }

  @Test
  void checkNotNullWithReferAndSupplier_NullReference_ExceptionThrown() {
    final Supplier<NullPointerException> exceptionSupplier = () -> new NullPointerException(
        "Null reference");

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectPreconditions.checkNotNull(null, exceptionSupplier);
    });

    assertTrue(exception.getMessage().contains("Null reference"));
  }

  @Test
  void checkNotNullWithReferAndSupplier_ValidReference_NoExceptionThrown() {
    final Supplier<NullPointerException> exceptionSupplier = () -> new NullPointerException(
        "Null reference");

    assertDoesNotThrow(() -> {
      ObjectPreconditions.checkNotNull(new Object(), exceptionSupplier);
    });
  }

  @Test
  void checkNotNullWithReferAndString_NullReference_ExceptionThrown() {
    String label = "test reference";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      ObjectPreconditions.checkNotNull(null, label);
    });

    assertTrue(exception.getMessage().contains(String.format("%s がnullです。", label)));
  }

  @Test
  void checkNotNullWithReferAndString_ValidReference_NoExceptionThrown() {
    String label = "test reference";

    assertDoesNotThrow(() -> {
      ObjectPreconditions.checkNotNull(new Object(), label);
    });
  }
}
