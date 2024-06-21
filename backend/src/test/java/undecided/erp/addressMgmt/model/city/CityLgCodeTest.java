package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class CityLgCodeTest {

  @Test
  void reconstruct_ShouldReconstructCityLgCode_WhenProvidedWithValidString() {
    // Arrange
    String expectedValue = "CT";

    // Act
    CityLgCode cityLgCode = CityLgCode.reconstruct(expectedValue);

    // Assert
    assertNotNull(cityLgCode);
    assertEquals(expectedValue, cityLgCode.toString());
  }

  @Test
  void of_ShouldCreateCityLgCode_WhenProvidedWithValidString() {
    // Arrange
    String expectedValue = "NY";

    // Act
    CityLgCode cityLgCode = CityLgCode.of(expectedValue);

    // Assert
    assertNotNull(cityLgCode);
    assertEquals(expectedValue, cityLgCode.toString());
  }
}