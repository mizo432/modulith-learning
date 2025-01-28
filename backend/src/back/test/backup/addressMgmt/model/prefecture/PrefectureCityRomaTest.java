package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.city.CityRoma;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureRoma;

class PrefectureCityRomaTest {

  @Test
  @DisplayName("Test reconstruct method with valid string")
  void testReconstructWithValidString() {
    String expectedValue = "testValue";
    PrefectureRoma actualPrefectureRoma = PrefectureRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualPrefectureRoma.getValue());
  }

  @Test
  @DisplayName("Test reconstruct method with null string")
  void testReconstructWithNullString() {
    String expectedValue = null;
    PrefectureRoma actualPrefectureRoma = PrefectureRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualPrefectureRoma.getValue());
  }

  @Test
  @DisplayName("Test reconstruct method with empty string")
  void testReconstructWithEmptyString() {
    String expectedValue = "";
    PrefectureRoma actualPrefectureRoma = PrefectureRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualPrefectureRoma.getValue());
  }

  @Test
  @DisplayName("Test toString method with valid string")
  void testToStringWithValidString() {
    String expectedValue = "testValue";
    PrefectureRoma prefectureRoma = new PrefectureRoma(expectedValue);
    String actualValue = prefectureRoma.toString();
    Assertions.assertEquals(expectedValue, actualValue);
  }

  @Test
  @DisplayName("Test toString method with null string")
  void testToStringWithNullString() {
    String expectedValue = null;
    PrefectureRoma prefectureRoma = new PrefectureRoma(expectedValue);
    String actualValue = prefectureRoma.toString();
    Assertions.assertEquals(expectedValue, actualValue);
  }

  @Test
  @DisplayName("Test toString method with empty string")
  void testToStringWithEmptyString() {
    String expectedValue = "";
    PrefectureRoma prefectureRoma = new PrefectureRoma(expectedValue);
    String actualValue = prefectureRoma.toString();
    Assertions.assertEquals(expectedValue, actualValue);
  }

  @Test
  @DisplayName("Test CityRoma reconstruct method with valid string")
  void testCityRomaReconstructWithValidString() {
    String expectedValue = "testValue";
    CityRoma actualCityRoma = CityRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualCityRoma.getValue());
  }

  @Test
  @DisplayName("Test CityRoma reconstruct method with null string")
  void testCityRomaReconstructWithNullString() {
    String expectedValue = null;
    CityRoma actualCityRoma = CityRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualCityRoma.getValue());
  }

  @Test
  @DisplayName("Test CityRoma reconstruct method with empty string")
  void testCityRomaReconstructWithEmptyString() {
    String expectedValue = "";
    CityRoma actualCityRoma = CityRoma.reconstruct(expectedValue);
    assertEquals(expectedValue, actualCityRoma.getValue());
  }
}
