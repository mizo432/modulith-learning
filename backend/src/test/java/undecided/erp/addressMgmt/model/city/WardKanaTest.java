package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class WardKanaTest {

  @Test
  void testReconstruct_withValidValue() {
    String expectedValue = "valid";
    WardKana wardKana = WardKana.reconstruct(expectedValue);
    assertNotNull(wardKana);
    assertEquals(expectedValue, wardKana.toString());
  }

  @Test
  void testReconstruct_withEmptyValue() {
    String expectedValue = "";
    WardKana wardKana = WardKana.reconstruct(expectedValue);
    assertNotNull(wardKana);
    assertEquals(expectedValue, wardKana.toString());
  }

}