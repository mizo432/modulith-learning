package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CountryRomaTest {

  @Test
  void reconstruct_emptyString_valueSetToEmptyString() {
    // Arrange
    String expectedValue = "";

    // Act
    CountryRoma countryRoma = CountryRoma.reconstruct(expectedValue);

    // Assert
    assertNotNull(countryRoma);
    assertEquals(expectedValue, countryRoma.getValue());
  }

  @Test
  void reconstruct_nonEmptyString_valueSetToProvidedValue() {
    // Arrange
    String expectedValue = "Test Value";

    // Act
    CountryRoma countryRoma = CountryRoma.reconstruct(expectedValue);

    // Assert
    assertNotNull(countryRoma);
    assertEquals(expectedValue, countryRoma.getValue());
  }
}