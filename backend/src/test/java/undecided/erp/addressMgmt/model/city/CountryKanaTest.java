package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.Getter;
import org.junit.jupiter.api.Test;

@Getter

public class CountryKanaTest {

  @Test
  public void reconstruct_countryNameInput_CountryKanaExpected() {
    String countryName = "Japan";
    CountryKana countryKana = CountryKana.reconstruct(countryName);

    assertEquals(countryName, countryKana.getValue());
  }

  @Test
  public void reconstruct_emptyInput_EmptyCountryKanaExpected() {
    String countryName = "";
    CountryKana countryKana = CountryKana.reconstruct(countryName);

    assertEquals(countryName, countryKana.getValue());
  }

  @Test
  public void reconstruct_nullInput_NullCountryKanaExpected() {
    String countryName = null;
    CountryKana countryKana = CountryKana.reconstruct(countryName);

    assertEquals(countryName, countryKana.getValue());
  }
}