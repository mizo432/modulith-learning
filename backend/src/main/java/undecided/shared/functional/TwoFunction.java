package undecided.shared.functional;

import java.util.function.BiFunction;
import org.jspecify.annotations.Nullable;

/**
 * TwoFunction インタフェイスは、2つの入力値を受け取り、1つの結果を返す関数型インタフェイスです。 Java 標準の {@link BiFunction}
 * を拡張し、これに準拠しています。
 *
 * @param <T> 最初の引数の型
 * @param <U> 2番目の引数の型
 * @param <R> 戻り値の型
 */
@FunctionalInterface
public interface TwoFunction<T, U, R> extends BiFunction<T, U, R> {
  /**
   * 2つの入力値を使用して処理を実行し、結果を返します。
   *
   * @param t 最初の入力値。nullである可能性があります。
   * @param u 2番目の入力値。nullである可能性があります。
   * @return 処理結果となる値
   */
  @Override
  R apply(@Nullable T t, @Nullable U u);
}
