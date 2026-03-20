package undecided.shared.common.primitiveOld;

import static undecided.erp.common.precondition.ArrayPrecondition.checkAllElementNotNull;
import static undecided.erp.common.precondition.ArrayPrecondition.checkLengthAtLeast;

import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import undecided.erp.common.precondition.IndexedRuntimeException;

/** 列挙型およびそれに関連する機能を扱うためのユーティリティクラスです。 */
@UtilityClass
public class Enums {

  /**
   * 与えられた列挙の値の序数に基づいて値の配列から値を返します。
   *
   * @param <EnumType> 列挙の型
   * @param <ValueType> 値の型
   * @param targetEnum 配列から値を取得するために使用される列挙の値
   * @param values 値の配列
   * @return 列挙の値の序数に対応する配列からの値
   * @throws IllegalArgumentException 値の配列の長さが序数+1より小さい場合
   * @throws NullPointerException enumValueまたはvaluesがnullの場合
   */
  @SafeVarargs
  public static <EnumType extends Enum<EnumType>, ValueType> ValueType getValueByEnumOrdinal(
      @NonNull EnumType targetEnum, @NonNull ValueType... values) {
    final int ordinal = targetEnum.ordinal();
    final int minimumLength = ordinal + 1;
    checkLengthAtLeast(
        values,
        () -> new IllegalArgumentException("values length must be at least " + minimumLength),
        minimumLength);
    return values[ordinal];
  }

  /**
   * 列挙の順序値に基づいて値の配列から値を取得します。
   *
   * @param <EnumType> 列挙型のタイプ
   * @param <ValueType> 値のタイプ
   * @param targetEnum 配列から値を取得するための列挙値
   * @param valueSuppliers 値の提供者の配列
   * @return 値の提供者の配列から列挙値の順序に対応する値
   * @throws IllegalArgumentException 値供給者配列の長さが順序 + 1 未満の場合
   * @throws IndexedRuntimeException 任意の値供給者がnullの場合
   * @throws NullPointerException enumValueまたはvalueSuppliersがnullの場合
   */
  @SafeVarargs
  public static <EnumType extends Enum<EnumType>, ValueType>
      ValueType getValueByEnumOrdinalFromSuppliers(
          @NonNull EnumType targetEnum, @NonNull Supplier<ValueType>... valueSuppliers) {
    final int ordinal = targetEnum.ordinal();
    final int minimumLength = ordinal + 1;
    checkLengthAtLeast(
        valueSuppliers,
        () ->
            new IllegalArgumentException("valueSuppliers length must be at least " + minimumLength),
        minimumLength);
    checkAllElementNotNull(
        valueSuppliers,
        (index) -> new IndexedRuntimeException("all value supplier must not null.", index));

    return valueSuppliers[ordinal].get();
  }
}
