package undecided.erp.addressMgmt.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WardRoma {

  static final WardRoma EMPTY = new WardRoma();
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

  @Override
  public String toString() {
    return value;

  }
}
