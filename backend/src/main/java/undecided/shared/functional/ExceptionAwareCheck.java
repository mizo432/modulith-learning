package undecided.shared.functional;

import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * 関数型インタフェイス FunctionalA は、渡された入力および例外サプライヤを利用して特定の処理を実行する抽象メソッドを定義します。
 *
 * @param <T> 処理の入力となる引数の型
 * @param <E> 処理中にスローされる可能性のある実行時例外の型
 */
@FunctionalInterface
public interface ExceptionAwareCheck<T, E extends RuntimeException> {
  /**
   * 指定された入力値と例外サプライヤを使用して処理を実行します。
   *
   * @param t 処理の対象となる入力値
   * @param exceptionSupplier 処理中に例外をスローする必要がある場合に使用される例外サプライヤ
   * @return 処理結果となる値
   * @throws E 処理中に発生する可能性のある例外
   */
  T apply(@NonNull T t, @NonNull Supplier<E> exceptionSupplier) throws E;
}
