package undecided.shared.common.primitive;

import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import undecided.shared.functional.ExceptionAwareCheck;

/**
 * Objects2 クラスは、null 値や null チェック機能を提供するユーティリティクラスです。 主に、プリミティブ型の null
 * 値を定数として管理し、それを判定するための機能を提供します。
 */
@UtilityClass
public class Objects2 {
  /** 特定の意味を持つ定数としての null 値を表します。 この定数は、null 値が必要なケースで明示的に使用されることを想定しています。 */
  public static final Object NULL = null;

  public static final IsNull IS_NULL = new IsNull();
  public static final CheckNotNull CHECK_NOT_NULL = new CheckNotNull();

  public static class IsNull implements Predicate<Object> {
    @Override
    public boolean test(Object t) {
      return t == NULL;
    }
  }

  public static class CheckNotNull implements ExceptionAwareCheck<Object, RuntimeException> {

    @Override
    public Object apply(Object o, @NonNull final Supplier<RuntimeException> runtimeExceptionSuppler)
        throws RuntimeException {
      if (IS_NULL.test(o)) throw runtimeExceptionSuppler.get();
      return o;
    }
  }
}
