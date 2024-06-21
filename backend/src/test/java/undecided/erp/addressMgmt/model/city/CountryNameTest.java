package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class CountryNameTest {

  @Test
  public void testReconstruct() {
    String expectedValue = "United States";
    CountryName countryName = CountryName.reconstruct(expectedValue);

    assertNotNull(countryName);
    assertEquals(expectedValue, countryName.toString());
  }

  @Test
  public void testReconstructWithEmptyString() {
    String expectedValue = "";
    CountryName countryName = CountryName.reconstruct(expectedValue);

    assertNotNull(countryName);
    assertEquals(expectedValue, countryName.toString());
  }

  // more tests
}