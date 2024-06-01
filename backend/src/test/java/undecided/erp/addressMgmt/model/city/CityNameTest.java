package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CityNameTest {

  @Test
  public void shouldReconstructCityName() {
    // given
    String value = "London";

    // when
    CityName cityName = CityName.reconstruct(value);

    // then
    assertEquals(value, cityName.getValue());
  }

  @Test
  public void shouldReturnNullWhenReconstructWithNull() {
    // given
    String value = null;

    // when
    CityName cityName = CityName.reconstruct(value);

    // then
    assertEquals(value, cityName.getValue());
  }

}