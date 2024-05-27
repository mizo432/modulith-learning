package undecided.erp.shared.value;

import java.math.BigInteger;

public record AllDecimalCode(String value) {

  public static AllDecimalCode of(String value) {
    if (!isPositiveDigits(value)) {
      throw new IllegalArgumentException("The value '" + value + "' is not a valid decimal code");
    }
    return new AllDecimalCode(value);
  }

  private static boolean isPositiveDigits(String s) {
    try {
      BigInteger num = new BigInteger(s);
      return num.compareTo(BigInteger.ZERO) > 0;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }
}
