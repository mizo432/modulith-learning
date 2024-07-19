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

public class CityKana {

  public static final CityKana EMPTY = new CityKana();
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
