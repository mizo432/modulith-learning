package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class WardKana {

  private String value;

  public static WardKana reconstruct(String value) {
    return new WardKana(value);

  }

  public static WardKana of(String value) {
    return new WardKana(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
