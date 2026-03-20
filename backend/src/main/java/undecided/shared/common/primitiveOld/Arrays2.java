package undecided.shared.common.primitiveOld;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import undecided.erp.common.exception.SystemException;

/**
 * 配列操作を補助するユーティリティクラスです。
 *
 * <p>このクラスは、主に配列に関する便利なメソッドを提供します。 すべてのメソッドは静的メソッドであり、インスタンス化する必要がありません。
 */
@UtilityClass
public class Arrays2 {

  /**
   * 配列が空であるかどうかを判定します。
   *
   * @param <T> 配列内の要素の型
   * @param array 判定対象の非null配列
   * @return 配列が空の場合はtrue、それ以外の場合はfalse
   */
  public static <T> boolean isEmpty(@NonNull T[] array) {
    return array.length == 0;
  }

  /**
   * 配列が空かどうかを確認します。
   *
   * @param array 確認する配列
   * @param <T> 配列内の要素のタイプ
   * @return 配列がnullまたは0の長さであれば真、それ以外の場合は偽
   */
  public static <T> boolean isEmptySilently(T[] array) {
    return (array == null || array.length == 0);
  }

  /**
   * 与えられた配列のすべての要素が非nullであるかを確認します。
   *
   * @param <E> 配列の要素の型
   * @param array nullチェックを行う対象の非null配列
   * @return 配列内のすべての要素が非nullである場合はtrue。1つでもnullが含まれている場合はfalse。
   * @throws NullPointerException 配列そのものがnullである場合
   */
  public static <E> boolean allElementsNotNull(@NonNull E[] array) {
    for (E e : array) {
      if (e == null) {
        return false;
      }
    }

    return true;
  }

  /**
   * このメソッドは配列を引数にとり、非null要素のストリームを返します。
   *
   * @param array ストリームを作成する配列
   * @param <T> 配列の要素の型
   * @return 入力配列からの非null要素のストリーム
   */
  public static <T> Stream<@NonNull T> stream(@NonNull T[] array) {
    checkNotNull(
        array, () -> new SystemException("array must not be null!", new NullPointerException()));

    return Arrays.stream(array);
  }

  /**
   * 指定されたオブジェクト配列のハッシュコードを計算して返します。
   *
   * <p>配列内の各要素のハッシュコードを基に計算が行われます。
   *
   * @param args ハッシュコードを計算する対象のオブジェクト配列
   * @return 引数の配列に基づくハッシュコードの値
   */
  public static int hash(Object[] args) {
    return Arrays.hashCode(args);
  }

  /**
   * {@link Arrays#equals(Object[], Object[])} を使用して2つの配列を比較し、等しいかどうかを判定します。
   *
   * @param args 比較する最初の配列
   * @param args1 比較する2番目の配列
   * @return 2つの配列が等しい場合はtrue、それ以外の場合はfalse
   */
  public static boolean equal(Object[] args, Object[] args1) {

    return Arrays.equals(args, args1);
  }

  /**
   * 指定された配列を文字列表現に変換します。
   *
   * @param args 文字列に変換するオブジェクトの配列。配列内の要素は {@code toString} メソッドを使用して 各要素を文字列化します。{@code null}
   *     が指定された場合は {@code "null"} を返します。
   * @return 配列を表す文字列。例えば、配列内の要素が "a", "b", "c" の場合は {@code "[a, b, c]"} が返されます。
   */
  public static String toString(Object[] args) {
    return Arrays.toString(args);
  }
}
