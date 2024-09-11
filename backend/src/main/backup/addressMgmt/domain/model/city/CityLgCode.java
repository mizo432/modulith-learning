package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import undecided.erp.shared.value.FixedLengthString;

@AllArgsConstructor
@Getter
@Embeddable
@NoArgsConstructor
public class CityLgCode {

  public static final CityLgCode EMPTY = new CityLgCode();
  private String value;

  /**
   * 提供された値で新しいPrefectureLgCodeオブジェクトを再構築します。
   *
   * @param value PrefectureLgCodeの値。
   * @return 提供された値を持つ新しいPrefectureLgCodeオブジェクト。
   */
  public static CityLgCode reconstruct(String value) {
    return new CityLgCode(value);
  }

  /**
   * 提供された値で新しいPrefectureLgCodeオブジェクトを作成します。
   *
   * @param value PrefectureLgCodeの値。
   * @return 提供された値と共に新しいPrefectureLgCodeオブジェクト。
   */
  public static CityLgCode of(@NonNull String value) {
    FixedLengthString.of(value, 2);
    return new CityLgCode(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
