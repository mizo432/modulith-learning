package undecided.erp.relMgmt.model.orgRole.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import undecided.erp.relMgmt.domain.model.orgRole.company.CompanyCode;

class CompanyCodeTest {

  @Test
  void testOf_ValidInput() {
    String value = "123";
    CompanyCode result = CompanyCode.of(value);
    assertEquals(value, result.toString());
  }

  @Test
  void testOf_InvalidInput_AllDecimalCode() {
    String value = "ABC";
    assertThrows(IllegalArgumentException.class, () -> CompanyCode.of(value));
  }

  @Test
  void testOf_InvalidInput_LengthCode() {
    String value = "1234";
    assertThrows(IllegalArgumentException.class, () -> CompanyCode.of(value));
  }
}
