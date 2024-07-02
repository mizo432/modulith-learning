package undecided.erp.common.primitive;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Arrays2 {

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
   * このメソッドは、指定された配列のすべての要素が非nullであることを確認します。
   *
   * @param array 確認する配列
   * @return すべての要素が非nullの場合はtrue、そうでない場合はfalse
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
    return Arrays.stream(array);
    
  }
}
