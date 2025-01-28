package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.city.CityKana;

public class CityKanaTest {

  @Test
  public void reconstructTest() {
    String expected = "Test Kana";
    CityKana actual = CityKana.reconstruct(expected);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void ofTest() {
    String expected = "Test Kana";
    CityKana actual = CityKana.of(expected);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void toStringTest() {
    String expected = "Test Kana";
    CityKana cityKana = new CityKana(expected);
    assertEquals(expected, cityKana.toString());
  }
}