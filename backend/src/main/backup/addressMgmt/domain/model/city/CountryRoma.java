package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CountryRoma {

  public static final CountryRoma EMPTY = new CountryRoma();
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
