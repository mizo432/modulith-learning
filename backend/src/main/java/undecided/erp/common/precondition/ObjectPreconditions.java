package undecided.erp.common.precondition;

import java.util.function.Supplier;

public class ObjectPreconditions {

  public static <T> void checkNotNull(T refer, Supplier<? extends RuntimeException> supplier) {
    if (refer == null) {
      throw supplier.get();
    }
  }

  public static <T> void checkNotNull(T refer, String label) {
    if (refer == null) {
      throw new NullPointerException(String.format("%s がnullです。", label));
    }
  }

  public static void checkArgument(boolean expression,
      Supplier<? extends RuntimeException> supplier) {
    if (!expression) {
      throw supplier.get();
    }
  }

  public static <T> void checkArgument(boolean expression,
      String label) {
    if (!expression) {
      throw new IllegalArgumentException(String.format("引数: %s が不正です。", label));
    }
  }

  public static void checkState(boolean expression,
      Supplier<? extends RuntimeException> supplier) {
    if (!expression) {
      throw supplier.get();
    }
  }

  public static <T> void checkState(boolean expression,
      String label) {
    if (!expression) {
      throw new IllegalStateException(String.format("%s の状態が不正です。", label));
    }
  }

}
