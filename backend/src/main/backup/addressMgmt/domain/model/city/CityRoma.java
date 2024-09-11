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
public class CityRoma {

  public static final CityRoma EMPTY = new CityRoma();
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
