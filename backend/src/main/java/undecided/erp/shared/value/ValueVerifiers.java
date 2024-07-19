package undecided.erp.shared.value;

import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValueVerifiers {

  /**
   * 指定された参照が空でないことを検証します。参照が空の場合、exceptionSupplierで提供されるランタイム例外がスローされます。それ以外の場合、参照が返されます。
   *
   * @param ref 検証する参照
   * @param exceptionSupplier 参照が空の場合にスローする例外を提供するサプライヤー
   * @param <R> 参照のタイプ
   * @param <E> ランタイム例外のタイプ
   * @return 検証済みの参照
   * @throws E 参照が空の場合
   */
  public static <R extends MakeableEmpty, E extends RuntimeException> R verifyNotEmpty(
      @NonNull R ref, Supplier<E> exceptionSupplier) {
    if (ref.isEmpty()) {
      throw exceptionSupplier.get();
    }

    return ref;
  }
}
