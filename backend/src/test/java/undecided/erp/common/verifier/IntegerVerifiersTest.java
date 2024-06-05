package undecided.erp.common.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class IntegerVerifiersTest {

  private final Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException(
      "Number is not in the specified range");

  @Test
  void testVerifyRangeClosed_whenNumberIsInRange_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, IntegerVerifiers.verifyRangeClosed(10, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosed_whenNumberIsEqualToMin_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, IntegerVerifiers.verifyRangeClosed(5, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosed_whenNumberIsEqualToMax_shouldReturnThatNumber() {
    Integer number = 15;
    assertEquals(number, IntegerVerifiers.verifyRangeClosed(15, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosed_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeClosed(4, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosed_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeClosed(16, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeGreaterThan_whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, IntegerVerifiers.verifyRangeGreaterThan(10, 5, exceptionSupplier));
  }

  @Test
  void testVerifyRangeGreaterThan_whenNumberIsEqualToMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeGreaterThan(5, 5, exceptionSupplier));
  }

  @Test
  void testVerifyRangeGreaterThan_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeGreaterThan(4, 5, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpen_whenNumberIsInRange_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, IntegerVerifiers.verifyRangeOpen(10, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpen_whenNumberIsEqualToMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeOpen(5, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpen_whenNumberIsEqualToMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeOpen(15, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpen_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeOpen(4, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpen_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeOpen(16, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosedOpen_whenNumberIsInClosedRange_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, IntegerVerifiers.verifyRangeClosedOpen(5, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosedOpen_whenNumberIsInOpenRange_shouldReturnThatNumber() {
    Integer number = 14;
    assertEquals(number, IntegerVerifiers.verifyRangeClosedOpen(14, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosedOpen_whenNumberIsEqualToMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeClosedOpen(15, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosedOpen_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeClosedOpen(4, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeClosedOpen_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeClosedOpen(16, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpenClosed_whenNumberIsInOpenRange_shouldReturnThatNumber() {
    Integer number = 6;
    assertEquals(number, IntegerVerifiers.verifyRangeOpenClosed(6, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpenClosed_whenNumberIsInClosedRange_shouldReturnThatNumber() {
    Integer number = 15;
    assertEquals(number, IntegerVerifiers.verifyRangeOpenClosed(15, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpenClosed_whenNumberIsEqualToMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeOpenClosed(5, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpenClosed_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeOpenClosed(4, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeOpenClosed_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeOpenClosed(16, 5, 15, exceptionSupplier));
  }

  @Test
  void testVerifyRangeAtLeast_whenNumberIsEqualToMin_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, IntegerVerifiers.verifyRangeAtLest(5, 5, exceptionSupplier));
  }

  @Test
  void testVerifyRangeAtLeast_whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, IntegerVerifiers.verifyRangeAtLest(10, 5, exceptionSupplier));
  }

  @Test
  void testVerifyRangeAtLeast_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeAtLest(4, 5, exceptionSupplier));
  }

  @Test
  void testVerifyRangeAtMost_whenNumberIsEqualToMax_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, IntegerVerifiers.verifyRangeAtMost(10, 10, exceptionSupplier));
  }

  @Test
  void testVerifyRangeAtMost_whenNumberIsLessThanMax_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, IntegerVerifiers.verifyRangeAtMost(5, 10, exceptionSupplier));
  }

  @Test
  void testVerifyRangeAtMost_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyRangeAtMost(15, 10, exceptionSupplier));
  }

  @Test
  void testVerifyPositive_whenNumberIsPositive_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, IntegerVerifiers.verifyPositive(5, exceptionSupplier));
  }

  @Test
  void testVerifyPositive_whenNumberIsZero_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyPositive(0, exceptionSupplier));
  }

  @Test
  void testVerifyPositive_whenNumberIsNegative_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyPositive(-5, exceptionSupplier));
  }

  @Test
  void testVerifyPositiveOrZero_whenNumberIsPositive_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, IntegerVerifiers.verifyPositiveOrZero(5, exceptionSupplier));
  }

  @Test
  void testVerifyNegative_whenNumberIsNegative_shouldReturnThatNumber() {
    Integer number = -5;
    assertEquals(number, IntegerVerifiers.verifyNegative(-5, exceptionSupplier));
  }

  @Test
  void testVerifyNegative_whenNumberIsZero_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyNegative(0, exceptionSupplier));
  }

  @Test
  void testVerifyNegative_whenNumberIsPositive_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyNegative(5, exceptionSupplier));
  }

  @Test
  void testVerifyPositiveOrZero_whenNumberIsZero_shouldReturnThatNumber() {
    Integer number = 0;
    assertEquals(number, IntegerVerifiers.verifyPositiveOrZero(0, exceptionSupplier));
  }

  @Test
  void testVerifyPositiveOrZero_whenNumberIsNegative_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyPositiveOrZero(-5, exceptionSupplier));
  }

  @Test
  void testVerifyNegativeOrZero_whenNumberIsZero_shouldReturnThatNumber() {
    Integer number = 0;
    assertEquals(number, IntegerVerifiers.verifyNegativeOrZero(0, exceptionSupplier));
  }

  @Test
  void testVerifyNegativeOrZero_whenNumberIsNegative_shouldReturnThatNumber() {
    Integer number = -5;
    assertEquals(number, IntegerVerifiers.verifyNegativeOrZero(-5, exceptionSupplier));
  }

  @Test
  void testVerifyNegativeOrZero_whenNumberIsPositive_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> IntegerVerifiers.verifyNegativeOrZero(5, exceptionSupplier));
  }
}
