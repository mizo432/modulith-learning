package undecided.shared.common.primitive;

import java.util.Collection;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Collections2 {
  /**
   * コレクションが空であるかどうかを判定するための {@link Predicate} 定数です。 この定数を使用することで、任意の {@link Collection}
   * オブジェクトが要素を持たない場合に {@code true} を返します。
   *
   * <p>判定ロジックとして {@code Collection#isEmpty()} メソッドが実行されます。
   */
  public static final Predicate<Collection<?>> IS_EMPTY = isEmpty();

  /**
   * コレクションが空でないかどうかを判定するための {@link Predicate} 定数です。
   *
   * <p>この定数を使用すると、任意の {@link Collection} オブジェクトが要素を1つ以上持っている場合に {@code true} を返します。 判定ロジックには、{@link
   * Collections2#IS_EMPTY} を反転 (negate) した結果が使用されます。
   */
  public static final Predicate<Collection<?>> IS_NOT_EMPTY = isEmpty().negate();

  /**
   * 指定されたコレクションが空であるかどうかを判定する {@link Predicate} を生成します。
   *
   * <p>このメソッドで生成される Predicate は、コレクションの {@code isEmpty()} メソッドを使用して 要素が存在しない場合に {@code true} を返します。
   *
   * @param <T> 判定対象となるコレクションの型
   * @return コレクションが空である場合は {@code true} を返す {@link Predicate}
   */
  public static <T extends Collection<?>> Predicate<T> isEmpty() {
    return t -> t.isEmpty();
  }

  public static <T extends Collection<T>> Predicate<T> allElementsNotNull() {
    return t -> {
      for (T element : t) {
        if (Objects2.IS_NULL.test(element)) {
          return false;
        }
      }
      return true;
    };
  }
}
