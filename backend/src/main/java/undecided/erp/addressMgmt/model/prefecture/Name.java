package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Name {

  private String value;

  public static Name reconstruct(String value) {
    return new Name(value);
  }

  @Override
  public String toString() {
    return value;
  }

}
