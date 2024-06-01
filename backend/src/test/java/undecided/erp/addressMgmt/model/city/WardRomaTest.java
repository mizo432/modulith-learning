package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class WardRomaTest {

  @Test
  void testReconstruct_NullValue() {
    WardRoma result = WardRoma.reconstruct(null);
    assertSame(WardRoma.EMPTY, result);
  }

  @Test
  void testReconstruct_EmptyValue() {
    WardRoma result = WardRoma.reconstruct("");
    assertSame(WardRoma.EMPTY, result);
  }

  @Test
  void testReconstruct_NonEmptyValue() {
    String testValue = "Test Value";
    WardRoma result = WardRoma.reconstruct(testValue);
    assertEquals(testValue, result.getValue());
  }

  @Test
  void testOf_NullValue() {
    WardRoma result = WardRoma.of(null);
    assertSame(WardRoma.EMPTY, result);
  }

  @Test
  void testOf_EmptyValue() {
    WardRoma result = WardRoma.of("");
    assertSame(WardRoma.EMPTY, result);
  }

  @Test
  void testOf_NonEmptyValue() {
    String testValue = "Test Value";
    WardRoma result = WardRoma.of(testValue);
    assertEquals(testValue, result.getValue());
  }
}