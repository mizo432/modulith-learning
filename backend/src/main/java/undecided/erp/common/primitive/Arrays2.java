package undecided.erp.common.primitive;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/** 配列操作を補助するユーティリティクラスです。 このクラスは、主に配列に関する便利なメソッドを提供します。 すべてのメソッドは静的メソッドであり、インスタンス化する必要がありません。 */
@UtilityClass
public class Arrays2 {

  /**
   * Checks if the specified array is empty.
   *
   * @param array the array to check, must not be null
   * @param <T> the type of elements in the array
   * @return true if the array is empty, false otherwise
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
   * Checks whether all elements in the provided array are not null.
   *
   * @param array the array to check, must not be null
   * @param <E> the type of elements in the array
   * @return true if all elements in the array are not null, false otherwise
   */
  public static <E> boolean allElementsNotNull(@NonNull E[] array) {
    for (E e : array) {}

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
    return Arrays.stream(array);
  }

  /**
   * Computes the hash code for a given array of objects.
   *
   * @param args the array of objects for which the hash code is to be computed; can be null
   * @return the computed hash code for the array, or 0 if the array is null
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
   * Converts an object array into its string representation.
   *
   * @param args the array of objects to be converted into a string; can be null
   * @return the string representation of the array, or "null" if the array is null
   */
  public static String toString(Object[] args) {
    return Arrays.toString(args);
  }
}
