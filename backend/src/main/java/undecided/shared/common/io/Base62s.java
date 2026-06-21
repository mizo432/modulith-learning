package undecided.shared.common.io;

import java.util.function.Function;
import org.jspecify.annotations.NonNull;
import undecided.shared.common.primitive.Objects2;

public class Base62s {

  public static final Function<String, Long> DECODE_FROM_BASE62 = new Function<>() {
    @Override
    public @NonNull Long apply(@NonNull String s) {
      Objects2.CHECK_NOT_NULL.apply(s, () -> new IllegalArgumentException("s must not be null"));
      boolean negative = s.startsWith("-");
      String digits = negative ? s.substring(1) : s;
      long result = 0L;
      for (int i = 0; i < digits.length(); i++) {
        int index = BASE62_CHARS.indexOf(digits.charAt(i));
        if (index < 0) {
          throw new IllegalArgumentException("Invalid Base62 character: " + digits.charAt(i));
        }
        result = result * BASE + index;
      }
      return negative ? -result : result;
    }
  };
  private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final int BASE = 62;
  public static final Function<Long, String> ENCODE_TO_BASE62 = new Function<>() {

    @Override
    public @NonNull String apply(@NonNull Long longValue) {
      Objects2.CHECK_NOT_NULL.apply(longValue,
          () -> new IllegalArgumentException("aLong must not be null"));
      if (longValue == 0L) {
        return "0";
      }
      boolean negative = longValue < 0;
      long value = negative ? -longValue : longValue;
      StringBuilder sb = new StringBuilder();
      while (value > 0) {
        sb.append(BASE62_CHARS.charAt((int) (value % BASE)));
        value /= BASE;
      }
      if (negative) {
        sb.append('-');
      }
      return sb.reverse().toString();
    }
  };

}
