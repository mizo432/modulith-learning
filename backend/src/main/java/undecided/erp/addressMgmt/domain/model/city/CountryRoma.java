package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CountryRoma {

  private String value;

  public static CountryRoma reconstruct(String value) {
    return new CountryRoma(value);

  }

  public static CountryRoma of(String value) {
    return new CountryRoma(value);

  }

  @Override
  public String toString() {
    return value;
  }
}
