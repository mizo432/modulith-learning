package undecided.erp.common.primitive;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;
import com.ibm.icu.text.UnicodeSet;

/**
 * The Strings2 class provides utility methods for working with strings.
 */
public class Strings2 {

  public static final String EMPTY = "";

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
   * 与えられた文字列が空かnullの場合、デフォルトの文字列を返します。
   *
   * @param str チェックする文字列
   * @param defaultString 与えられた文字列が空またはnullの場合に返すデフォルトの文字列
   * @return 与えられた文字列が空またはnullでない場合はその文字列、そうでない場合はデフォルトの文字列
   */
  public static String defaultIfEmpty(String str, String defaultString) {
    return isEmpty(str) ? EMPTY : str;

  }
}
