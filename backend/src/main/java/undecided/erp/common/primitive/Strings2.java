package undecided.erp.common.primitive;

import static undecided.erp.common.primitive.Objects2.defaultIfNull;
import static undecided.erp.common.primitive.Objects2.isNull;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;
import com.ibm.icu.text.UnicodeSet;
import undecided.erp.common.verifier.IntegerVerifiers;

/**
 * This class provides utility methods for handling strings.
 */
public class Strings2 {

  /**
   * An empty string constant.
   */
  public static final String EMPTY = "";
  /**
   * Represents a blank space.
   * <p>
   * This variable is a constant {@code String} that contains a single blank space character. It can
   * be used to represent an empty or blank value in various situations.
   * </p>
   */
  public static final String BLANK = " ";
  /**
   * {@code NARROW_WIDTH}変数は、Unicodeに基づいた狭い文字の幅を表す値です。 その値は定数であり、値は1です。
   * <p>
   * この変数は、{@code Strings2}クラスの{@code getWidthBasedOnUnicode}メソッドで文字の幅を
   * Unicode値に基づいて決定するために使用されます。このメソッドは、{@code UCharacter.getIntPropertyValue}
   * メソッドから得られた値がswitch文で定義されたどのケースにも一致しない場合、{@code NARROW_WIDTH}定数を使用します。
   * <p>
   * 注：{@code NARROW_WIDTH}変数はprivate、static、finalで宣言されているため、その所属するクラスの外部からは
   * 修正したりアクセスしたりすることはできません。
   */
  private static final int NARROW_WIDTH = 1;
  /**
   * {@code WIDE_WIDTH} 変数は、Unicodeに基づく幅値を表す変数です。 その値は定数で、2です。
   * <p>
   * この変数は {@code Strings2} クラスの {@code getWidthBasedOnUnicode} メソッドで使用され、 その Unicode
   * 値に基づいて文字の幅を決定します。 {@code UCharacter.getIntPropertyValue} メソッドから得た値が switch
   * 文で定義されたどのケースとも一致しない場合、 {@code WIDE_WIDTH} 定数が使用されます。
   * <p>
   * 注意： {@code WIDE_WIDTH} 変数は private、static、final として宣言されているため、
   * それを含むクラスの外部から変更したりアクセスしたりすることはできません。
   */
  private static final int WIDE_WIDTH = 2;
  /**
   * {@code HALF_WIDTH_UNICODE_SET} represents a UnicodeSet that contains halfwidth, narrow, and
   * neutral characters based on East Asian Width property.
   */
  private static final UnicodeSet HALF_WIDTH_UNICODE_SET =
      new UnicodeSet(
          "[[:East_Asian_Width=Halfwidth:][:East_Asian_Width=Narrow:][:East_Asian_Width=Neutral:]]");
  private static final UnicodeSet FULL_WIDTH_UNICODE_SET =
      new UnicodeSet(
          "[[:East_Asian_Width=Fullwidth:][:East_Asian_Width=Wide:][:East_Asian_Width=Ambiguous:]]");

  /**
   * 与えられた文字列が空かどうかを確認します。
   *
   * @param str 確認する文字列
   * @return 文字列が空または{@code null}の場合は{@code true}、それ以外の場合は{@code false}を返します。
   */
  public static boolean isEmpty(String str) {
    if (str == null) {
      return true;
    }
    return EMPTY.equals(str);
  }

  /**
   * 指定された文字列の半角の長さを返します。
   *
   * @param str 半角の長さを計算する文字列
   * @return 文字列の半角の長さ
   */
  public static int getHalfWidthLength(String str) {
    if (str == null) {
      return 0;
    }
    int length = 0;
    for (char c : str.toCharArray()) {
      length = addCharacterLength(length, c);
    }
    return length;
  }

  /**
   * Unicodeの幅に基づいて、文字の長さを指定された長さに追加します。
   *
   * @param length 初期の長さ
   * @param c 追加する文字
   * @return 文字の幅を追加した後の更新された長さ
   */
  private static int addCharacterLength(int length, char c) {
    return length + getWidthBasedOnUnicode(c);
  }

  /**
   * 与えられたコードポイントのユニコードに基づいて幅値を返します。
   *
   * @param codePoint 幅を決定するためのユニコードのコードポイント
   * @return ユニコードのコードポイントに基づいた幅値。コードポイントが全角、広い、または曖昧な東アジアの幅を持つ場合、 {@code WIDE_WIDTH}
   * を返します。それ以外の場合は、{@code NARROW_WIDTH} を返します。
   */
  private static int getWidthBasedOnUnicode(int codePoint) {
    int value = UCharacter.getIntPropertyValue(codePoint, UProperty.EAST_ASIAN_WIDTH);
    return switch (value) {
      case UCharacter.EastAsianWidth.FULLWIDTH, UCharacter.EastAsianWidth.WIDE,
           UCharacter.EastAsianWidth.AMBIGUOUS -> WIDE_WIDTH;
      default -> NARROW_WIDTH;
    };
  }

  /**
   * 与えられた文字列内の全ての文字が半角文字であるかどうかを判断します。
   *
   * @param str チェックする文字列。
   * @return 全ての文字が半角の場合は {@code true}、そうでない場合は {@code false} を返します。
   */
  public static boolean isAllCharacterHalfWidth(String str) {
    if (isEmpty(str)) {
      return true;
    }

    for (char c : str.toCharArray()) {
      if (!HALF_WIDTH_UNICODE_SET.contains(c)) {
        return false;
      }
    }
    return true;

  }

  /**
   * 与えられた文字列内の全ての文字が半角文字であるかどうかを判断します。
   *
   * @param str チェックする文字列。
   * @return 全ての文字が半角の場合は {@code true}、そうでない場合は {@code false} を返します。
   */
  public static boolean isAllCharacterFullWidth(String str) {
    if (isEmpty(str)) {
      return true;
    }
    for (char c : str.toCharArray()) {
      if (!FULL_WIDTH_UNICODE_SET.contains(c)) {
        return false;
      }
    }
    return true;

  }

  /**
   * 入力された文字列が null の場合は空文字列を返し、それ以外の場合は入力された文字列そのものを返します。
   *
   * @param str null かどうかを確認するための入力文字列
   * @return 入力された文字列が null の場合は空文字列、それ以外の場合は入力された文字列そのもの
   */
  public static String emptyIfNull(String str) {
    return defaultIfNull(str, EMPTY);

  }

  /**
   * 与えられた文字列が空かnullの場合、デフォルトの文字列を返します。
   *
   * @param str チェックする文字列
   * @param defaultString 与えられた文字列が空またはnullの場合に返すデフォルトの文字列
   * @return 与えられた文字列が空またはnullでない場合はその文字列、そうでない場合はデフォルトの文字列
   */
  public static String defaultIfEmpty(String str, String defaultString) {
    return isEmpty(str) ? defaultString : str;

  }

  /**
   * 与えられた文字列が空またはnullの場合はnullを返し、そうでない場合は与えられた文字列を返します。 このメソッドは、空またはnullの文字列を異なる方法で扱いたいシナリオで便利です。
   *
   * @param str 確認されるべき文字列
   * @return 文字列が空またはnullの場合はnull、それ以外の場合は与えられた文字列
   */
  public static String nullIfEmpty(String str) {
    return defaultIfEmpty(str, null);

  }

  /**
   * 文字列が指定したプレフィックスで始まるかどうかを確認します。
   *
   * @param str 確認する文字列
   * @param prefix 比較対象のプレフィックス
   * @return 文字列がプレフィックスで始まる場合はtrue、そうでない場合はfalseを返します
   */
  public static boolean startWith(String str, String prefix) {
    if (str == null || prefix == null) {
      return false;
    }
    final int length1 = str.length();
    final int length2 = prefix.length();
    if (length1 < length2) {
      return false;
    }
    final String s1 = str.substring(0, prefix.length());
    return s1.equals(prefix);

  }

  /**
   * 与えられた文字列が指定された接頭辞で始まるかどうかを（大文字・小文字を区別せずに）判断します。
   *
   * @param str チェックする文字列
   * @param prefix 比較する接頭辞
   * @return 文字列が接頭辞で始まる場合（大文字・小文字を区別せず）は{@code true}、そうでない場合は{@code false}
   */
  public static boolean startsWithIgnoreCase(final String str,
      final String prefix) {
    if (str == null || prefix == null) {
      return false;
    }
    final int length1 = str.length();
    final int length2 = prefix.length();
    if (length1 < length2) {
      return false;
    }
    final String s1 = str.substring(0, prefix.length());
    return s1.equalsIgnoreCase(prefix);
  }

  /**
   * 与えられた文字列が指定された接尾辞で終わるかどうかを決定します。
   *
   * @param str チェックする文字列。
   * @param suffix 比較する接尾辞。
   * @return 文字列が接尾辞で終わる場合は {@code true}、それ以外の場合は {@code false}。
   */
  public static boolean endsWith(final String str,
      final String suffix) {
    if (str == null || suffix == null) {
      return false;
    }
    final int length1 = str.length();
    final int length2 = suffix.length();
    if (length1 < length2) {
      return false;
    }
    final String s1 = str.substring(length1 - length2);
    return s1.equals(suffix);
  }

  /**
   * 指定された接尾辞で指定された文字列が終了しているかどうかを、大文字と小文字を無視して判断します。
   *
   * @param str 対象となる文字列。
   * @param suffix 比較する接尾辞。
   * @return 文字列が大文字と小文字を区別せずに接尾辞で終わる場合は {@code true}、 そうでない場合は {@code false}。
   */
  public static boolean endsWithIgnoreCase(final String str,
      final String suffix) {
    if (str == null || suffix == null) {
      return false;
    }
    final int length1 = str.length();
    final int length2 = suffix.length();
    if (length1 < length2) {
      return false;
    }
    final String s1 = str.substring(length1 - length2);
    return s1.equalsIgnoreCase(suffix);
  }

  /**
   * 文字列から先頭の空白を取り除く。
   *
   * @param str 取り除かれるべき文字列。
   * @return 取り除かれた文字列。もし入力の文字列がnullなら、空の文字列が返される。
   */
  public static String ltrim(final String str) {
    return ltrim(str, null);

  }

  /**
   * 文字列の先頭から特定の文字を削除します。
   *
   * @param str トリム対象の入力文字列。
   * @param trim 文字列の先頭から削除するための文字。
   * @return 先頭から特定の文字が削除された新しい文字列を返します。入力の文字列がnullの場合、nullを返します。トリムの文字がnullの場合、空の文字列がトリムに使用されます。
   */
  public static String ltrim(final String str, String trim) {
    if (isNull(str)) {
      return null;
    }
    if (isNull(trim)) {
      trim = BLANK;
    }
    if (str.length() < trim.length()) {
      return str;
    }

    String result = str;

    while (result.startsWith(trim)) {
      result = result.substring(trim.length());
    }

    return result;
  }

  /**
   * 与えられた文字列から末尾の空白を除去します。文字列が null の場合は null を返します。
   *
   * @param str 末尾の空白を除去する文字列
   * @return 末尾の空白が除去された文字列、または入力文字列が null の場合は null
   */
  public static String rtrim(final String str) {
    return rtrim(str, null);
  }

  /**
   * 与えられた文字列から 'trim' パラメータで指定された末尾の文字を削除します。
   *
   * @param str トリム対象の文字列。nullであってはいけません。
   * @param trim 文字列の末尾から削除する文字。nullの場合、空白文字が使用されます。
   * @return トリムされた文字列。
   */
  public static String rtrim(final String str, String trim) {
    if (str == null) {
      return null;
    }

    if (str.length() < (trim == null ? 1 : trim.length())) {
      return str;
    }

    trim = (trim == null) ? " " : trim;

    if (str.endsWith(trim)) {
      return rtrim(str.substring(0, str.length() - trim.length()), trim);
    }

    return str;
  }

  /**
   * 与えられた文字列から先頭と末尾の空白をトリム（削除）します。
   *
   * @param str トリムする文字列、nullも可能です
   * @return トリムされた文字列、もしくは入力の文字列がnullの場合はnullを返します
   */
  public static String trim(final String str) {
    return trim(str, null);

  }

  /**
   * 指定された文字を文字列の先頭と末尾から削除し、与えられた文字列をトリムします。
   *
   * @param str トリムする文字列。
   * @param trim 文字列から削除する文字。nullの場合、デフォルトは単一のスペース文字です。
   * @return トリムされた文字列。
   */
  public static String trim(final String str, final String trim) {
    return ltrim(rtrim(str, trim), trim);
  }

  /**
   * もし存在する場合、テキストから指定されたプレフィクスをトリムします。
   *
   * @param text プレフィクスをトリムするべきテキスト。
   * @param prefix トリムするためのプレフィクス。
   * @return プレフィクスが存在する場合はトリムしたテキスト、存在しない場合は元のテキスト。 テキストがnullの場合はnull。 テレフィックスがnullの場合はテキスト。
   */
  public static String trimPrefix(final String text, final String prefix) {
    if (text == null) {
      return null;
    }
    if (prefix == null) {
      return text;
    }
    if (text.startsWith(prefix)) {
      return text.substring(prefix.length());
    }
    return text;
  }

  /**
   * 指定したテキストから特定の接尾辞を削除します。
   *
   * @param text 接尾辞を削除するテキスト。
   * @param suffix テキストから削除する接尾辞。
   * @return 接尾辞が削除されたテキスト、または接尾辞で終わらない場合は元のテキスト。テキストがnullの場合はnull。 テレフィックスがnullの場合はテキスト。
   */
  public static String trimSuffix(final String text, final String suffix) {
    if (text == null) {
      return null;
    }
    if (suffix == null) {
      return text;
    }
    if (text.endsWith(suffix)) {
      return text.substring(0, text.length() - suffix.length());
    }
    return text;
  }

  /**
   * 指定した文字列が十進数であるかどうかを確認します。
   *
   * @param value チェックする文字列
   * @return 文字列が十進数であればtrue、そうでなければfalseを返します
   */
  public static boolean isDecimal(String value) {
    if (isNull(value)) {
      return true;
    }
    if (isEmpty(value)) {
      return true;
    }
    return value.matches("[0-9]*");

  }

  /**
   * 与えられた文字列の長さを計算します。
   *
   * @param value 長さを計算する文字列
   * @return 文字列の長さ、または文字列がnullの場合は0
   */
  public static int length(String value) {
    if (isNull(value)) {
      return 0;
    }
    return value.length();

  }

  /**
   * 与えられたstrがsurroundStringに囲まれた新しい文字列を返します。
   *
   * @param str surroundStringで囲む文字列str (nullは空文字列として扱われます)
   * @param surroundString strを囲むための文字列 (nullは空文字列として扱われます)
   * @return strがsurroundStringに囲まれた結果の文字列
   */
  public static String surround(String str, String surroundString) {
    if (isNull(surroundString)) {
      surroundString = EMPTY;
    }
    if (isNull(str)) {
      str = EMPTY;
    }

    return surroundString + str + surroundString;
  }

  /**
   * 指定した回数だけ指定した文字列を繰り返します。
   *
   * @param str 繰り返す文字列。
   * @param times 文字列を繰り返す回数。
   * @return 繰り返された文字列。
   */
  public static String repeat(String str, int times) {
    IntegerVerifiers.verifyPositiveOrZero(times,
        () -> new IllegalArgumentException("times must not negative times=" + times));

    return repeat(str, times, EMPTY);
  }

  /**
   * 指定した数の回数だけ文字列を繰り返し、各繰り返しの間に分割記号を入れる。
   *
   * @param str 繰り返す文字列
   * @param times 文字列を繰り返したい回数
   * @param separator 各反復間で配置しんだ分割記号
   * @return 指定した数の反復と分割記号を含む結果の文字列
   */
  public static String repeat(String str, int times, String separator) {
    IntegerVerifiers.verifyPositiveOrZero(times,
        () -> new IllegalArgumentException("times must not negative times=" + times));
    if (isNull(str)) {
      str = EMPTY;
    }
    if (isNull(separator)) {
      separator = EMPTY;
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(str).append(separator);
    }
    if (!sb.isEmpty()) {
      sb.setLength(sb.length() - separator.length());
    }

    return sb.toString();
  }

  /**
   * 文字列の最初の文字を小文字に変換します。
   *
   * @param str 入力文字列
   * @return 最初の文字が大文字である場合、その文字を小文字に変換した入力文字列を返します。文字列が空であるか、最初の2文字が大文字である場合、同じ入力文字列をそのまま返します。
   */
  public static String uncapitalize(String str) {
    if (isEmpty(str)) {
      return str;
    }
    final char[] chars = str.toCharArray();
    if (chars.length >= 2 && Character.isUpperCase(chars[0])
        && Character.isUpperCase(chars[1])) {
      return str;
    }
    chars[0] = Character.toLowerCase(chars[0]);
    return new String(chars);
  }
}
