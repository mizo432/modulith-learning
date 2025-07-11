package undecided.erp.common.primitive;

public class NullnessCasts {

  static <T> T uncheckedCastNullableTToT(T t) {
    return t;
  }

  static <T> T unsafeNull() {
    return null;
  }
}
