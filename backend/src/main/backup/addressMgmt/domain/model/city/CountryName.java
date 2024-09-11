package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class CountryName {

  public static final CountryName EMPTY = new CountryName();
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
