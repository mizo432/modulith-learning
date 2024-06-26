package undecided.erp.common.verifier;

import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BooleanVerifiers {

  public static Boolean verifyTrue(Boolean ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (ref) {
      return ref;
    }
    throw exceptionSupplier.get();
  }

  public static Boolean verifyFalse(Boolean ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (!ref) {
      return ref;
    }
    throw exceptionSupplier.get();
  }


}
