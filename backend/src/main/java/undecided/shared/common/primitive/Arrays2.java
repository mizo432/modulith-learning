package undecided.shared.common.primitive;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Arrays2 {
  /**
   * 配列が空であるかどうかを判定するための {@link Predicate} 定数です。
   *
   * <p>この定数を使用することで、任意の配列が要素を持たない場合に {@code true} を返します。
   */
  public static final Predicate<Object[]> IS_EMPTY = isEmpty();

  /**
   * 配列が空でないかどうかを判定するための {@link Predicate} 定数です。
   *
   * <p>この定数を使用すると、任意の配列が要素を1つ以上持っている場合に {@code true} を返します。 判定ロジックには、{@link Arrays2#IS_EMPTY} を反転
   * (negate) した結果が使用されます。
   */
  public static final Predicate<Object[]> IS_NOT_EMPTY = isEmpty().negate();

  /**
   * 配列内の全ての要素が {@code null} ではないかどうかを判定する {@link Predicate} 定数です。
   *
   * <p>この定数を使用すると、配列内に1つでも {@code null} の要素が含まれている場合には {@code false} を返します。 配列の全ての要素が {@code null}
   * でない場合に限り {@code true} を返します。
   *
   * <p>判定ロジックには、ユーティリティクラス {@link Objects2} の {@link Objects2#IS_NULL} を使用して 各要素の {@code null}
   * チェックを行います。
   */
  public static final Predicate<Object[]> ALL_ELEMENTS_NOT_NULL = allElementsNotNull();

  /**
   * 配列を文字列表現に変換するための {@link Function} 定数です。
   *
   * <p>この定数は、任意の配列を {@link Arrays#toString(Object[])} メソッドを使用して文字列形式に変換します。
   * 配列の各要素をカンマ区切りで結合し、"[要素1, 要素2, ...]" の形式で返します。
   *
   * <p>例えば、空の配列が入力された場合は "[]" を返し、要素が存在する場合はその内容を文字列に変換します。
   *
   * <p>スレッドセーフに使用可能な汎用ユーティリティとして提供されます。
   */
  public static final Function<Object[], String> TO_STRING = Arrays2.toStrings();

  /**
   * 指定された配列が空であるかどうかを判定する {@link Predicate} を生成します。
   *
   * <p>このメソッドで生成される Predicate は、配列の {@code length} を使用して 要素が存在しない場合に {@code true} を返します。
   *
   * @param <T> 配列の要素の型
   * @return 配列が空である場合は {@code true} を返す {@link Predicate}
   */
  public static <T> Predicate<T[]> isEmpty() {
    return t -> t.length == 0;
  }

  /**
   * 配列の全ての要素が {@code null} ではないかを判定する {@link Predicate} を生成します。
   *
   * <p>このメソッドで生成される {@link Predicate} は、配列内の各要素に対し {@code Objects2.IS_NULL.test(element)} を適用し、1つでも
   * {@code true} を返した場合に {@code false} を返します。全要素が {@code null} でない場合に限り {@code true} を返します。
   *
   * @param <T> 配列の要素の型
   * @return 全ての要素が {@code null} でない場合は {@code true} を返す {@link Predicate}
   */
  public static <T> Predicate<T[]> allElementsNotNull() {
    return t -> {
      for (T element : t) {
        if (Objects2.IS_NULL.test(element)) {
          return false;
        }
      }
      return true;
    };
  }

  /**
   * 配列を文字列表現に変換する関数を生成します。
   *
   * <p>このメソッドで生成される {@link Function} は入力として与えられた配列を {@link Arrays#toString(Object[])}
   * メソッドを使用して文字列形式に変換します。
   *
   * @param <T> 配列の要素の型
   * @return 配列を文字列表現に変換する {@link Function}
   */
  public static <T> Function<T[], String> toStrings() {
    return t -> Arrays.toString(t);
  }
}
