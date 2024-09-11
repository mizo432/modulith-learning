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
public class WardRoma {

  public static final WardRoma EMPTY = new WardRoma();
  private String value;

  public static WardRoma reconstruct(String value) {
    if (value == null || value.isEmpty()) {
      return WardRoma.EMPTY;
    }
    return new WardRoma(value);

  }

  public static WardRoma of(String value) {
    if (value == null || value.isEmpty()) {
      return WardRoma.EMPTY;
    }
    return new WardRoma(value);

  }

  public static WardRoma empty() {
    return EMPTY;
  }

  @Override
  public String toString() {
    return value;

  }
}
