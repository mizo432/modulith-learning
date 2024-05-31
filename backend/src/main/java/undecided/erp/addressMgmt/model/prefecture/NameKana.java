package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class NameKana {

  private String value;

  public static NameKana reconstruct(String value) {
    return new NameKana(value);
  }

  @Override
  public String toString() {
    return value;
  }

}
