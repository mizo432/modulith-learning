package undecided.erp.shared.value;

import java.math.BigInteger;

public record AllDecimalString(String value) {

  public static AllDecimalString of(String value) {
    if (value == null) {
      return new AllDecimalString(null);
    }
    if (value.isEmpty()) {
      return new AllDecimalString("");
    }
    if (!isPositiveDigits(value)) {
      throw new IllegalArgumentException("The value '" + value + "' is not a valid decimal code");
    }
    return new AllDecimalString(value);
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
