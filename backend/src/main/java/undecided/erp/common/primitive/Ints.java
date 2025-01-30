package undecided.erp.common.primitive;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Ints {

  public static final int BYTES = Integer.SIZE / Byte.SIZE;
  public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

  /**
   * 指定された{@code long}値に相当する{@code int}値を返します。
   * <p>
   * もし値が{@code Integer.MAX_VALUE}を超える場合は{@code Integer.MAX_VALUE}を返します。
   * また、値が{@code Integer.MIN_VALUE}より小さい場合は{@code Integer.MIN_VALUE}を返します。
   * それ以外の場合は、値を{@code int}にキャストした結果を返します。
   *
   * @param value {@code long}型の値を{@code int}型に変換する
   * @return 指定された値を飽和した形で表現した整数値
   */
  public static int saturatedCast(long value) {
    if (value > Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    if (value < Integer.MIN_VALUE) {
      return Integer.MIN_VALUE;
    }
    return (int) value;
  }

}
