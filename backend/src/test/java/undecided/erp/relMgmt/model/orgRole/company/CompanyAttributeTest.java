package undecided.erp.relMgmt.model.orgRole.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyAttributeTest {

  @Test
  public void testOfShouldCreateCompanyAttribute() {
    // Initialize test inputs
    CompanyCode code = CompanyCode.of("001");
    CompanyName name = CompanyName.of("Test Name");

    // Execute the method to be tested
    CompanyAttribute companyAttribute = CompanyAttribute.of(code, name);

    // Validate the result
    Assertions.assertNotNull(companyAttribute);
    Assertions.assertEquals(code, companyAttribute.getCode());
    Assertions.assertEquals(name, companyAttribute.getName());
  }
}