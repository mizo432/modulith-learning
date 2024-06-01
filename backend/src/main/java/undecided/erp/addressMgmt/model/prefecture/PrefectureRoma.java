package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PrefectureRoma {

  private String value;

  public static PrefectureRoma reconstruct(String value) {
    return new PrefectureRoma(value);

  }

  public static PrefectureRoma of(String value) {
    return new PrefectureRoma(value);

  }

  @Override
  public String toString() {
    return value;
  }
}
