package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.city.WardRoma;

class WardRomaTest {

  @Test
  void testReconstruct_NullValue() {
    WardRoma result = WardRoma.reconstruct(null);
    Assertions.assertSame(WardRoma.empty(), result);
  }

  @Test
  void testReconstruct_EmptyValue() {
    WardRoma result = WardRoma.reconstruct("");
    Assertions.assertSame(WardRoma.EMPTY, result);
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
    Assertions.assertSame(WardRoma.EMPTY, result);
  }

  @Test
  void testOf_EmptyValue() {
    WardRoma result = WardRoma.of("");
    Assertions.assertSame(WardRoma.EMPTY, result);
  }

  @Test
  void testOf_NonEmptyValue() {
    String testValue = "Test Value";
    WardRoma result = WardRoma.of(testValue);
    assertEquals(testValue, result.getValue());
  }
}