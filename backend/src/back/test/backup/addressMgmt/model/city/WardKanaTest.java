package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.city.WardKana;

class WardKanaTest {

  @Test
  void testReconstruct_withValidValue() {
    String expectedValue = "valid";
    WardKana wardKana = WardKana.reconstruct(expectedValue);
    Assertions.assertNotNull(wardKana);
    assertEquals(expectedValue, wardKana.toString());
  }

  @Test
  void testReconstruct_withEmptyValue() {
    String expectedValue = "";
    WardKana wardKana = WardKana.reconstruct(expectedValue);
    Assertions.assertNotNull(wardKana);
    assertEquals(expectedValue, wardKana.toString());
  }

}