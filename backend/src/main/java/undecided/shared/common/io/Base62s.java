package undecided.shared.common.io;

import java.util.function.Function;
import org.jspecify.annotations.NonNull;
import undecided.shared.common.primitive.Objects2;

public class Base62s {

  private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final int BASE = 62;
  /**
   * {@code DECODE_FROM_BASE62} は Base62 エンコーディングされた文字列をデコードし、対応する {@code Long} 型の値を返す
   * {@code Function} です。
   *
   * <ul>
   *   <li>負の数値を扱う場合は、文字列の先頭に "-" を付けることで対応します。</li>
   *   <li>{@code BASE62_CHARS} に含まれない無効な文字が文字列内に含まれている場合、例外をスローします。</li>
   *   <li>入力引数が {@code null} の場合は、例外がスローされます。</li>
   * </ul>
   * <p>
   * デコード処理では以下のロジックを用います：
   * <ul>
   *   <li>文字列内の各文字を 62 進数として扱い、文字のインデックスを用いて値を計算します。</li>
   *   <li>文字列が負の場合は、最終的な結果に負符号を付けます。</li>
   * </ul>
   * <p>
   * スローされる例外:
   * <ul>
   *   <li>{@code IllegalArgumentException} - 入力値が {@code null}、または無効な Base62 文字を含む場合。</li>
   * </ul>
   */
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
  /**
   * {@code ENCODE_TO_BASE62} は {@code Long} 型の数値を Base62 エンコーディングされた {@code String} としてエンコードするための
   * {@code Function} です。
   * <p>
   * エンコード処理では以下の仕様を持ちます: - 対象の長整数値({@code Long})を Base62 に変換し、負の値の場合は結果の文字列の先頭に "-" を付加します。 - 値が
   * {@code 0} の場合は、文字列 {@code "0"} を返します。
   * <p>
   * 特記事項: - 値が {@code null} の場合は {@code IllegalArgumentException} がスローされます。 - 使用される Base62
   * の文字セットは標準的な "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" です。
   * <p>
   * スローされる例外: - {@code IllegalArgumentException} - 入力値が {@code null} の場合。
   */
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
