package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class CountryKana {

  private String value;

  public static CountryKana reconstruct(String value) {
    return new CountryKana(value);

  }

  public static CountryKana of(String value) {
    return new CountryKana(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
