package undecided.erp.common.verifier;

import java.util.EnumSet;
import java.util.function.Supplier;
import lombok.NonNull;

public class EnumVerifiers {

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
  public static <E extends Enum<E>> E verifyContains(E ref, @NonNull EnumSet<E> enumSet,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (!enumSet.contains(ref)) {
      throw supplier.get();
    }
    return ref;

  }

  /**
   * 引数として与えられたtestValueがreferenceValueと等しくないことを確認します。
   * <p>
   * もし等しければ、指定されたsupplierを使って例外をスローします。
   *
   * @param referenceValue 比較するための参照値。
   * @param testValue 参照値との非等価性を確認するための値。
   * @param exceptionSupplier 値が等しい場合にスローされる例外を提供します。
   * @param <E> Enumの種類を示します。
   * @return 与えられたtestValueがそれと等しくない場合、参照値を返します。
   * @throws RuntimeException 与えられたtestValueが参照値と等しい場合。
   */
  public static <E extends Enum<E>> E verifyNotEqual(E referenceValue, @NonNull E testValue,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (testValue == referenceValue) {
      throw exceptionSupplier.get();
    }
    return referenceValue;
  }
}
