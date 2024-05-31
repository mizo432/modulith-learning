package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameRomaTest {

  @Test
  @DisplayName("Test reconstruct method with valid string")
  void testReconstructWithValidString() {
    String expectedValue = "testValue";
    NameRoma actualNameRoma = NameRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualNameRoma.getValue());
  }

  @Test
  @DisplayName("Test reconstruct method with null string")
  void testReconstructWithNullString() {
    String expectedValue = null;
    NameRoma actualNameRoma = NameRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualNameRoma.getValue());
  }

  @Test
  @DisplayName("Test reconstruct method with empty string")
  void testReconstructWithEmptyString() {
    String expectedValue = "";
    NameRoma actualNameRoma = NameRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualNameRoma.getValue());
  }

  @Test
  @DisplayName("Test toString method with valid string")
  void testToStringWithValidString() {
    String expectedValue = "testValue";
    NameRoma nameRoma = new NameRoma(expectedValue);
    String actualValue = nameRoma.toString();
    assertEquals(expectedValue, actualValue);
  }

  @Test
  @DisplayName("Test toString method with null string")
  void testToStringWithNullString() {
    String expectedValue = null;
    NameRoma nameRoma = new NameRoma(expectedValue);
    String actualValue = nameRoma.toString();
    assertEquals(expectedValue, actualValue);
  }

  @Test
  @DisplayName("Test toString method with empty string")
  void testToStringWithEmptyString() {
    String expectedValue = "";
    NameRoma nameRoma = new NameRoma(expectedValue);
    String actualValue = nameRoma.toString();
    assertEquals(expectedValue, actualValue);
  }
}