package undecided.erp.addressMgmt.domain.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class PrefectureKana {

  private String value;

  public static PrefectureKana reconstruct(String value) {
    return new PrefectureKana(value);

  }

  public static PrefectureKana of(String value) {
    return new PrefectureKana(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
