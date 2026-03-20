package undecided.shared.common.primitiveOld;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Ints {

  public static final int BYTES = Integer.SIZE / Byte.SIZE;

  /**
   * {@code int}型における最大の2のべき乗値を表します。
   *
   * <p>この定数は、32ビット整数の範囲内で最大の2のべき乗値であり、 具体的には <code>1 << (Integer.SIZE -
   * 2)</code>、すなわち2<sup>30</sup>を示します。
   *
   * <p>これは主に整数値を扱う際に、領域チェックやビット演算を効率的に行う ために使用されます。
   */
  public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

  /**
   * 指定された{@code long}値に相当する{@code int}値を返します。
   *
   * <p>もし値が{@code Integer.MAX_VALUE}を超える場合は{@code Integer.MAX_VALUE}を返します。 また、値が{@code
   * Integer.MIN_VALUE}より小さい場合は{@code Integer.MIN_VALUE}を返します。 それ以外の場合は、値を{@code int}にキャストした結果を返します。
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
