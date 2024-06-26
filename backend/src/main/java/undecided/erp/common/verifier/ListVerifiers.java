package undecided.erp.common.verifier;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ListVerifiers {

  /**
   * 与えられたリストが空でないことを確認します。
   *
   * @param <T> リスト内の要素のタイプ
   * @param list チェックするリスト
   * @return リストが空でない場合は入力リスト
   * @throws IllegalArgumentException リストが空の場合
   */
  public static <T> List<T> verifyNotEmpty(List<T> list) {
    return verifyNotEmpty(list, () -> new IllegalArgumentException("List must not be empty"));

  }

  /**
   * 指定されたリストが空でないことを確認します。
   *
   * @param <T> リスト内の要素のタイプ
   * @param list 確認するリスト
   * @param supplier リストが空の場合に例外を生成する供給機能
   * @return リストが空でない場合は入力リスト
   * @throws RuntimeException リストが空で、supplierによって生成された場合
   */
  public static <T> List<T> verifyNotEmpty(List<T> list,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (list == null) {
      return list;
    }

    if (list.isEmpty()) {
      throw supplier.get();
    }
    return list;
  }

  /**
   * 与えられたリストのすべての要素がnullでないことを確認します。
   * <p>
   * 任意の要素がnullの場合、対応するインデックスの例外を投げます。
   *
   * @param <E> リスト内の要素のタイプ
   * @param list チェックするリスト
   * @param function 与えられたインデックスの例外を生成する関数
   * @return すべての要素がnullでない場合は入力リスト
   * @throws IndexedRuntimeException リストの任意の要素がnullの場合、null要素のインデックスと共に
   */
  public static <E> List<E> verifyAllElementNotNull(List<E> list,
      @NonNull Function<Integer, ? extends IndexedRuntimeException> function) {
    if (list == null) {
      return list;
    }

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i) == null) {
        throw function.apply(i);
      }

    }
    return list;
  }

  /**
   * 与えられたリストの任意の要素が null でないか確認します。
   * <p>
   * もし null の要素があれば、指定した例外をスローします。
   *
   * @param <E> リスト内の要素の型
   * @param list チェックするリスト
   * @param supplier 例外を生成するためのサプライヤ
   * @return 全ての要素が null でない場合、入力リスト
   * @throws RuntimeException リスト内の任意の要素が null の場合
   */
  public static <E> List<E> verifyAnyElementNotNull(List<E> list,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (list == null) {
      return list;
    }

    if (list.stream().noneMatch(Objects::nonNull)) {
      throw supplier.get();
    }

    return list;
  }

  /**
   * 与えられたリストに厳密に1つの非null要素があることをチェックします。
   * <p>
   * 1つ以上の非null要素を含んでいた場合は、例外がスローされます。
   *
   * @param <E> リスト内の要素の型
   * @param list チェックするリスト
   * @param supplier 例外を作成するために使用するサプライヤー
   * @return チェックがパスした場合は入力リスト
   * @throws RuntimeException 1つ以上の非null要素を含んでいる場合
   */
  public static <E> List<E> verifyOneElementNotNull(List<E> list,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (list == null) {
      return list;
    }

    long numberOfNonNullElements = list.stream().filter(Objects::nonNull).count();

    if (numberOfNonNullElements != 1) {
      throw supplier.get();
    }

    return list;
  }
}
