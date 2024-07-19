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
@NoArgsConstructor
@Embeddable
public class CityName {

  public static final CityName EMPTY = new CityName();
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
