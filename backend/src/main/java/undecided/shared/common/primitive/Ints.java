package undecided.shared.common.primitive;

import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import undecided.shared.functional.ExceptionAwareCheck;

@UtilityClass
public class Ints {
  public static final int ZERO = 0;
  public static final CheckPositive<RuntimeException> CHECK_POSITIVE = new CheckPositive<>();

  public static class CheckPositive<E extends RuntimeException>
      implements ExceptionAwareCheck<Integer, E> {

    @Override
    public Integer apply(Integer i, @NonNull Supplier<E> runtimeExceptionSuppler) throws E {
      if (i <= ZERO) throw runtimeExceptionSuppler.get();
      return i;
    }
  }
}
