package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class CountryName {

  private String value;

  public static CountryName reconstruct(String value) {
    return new CountryName(value);

  }

  public static CountryName of(@NonNull String value) {
    return new CountryName(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
