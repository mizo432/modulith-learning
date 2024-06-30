package undecided.erp.relationship.model.orgRole.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyName;

class CompanyNameTest {

  @Test
  void testOfNonNullValue() {
    String testValue = "TestCompanyName";
    CompanyName companyName = CompanyName.of(testValue);
    assertEquals(testValue, companyName.getValue());
  }

  @Test
  void testOfNullValue() {
    assertThrows(IllegalArgumentException.class, () -> CompanyName.of(null));
  }

  @Test
  void testOfLengthExceeded() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i <= 128; i++) {
      sb.append('1');
    }
    assertThrows(IllegalArgumentException.class, () -> CompanyName.of(sb.toString()));
  }

  @Test
  void testOfMaxLengthString() {
    String maxVal = String.join("", Collections.nCopies(128, "a"));
    CompanyName.of(maxVal);
  }

  @Test
  void testOfOverMaxLengthString() {
    String overMaxVal = String.join("", Collections.nCopies(129, "1"));
    assertThrows(IllegalArgumentException.class, () -> CompanyName.of(overMaxVal));
  }

  @Test
  void testToString() {
    String testValue = "TestCompanyName";
    CompanyName companyName = CompanyName.of(testValue);
    assertEquals(testValue, companyName.toString());
  }

}
