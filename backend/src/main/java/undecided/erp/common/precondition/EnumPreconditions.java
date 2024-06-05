package undecided.erp.common.precondition;

import java.util.EnumSet;
import java.util.function.Supplier;
import lombok.NonNull;

public class EnumPreconditions {

  /**
   * 指定されたEnumSetに与えられた列挙型の定数が含まれているかどうかを確認します。含まれていない場合、サプライヤーによって指定された例外がスローされます。
   *
   * @param ref 確認する列挙型の定数
   * @param enumSet 確認するEnumSet
   * @param supplier 列挙型の定数が含まれていない場合にスローする例外を提供するサプライヤー
   * @param <E> 列挙型のタイプ
   * @return EnumSetに列挙型の定数が含まれている場合、同じ列挙型の定数
   * @throws RuntimeException 列挙型の定数がEnumSetに含まれていない場合
   */
  public static <E extends Enum<E>> E checkContains(E ref, @NonNull EnumSet<E> enumSet,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (!enumSet.contains(ref)) {
      throw supplier.get();
    }
    return ref;

  }

}
