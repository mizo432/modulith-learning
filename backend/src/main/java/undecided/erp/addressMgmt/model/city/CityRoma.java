package undecided.erp.addressMgmt.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CityRoma {

  private String value;

  public static CityRoma reconstruct(String value) {
    return new CityRoma(value);

  }

  public static CityRoma of(String value) {
    return new CityRoma(value);

  }

  @Override
  public String toString() {
    return value;
  }
}
