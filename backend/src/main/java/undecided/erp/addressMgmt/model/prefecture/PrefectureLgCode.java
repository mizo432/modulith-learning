package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.shared.value.FixedLengthString;

@AllArgsConstructor
@Getter
public class PrefectureLgCode {

  String value;

  /**
   * 提供された値で新しいPrefectureLgCodeオブジェクトを再構築します。
   *
   * @param value PrefectureLgCodeの値。
   * @return 提供された値を持つ新しいPrefectureLgCodeオブジェクト。
   */
  public static PrefectureLgCode reconstruct(String value) {
    return new PrefectureLgCode(value);
  }

  /**
   * 提供された値で新しいPrefectureLgCodeオブジェクトを作成します。
   *
   * @param value PrefectureLgCodeの値。
   * @return 提供された値と共に新しいPrefectureLgCodeオブジェクト。
   */
  public static PrefectureLgCode of(@NonNull String value) {
    FixedLengthString.of(value, 2);
    return new PrefectureLgCode(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
