package undecided.erp.common.primitive;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Booleans {

  /**
   * 与えられたBoolean値がtrueかどうかを確認します。
   *
   * @param value 確認するBoolean値
   * @return 値がnullでなく、trueの場合はtrue、それ以外の場合はfalseを返します
   */
  public static boolean isTrue(Boolean value) {
    if (value == null) {
      return false;
    }
    return value;
  }

  /**
   * 与えられたブール値がfalseかどうかを確認します。
   *
   * @param value チェックするブール値
   * @return 値がnullまたはfalseの場合はtrue、それ以外の場合はfalseを返します
   */
  public static boolean isFalse(Boolean value) {
    if (value == null) {
      return true;
    }
    return !value;
  }
}
