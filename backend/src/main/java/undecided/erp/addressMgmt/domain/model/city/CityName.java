package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class CityName {

  private String value;

  public static CityName reconstruct(String value) {
    return new CityName(value);

  }

  public static CityName of(@NonNull String value) {
    return new CityName(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
