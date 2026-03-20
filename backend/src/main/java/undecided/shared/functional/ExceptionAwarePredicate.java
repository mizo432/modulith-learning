package undecided.shared.functional;

import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * ExceptionAwarePredicate インタフェイスは、入力値に基づいて boolean の結果を提供し、必要に応じて例外をスローする 機能を提供する関数型インタフェイスです。
 *
 * @param <T> 処理の対象となる入力値の型
 * @param <E> 処理中にスローされる可能性のある実行時例外の型
 */
@FunctionalInterface
public interface ExceptionAwarePredicate<T, E extends RuntimeException> {
  /**
   * 指定された入力値を用いて条件を判定し、必要に応じて例外をスローします。
   *
   * @param t 判定の対象となる入力値
   * @param exceptionSupplier 条件を満たさなかった場合にスローされる例外を供給するサプライヤ
   * @return 条件が満たされる場合は true、それ以外の場合は false
   * @throws E 条件を満たさなかった場合にスローされる例外
   */
  boolean test(@NonNull T t, @NonNull Supplier<E> exceptionSupplier) throws E;
}
