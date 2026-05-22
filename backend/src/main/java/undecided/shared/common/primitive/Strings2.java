package undecided.shared.common.primitive;

import static undecided.shared.common.primitive.Ints.CHECK_POSITIVE;
import static undecided.shared.common.primitive.Objects2.CHECK_NOT_NULL;
import static undecided.shared.common.primitive.Objects2.IS_NULL;

import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import undecided.shared.functional.TwoFunction;

public class Strings2 {
  public static final String EMPTY = "";
  public static final String SPACE = " ";
  public static final String COMMA = ",";
  public static final String DOT = ".";
  public static final String SLASH = "/";
  public static final String UNDERSCORE = "_";
  public static final String DASH = "-";
  public static final String COLON = ":";
  public static final String SEMICOLON = ";";
  public static final String QUESTION_MARK = "?";
  public static final String AT = "@";
  public static final String PERIOD = ".";
  public static final String AND = "&";
  public static final String AMPERSAND = "&";
  public static final String PLUS = "+";
  public static final String EMPTY_STRING = "";
  public static final Surround SURROUND = new Surround();
  public static final IsEmpty IS_EMPTY = new IsEmpty();

  public enum CaseFormat {
    UPPER_CAMEL,
    LOWER_CAMEL,
    UPPER_UNDERSCORE,
    LOWER_UNDERSCORE
  }

  public interface IndexOf {
    int apply(String s, String subString);
  }

  public static interface LowerCamel {
    String convertToString(String s);
  }

  /**
   * 文字列が空かどうかを判定するための {@link Predicate} 実装です。
   *
   * <p>このクラスは、与えられた文字列が空文字列または {@code null} である場合に真を返します。 それ以外の場合は偽を返します。
   *
   * <p>主に文字列に対する空チェックを簡潔かつ明示的に行う用途で使用されます。
   */
  public static class IsEmpty implements Predicate<String> {
    /**
     * 指定された文字列が空であるか、または {@code null} であるかを判定します。
     *
     * @param t 判定対象の文字列。{@code null} の場合は空とみなされます。
     * @return 文字列が空または {@code null} の場合は {@code true}、それ以外の場合は {@code false} を返します。
     */
    @Override
    public boolean test(String t) {
      if (t == null) {
        return true;
      }
      return t.isEmpty();
    }
  }

  /**
   * Surround クラスは、指定された文字列を別の文字列で囲む処理を行うクラスです。
   *
   * <p>このクラスは {@link TwoFunction} インタフェースを実装しており、 2つの文字列を入力として受け取り、1つの結果文字列を返します。
   */
  public static class Surround implements TwoFunction<String, String, String> {
    @Override
    public String apply(String str, String surroundString) {
      if (IS_NULL.test(surroundString)) {
        surroundString = EMPTY;
      }
      if (IS_NULL.test(str)) {
        str = EMPTY;
      }

      return surroundString + str + surroundString;
    }
  }

  public static class IndexOfImpl implements IndexOf {

    @Override
    public int apply(String s, String subString) {
      if (IS_NULL.test(s) || IS_NULL.test(subString)) {
        return -1;
      }
      return s.indexOf(subString);
    }
  }

  /** LowerCamelImplは、入力文字列をLowerCamel形式に変換するための実装クラスです。 */
  public static class LowerCamelImpl implements LowerCamel {

    /**
     * 入力された文字列をLowerCamel形式に変換します。
     *
     * <p>入力文字列がnullの場合、空文字を返します。 入力文字列が非nullの場合、先頭文字を小文字に変換し、それ以外の部分はそのまま返します。
     *
     * @param s 変換対象の文字列。nullが許容されます。
     * @return LowerCamel形式に変換された文字列。入力がnullの場合は空文字を返します。
     */
    @Override
    public String convertToString(String s) {
      if (IS_NULL.test(s)) {
        return "";
      }
      if (s.isEmpty()) {
        return "";
      }
      if (s.contains(UNDERSCORE)) {
        String[] words = s.toLowerCase().split(UNDERSCORE);
        if (words.length == 0) {
          return "";
        }
        StringBuilder builder = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
          if (words[i].isEmpty()) {
            continue;
          }
          builder.append(words[i].substring(0, 1).toUpperCase());
          builder.append(words[i].substring(1));
        }
        return builder.toString();
      }
      if (s.matches("[A-Z]+")) {
        return s.toLowerCase();
      }
      return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
  }

  /**
   * Repeat クラスは、指定された文字列を指定された回数だけ繰り返して結合した文字列を生成する機能を提供します。
   *
   * <p>このクラスは {@link Function} インターフェースを実装しており、入力文字列に対して繰り返し操作を実行します。
   *
   * <p>主な機能: - 入力文字列を指定回数繰り返し、その結果を1つの文字列として結合します。 - コンストラクタで指定された繰り返し回数が正の整数であることを強制します。 - 入力文字列が
   * null でないことを保証します。
   *
   * <p>使用制限: - 繰り返し回数は正の整数である必要があります。それ以外の場合は例外をスローします。 - 入力文字列が null の場合も例外をスローします。
   *
   * <p>例外の取り扱い: - 繰り返し回数が正の整数でない場合は {@link IllegalArgumentException} がスローされます。 - 入力文字列が null の場合は
   * {@link IllegalArgumentException} がスローされます。
   */
  @RequiredArgsConstructor
  public class Repeat implements Function<String, String> {
    private final int times;

    @Override
    public String apply(String s) {
      CHECK_POSITIVE.apply(times, () -> new IllegalArgumentException("times must be positive"));
      CHECK_NOT_NULL.apply(s, () -> new IllegalArgumentException("s must not be null"));
      return String.join("", java.util.Collections.nCopies(times, s));
    }
  }
}
