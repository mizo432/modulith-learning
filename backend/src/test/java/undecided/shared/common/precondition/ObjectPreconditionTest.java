package undecided.shared.common.precondition;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ObjectPreconditionTest {

  @Test
  void checkStateWithBooleanAndSupplier_ExceptionThrown() {
    Supplier<IllegalStateException> exceptionSupplier =
        () -> new IllegalStateException("State not expected");

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              ObjectPrecondition.checkState(false, exceptionSupplier);
            });

    assertTrue(exception.getMessage().contains("State not expected"));
  }

  @Test
  void checkStateWithBooleanAndSupplier_NoExceptionThrown() {
    Supplier<IllegalStateException> exceptionSupplier =
        () -> new IllegalStateException("State not expected");

    assertDoesNotThrow(
        () -> {
          ObjectPrecondition.checkState(true, exceptionSupplier);
        });
  }

  @Test
  void checkStateWithBooleanAndString_ExceptionThrown() {
    String label = "test state";

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              ObjectPrecondition.checkState(false, label);
            });

    assertTrue(exception.getMessage().contains(label + " の状態が不正です。"));
  }

  @Test
  void checkStateWithBooleanAndString_NoExceptionThrown() {
    String label = "test state";

    assertDoesNotThrow(
        () -> {
          ObjectPrecondition.checkState(true, label);
        });
  }

  @Test
  void checkArgumentWithBooleanAndSupplier_ExceptionThrown() {
    final Supplier<IllegalArgumentException> exceptionSupplier =
        () -> new IllegalArgumentException("Invalid argument");

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              ObjectPrecondition.checkArgument(false, exceptionSupplier);
            });

    assertTrue(exception.getMessage().contains("Invalid argument"));
  }

  @Test
  void checkArgumentWithBooleanAndSupplier_NoExceptionThrown() {
    final Supplier<IllegalArgumentException> exceptionSupplier =
        () -> new IllegalArgumentException("Invalid argument");

    assertDoesNotThrow(
        () -> {
          ObjectPrecondition.checkArgument(true, exceptionSupplier);
        });
  }

  @Test
  void checkArgumentWithBooleanAndString_ExceptionThrown() {
    String label = "test argument";

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              ObjectPrecondition.checkArgument(false, label);
            });

    assertTrue(exception.getMessage().contains(String.format("引数: %s が不正です。", label)));
  }

  @Test
  void checkArgumentWithBooleanAndString_NoExceptionThrown() {
    String label = "test argument";

    assertDoesNotThrow(
        () -> {
          ObjectPrecondition.checkArgument(true, label);
        });
  }

  @Test
  void checkNotNullWithReferAndSupplier_NullReference_ExceptionThrown() {
    final Supplier<NullPointerException> exceptionSupplier =
        () -> new NullPointerException("Null reference");

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              ObjectPrecondition.checkNotNull(null, exceptionSupplier);
            });

    assertTrue(exception.getMessage().contains("Null reference"));
  }

  @Test
  void checkNotNullWithReferAndSupplier_ValidReference_NoExceptionThrown() {
    final Supplier<NullPointerException> exceptionSupplier =
        () -> new NullPointerException("Null reference");

    assertDoesNotThrow(
        () -> {
          ObjectPrecondition.checkNotNull(new Object(), exceptionSupplier);
        });
  }

  @Test
  void checkNotNullWithReferAndString_NullReference_ExceptionThrown() {
    String label = "test reference";

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              ObjectPrecondition.checkNotNull(null, label);
            });

    assertTrue(exception.getMessage().contains(String.format("%s がnullです。", label)));
  }

  @Test
  void checkNotNullWithReferAndString_ValidReference_NoExceptionThrown() {
    String label = "test reference";

    assertDoesNotThrow(
        () -> {
          ObjectPrecondition.checkNotNull(new Object(), label);
        });
  }
}
