package undecided.erp.common.verifier;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import lombok.NonNull;

/**
 * SetPreconditionsクラスは、セットに対する前提条件をチェックするメソッドを提供します。
 */
public class SetVerifiers {

  public static <T> Set<T> checkNotEmpty(Set<T> set) {
    return checkNotEmpty(set, () -> new IllegalArgumentException("List must not be empty"));

  }

  /**
   * 指定されたセットが空でないことを確認します。 セットがnullまたは空の場合、指定された例外がスローされます。
   *
   * @param set 空でないことを確認するセット（null可能）。
   * @param supplier セットが空の場合にスローされる例外の供給者（非null）。
   * @param <T> セット内の要素のタイプ。
   * @return セットが空でない場合は元のセット。
   * @throws RuntimeException セットがnullまたは空の場合。
   */
  public static <T> Set<T> checkNotEmpty(Set<T> set,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (set == null) {
      return set;
    }

    if (set.isEmpty()) {
      throw supplier.get();
    }
    return set;
  }

  /**
   * @param set null要素を検査するセット（nullable）
   * @param supplier null要素が見つかった場合に投げられる例外を提供するsupplier（nonnull）
   * @param <E> 検査するセットの要素の型
   * @return null要素が見つからなかった場合は、元のセット
   * @throws RuntimeException セット内にnull要素が見つかった場合
   */
  public static <E> Set<E> checkAllElementNotNull(Set<E> set,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (set == null) {
      return set;
    }
    for (E e : set) {
      if (e == null) {
        throw supplier.get();
      }

    }
    return set;
  }

  /**
   * 与えられたセットの要素が全てnullでないかを確認します。
   *
   * @param set null要素の存在を確認するためのセット（nullable）
   * @param exceptionSupplier 非null要素が見つからない場合にスローする例外を提供する{@code Supplier}（非null）
   * @param <E> セット内の要素の種類
   * @return 非null要素が見つかった場合は元のセット
   * @throws RuntimeException セット内で非nullの要素が見つからない場合
   */
  public static <E> Set<E> checkAnyElementNotNull(Set<E> set,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (set == null || set.stream().anyMatch(Objects::nonNull)) {
      return set;
    }
    throw exceptionSupplier.get();
  }

  /**
   * セットが正確に1つの非null要素を含むかどうかを確認する。
   *
   * @param set チェックするセット（null可）。
   * @param exceptionSupplier セットが正確に1つの非null要素を含まない場合にスローされる例外を提供するサプライヤ（non-null）。
   * @param <E> セット内の要素の種類。
   * @return 具体的に1つの非null要素が含まれている場合は元のセット。
   * @throws RuntimeException セットがnullまたは具体的に1つの非null要素を含まない場合。
   */
  public static <E> Set<E> checkOneElementNotNull(Set<E> set,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {

    if (set != null && set.stream().filter(Objects::nonNull).count() != 1) {
      throw exceptionSupplier.get();
    }
    return set;
  }
}
