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
public class CountryKana {

  public static final CountryKana EMPTY = new CountryKana();
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
