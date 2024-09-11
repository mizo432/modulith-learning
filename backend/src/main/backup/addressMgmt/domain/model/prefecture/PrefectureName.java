package undecided.erp.addressMgmt.domain.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class PrefectureName {

  private String value;

  public static PrefectureName reconstruct(String value) {
    return new PrefectureName(value);

  }

  public static PrefectureName of(@NonNull String value) {
    return new PrefectureName(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
