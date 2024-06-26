package undecided.erp.common.primitive;

import org.checkerframework.checker.nullness.qual.Nullable;

public class NullnessCasts {

  static <T extends Object> T uncheckedCastNullableTToT(T t) {
    return t;
    
  }

  static <T extends @Nullable Object> T unsafeNull() {
    return null;

  }

}
