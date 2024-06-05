package undecided.erp.common.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class NumberVerifiersTest {

  private final Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException(
      "Number is not in the specified range");

  @Test
  public void testVerifyRangeClosed_whenNumberIsInRange_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, NumberVerifiers.verifyRangeClosed(10, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosed_whenNumberIsEqualToMin_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, NumberVerifiers.verifyRangeClosed(5, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosed_whenNumberIsEqualToMax_shouldReturnThatNumber() {
    Integer number = 15;
    assertEquals(number, NumberVerifiers.verifyRangeClosed(15, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosed_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeClosed(4, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosed_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeClosed(16, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeGreaterThan_whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, NumberVerifiers.verifyRangeGreaterThan(10, 5, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeGreaterThan_whenNumberIsEqualToMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeGreaterThan(5, 5, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeGreaterThan_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeGreaterThan(4, 5, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpen_whenNumberIsInRange_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, NumberVerifiers.verifyRangeOpen(10, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpen_whenNumberIsEqualToMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeOpen(5, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpen_whenNumberIsEqualToMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeOpen(15, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpen_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeOpen(4, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpen_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeOpen(16, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosedOpen_whenNumberIsInClosedRange_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, NumberVerifiers.verifyRangeClosedOpen(5, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosedOpen_whenNumberIsInOpenRange_shouldReturnThatNumber() {
    Integer number = 14;
    assertEquals(number, NumberVerifiers.verifyRangeClosedOpen(14, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosedOpen_whenNumberIsEqualToMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeClosedOpen(15, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosedOpen_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeClosedOpen(4, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeClosedOpen_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeClosedOpen(16, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpenClosed_whenNumberIsInOpenRange_shouldReturnThatNumber() {
    Integer number = 6;
    assertEquals(number, NumberVerifiers.verifyRangeOpenClosed(6, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpenClosed_whenNumberIsInClosedRange_shouldReturnThatNumber() {
    Integer number = 15;
    assertEquals(number, NumberVerifiers.verifyRangeOpenClosed(15, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpenClosed_whenNumberIsEqualToMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeOpenClosed(5, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpenClosed_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeOpenClosed(4, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeOpenClosed_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeOpenClosed(16, 5, 15, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeAtLeast_whenNumberIsEqualToMin_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, NumberVerifiers.verifyRangeAtLest(5, 5, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeAtLeast_whenNumberIsGreaterThanMin_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, NumberVerifiers.verifyRangeAtLest(10, 5, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeAtLeast_whenNumberIsLessThanMin_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeAtLest(4, 5, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeAtMost_whenNumberIsEqualToMax_shouldReturnThatNumber() {
    Integer number = 10;
    assertEquals(number, NumberVerifiers.verifyRangeAtMost(10, 10, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeAtMost_whenNumberIsLessThanMax_shouldReturnThatNumber() {
    Integer number = 5;
    assertEquals(number, NumberVerifiers.verifyRangeAtMost(5, 10, exceptionSupplier));
  }

  @Test
  public void testVerifyRangeAtMost_whenNumberIsGreaterThanMax_shouldThrowException() {
    assertThrows(RuntimeException.class,
        () -> NumberVerifiers.verifyRangeAtMost(15, 10, exceptionSupplier));
  }
}
