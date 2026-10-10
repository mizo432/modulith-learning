package undecided.shared.primitiveOld;

public class NullnessCasts {

  static <T> T uncheckedCastNullableTToT(T t) {
    return t;
  }

  static <T> T unsafeNull() {
    return null;
  }
}
