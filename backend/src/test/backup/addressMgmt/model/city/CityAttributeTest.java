package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.city.CityAttribute;
import undecided.erp.addressMgmt.domain.model.city.CityLgCode;
import undecided.erp.addressMgmt.domain.model.city.CityNames;
import undecided.erp.addressMgmt.domain.model.city.CountyNames;
import undecided.erp.addressMgmt.domain.model.city.WardNames;

public class CityAttributeTest {

  @Test
  public void testReconstruct() {
    CityLgCode cityLgCode = CityLgCode.reconstruct("036");
    CountyNames countyNames = CountyNames.reconstruct("Aoba", "Aoba", "Aoba");
    CityNames cityNames = CityNames.reconstruct("Yokohama", "Yokohama", "Yokohama");
    WardNames wardNames = WardNames.reconstruct("Totsuka", "Totsuka", "Totsuka");

    CityAttribute cityAttribute = CityAttribute.reconstruct(cityLgCode, countyNames, cityNames,
        wardNames);

    assertEquals(cityLgCode, cityAttribute.getLgCode());
    assertEquals(countyNames, cityAttribute.getCountryNames());
    assertEquals(cityNames, cityAttribute.getCityNames());
    assertEquals(wardNames, cityAttribute.getWardNames());
  }

  @Test
  public void testReconstructWithDifferentInputs() {
    CityLgCode cityLgCode = CityLgCode.reconstruct("789");
    CountyNames countyNames = CountyNames.reconstruct("Chuo", "Chuo", "Chuo");
    CityNames cityNames = CityNames.reconstruct("Tokyo", "Tokyo", "Tokyo");
    WardNames wardNames = WardNames.reconstruct("Nihonbashi", "Nihonbashi", "Nihonbashi");

    CityAttribute cityAttribute = CityAttribute.reconstruct(cityLgCode, countyNames, cityNames,
        wardNames);

    assertEquals(cityLgCode, cityAttribute.getLgCode());
    assertEquals(countyNames, cityAttribute.getCountryNames());
    assertEquals(cityNames, cityAttribute.getCityNames());
    assertEquals(wardNames, cityAttribute.getWardNames());
  }

  @Test
  public void testReconstructWithSimilarInputs() {
    CityLgCode cityLgCode = CityLgCode.reconstruct("789");
    CountyNames countyNames = CountyNames.reconstruct("Chuo", "Chuo", "Chuo");
    CityNames cityNames = CityNames.reconstruct("Tokyo", "Tokyo", "Tokyo");
    WardNames wardNames = WardNames.reconstruct("Chuo", "Chuo", "Chuo");

    CityAttribute cityAttribute = CityAttribute.reconstruct(cityLgCode, countyNames, cityNames,
        wardNames);

    assertEquals(cityLgCode, cityAttribute.getLgCode());
    assertEquals(countyNames, cityAttribute.getCountryNames());
    assertEquals(cityNames, cityAttribute.getCityNames());
    assertEquals(wardNames, cityAttribute.getWardNames());
  }
}
