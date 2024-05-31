package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NameRoma {

  private String value;

  public static NameRoma reconstruct(String value) {
    return new NameRoma(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
