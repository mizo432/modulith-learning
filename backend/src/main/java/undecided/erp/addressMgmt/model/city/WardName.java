package undecided.erp.addressMgmt.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class WardName {

  private String value;

  public static WardName reconstruct(String value) {
    return new WardName(value);

  }

  public static WardName of(@NonNull String value) {
    return new WardName(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
