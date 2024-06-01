package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class CityTest {

  @Test
  public void testReconstruct() {
    Long id = 1L;
    CityLgCode cityLgCode = CityLgCode.reconstruct("036");
    CountyNames countyNames = CountyNames.reconstruct("Aoba", "Aoba", "Aoba");
    CityNames cityNames = CityNames.reconstruct("Yokohama", "Yokohama", "Yokohama");
    WardNames wardNames = WardNames.reconstruct("Totsuka", "Totsuka", "Totsuka");

    CityAttribute attribute = CityAttribute.reconstruct(cityLgCode, countyNames, cityNames,
        wardNames);
    LocalDate effectiveDate = LocalDate.of(2022, 1, 1);
    LocalDate abolitionDate = LocalDate.of(2022, 12, 31);
    String remarks = "Test city";

    City city = City.reconstruct(id, attribute, effectiveDate, abolitionDate, remarks);

    assertEquals(id, city.getId().getValue());
    assertEquals(attribute, city.getAttribute());
    assertEquals(effectiveDate, city.getEffectiveDate().getValue().getValue());
    assertEquals(abolitionDate, city.getAbolitionDate().getValue().getValue());
    assertEquals(remarks, city.getRemarks());
  }
}
