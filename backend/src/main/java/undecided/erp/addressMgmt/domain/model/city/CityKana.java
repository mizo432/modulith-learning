package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class CityKana {

  private String value;

  public static CityKana reconstruct(String value) {
    return new CityKana(value);

  }

  public static CityKana of(String value) {
    return new CityKana(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
