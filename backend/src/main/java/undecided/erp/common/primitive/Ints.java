package undecided.erp.common.primitive;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Ints {

  public static final int BYTES = Integer.SIZE / Byte.SIZE;
  public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

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
