package undecided.shared.common.primitive;

import static undecided.shared.common.primitive.Ints.*;
import static undecided.shared.common.primitive.Objects2.*;

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
