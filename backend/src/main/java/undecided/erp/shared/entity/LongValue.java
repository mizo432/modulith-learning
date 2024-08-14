package undecided.erp.shared.entity;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.util.function.Supplier;
import lombok.NonNull;
import undecided.erp.common.verifier.LongVerifiers;

/**
 * インターフェース LongValue は、Long 型の単一の値を表します。
 * <p>
 * このインターフェースは SingleValue インターフェースを拡張しています。
 *
 * @see SingleValue
 */
public interface LongValue<VO extends LongValue<VO>> extends SingleValue<Long> {

  public class LongValues {

    /**
     * 与えられた引用が正の整数であるかを検証し、そうでない場合はカスタム例外をスローします。
     *
     * @param ref 正のものであるかどうかを検証するための参照。null値も許可されます。
     * @param exceptionSupplier 参照が正でない場合にスローされるカスタム例外を提供する{@code Supplier}。
     * @return 参照がnullでなく、正の場合は正の参照。
     * @throws RuntimeException 参照がnullまたは正でない場合。
     */
    public static <VO extends LongValue<VO>> VO checkPositive(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkGreaterThan(ref, exceptionSupplier, 0L);
    }

    /**
     * 与えられたLongの値が正またはゼロであることを検証します。
     *
     * @param ref 検証するLongの値。 nullの場合、メソッドは例外を投げずにnullを返します。
     * @param exceptionSupplier 与えられた値が負である場合に投げられる適切なRuntimeExceptionを提供するSupplier。
     * @return 与えられた値が正またはゼロである場合、同じLongの値が返ります。それ以外の場合、exceptionSupplierに基づいて例外が投げられます。
     * @throws RuntimeException 与えられた値が負の場合、exceptionSupplierに基づいて投げられます。
     */
    public static <VO extends LongValue<VO>> VO checkNonNegative(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkAtLest(ref, exceptionSupplier, 0L);

    }

    /**
     * 与えられた参照が負であるかを確認し、そうでなければRuntimeExceptionをスローします。
     * <p>
     * 参照がnullの場合、変更せずに返されます。
     *
     * @param ref 確認する参照
     * @param exceptionSupplier 参照が負でない場合にRuntimeExceptionを提供するSupplier
     * @return 参照が負の場合は参照を、nullであればnullを返します
     * @throws RuntimeException 参照が負でない場合
     */
    public static <VO extends LongValue<VO>> VO checkNegative(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkLessThan(ref, exceptionSupplier, 0L);
    }

    /**
     * 与えられた数値が負の数またはゼロであるかを確認します。
     * <p>
     * 参照数がnullの場合、nullを返します。参照数がゼロより大きい場合、指定された例外を投げます。
     *
     * @param ref 検証する数値。
     * @param exceptionSupplier 数値がゼロより大きい場合に投げる例外を提供するSupplier。
     * @return 数値がnullまたはゼロ以下である場合、同じ数値を返します。それ以外の場合は、例外が投げられます。
     * @throws RuntimeException 数値がゼロより大きい場合。
     */
    public static <VO extends LongValue<VO>> VO checkNegativeOrZero(final VO ref,
        final @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkAtMost(ref, exceptionSupplier, 0L);
    }

    /**
     * 指定した最小値と最大値によって定義される閉範囲内に特定のLongが存在するかを検証します。
     *
     * @param ref 検証されるLong値。
     * @param exceptionSupplier Longが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
     * @param min 閉範囲の最小値。
     * @param max 閉範囲の最大値。
     * @return 検証済みのLong値。
     * @throws RuntimeException Long値が指定された閉範囲内にない場合。
     */
    public static <VO extends LongValue<VO>> VO checkRangeClosed(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
        @NonNull Long min, @NonNull Long max) {
      if (isNull(ref)) {
        return null;
      }
      LongVerifiers.verifyRangeClosed(ref.getValue(), exceptionSupplier, min, max);
      return ref;

    }

    /**
     * 特定のLong値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
     *
     * @param ref 検証対象となるLong値。
     * @param exceptionSupplier Long値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
     * このsupplier関数はnullであってはなりません。
     * @param min 範囲の最小値。
     * @param max 範囲の最大値。
     * @return 検証されたLong値。
     * @throws RuntimeException Long値が指定した開放範囲内にない場合。
     */
    public static <VO extends LongValue<VO>> VO checkRangeOpen(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
        @NonNull Long min, @NonNull Long max) {
      if (isNull(ref)) {
        return null;
      }
      LongVerifiers.verifyRangeOpen(ref.getValue(), exceptionSupplier, min, max);
      return ref;

    }

    /**
     * 指定されたLong型の値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを検証します。
     *
     * @param ref 検証するLong型の値。
     * @param exceptionSupplier Long型の値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
     * このサプライヤー関数は非nullでなければなりません。
     * @param min 閉区間-開区間の範囲の最小値。
     * @param max 閉区間-開区間の範囲の最大値。
     * @return 検証済みのLong型の値。
     * @throws RuntimeException Long型の値が指定された閉区間-開区間の範囲内にない場合。
     */
    public static <VO extends LongValue<VO>> VO checkRangeClosedOpen(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Long min,
        @NonNull Long max) {
      if (isNull(ref)) {
        return null;
      }
      LongVerifiers.verifyRangeClosedOpen(ref.getValue(), exceptionSupplier, min, max);
      return ref;

    }

    /**
     * 指定された開放-閉鎖範囲（最小値と最大値によって定義される）内に、与えられたLong値が存在するかどうかを確認します。
     *
     * @param ref 検証するLong値。
     * @param exceptionSupplier Long値が範囲外の場合に投げられるRuntimeExceptionを返すサプライヤー関数。
     * このサプライヤー関数はnullであってはなりません。
     * @param min 開放-閉鎖範囲の最小値。
     * @param max 開放-閉鎖範囲の最大値。
     * @return 検証したLong値。
     * @throws RuntimeException Long値が指定した開放-閉鎖範囲内にない場合。
     */
    public static <VO extends LongValue<VO>> VO checkRangeOpenClosed(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Long min,
        @NonNull Long max) {
      if (isNull(ref)) {
        return null;
      }
      LongVerifiers.verifyRangeOpenClosed(ref.getValue(), exceptionSupplier, min, max);
      return ref;

    }

    /**
     * 指定された整数値が範囲の最小値以上であることを検証します。
     * <p>
     * 値が範囲内にない場合、exceptionSupplierによって提供されるRuntimeExceptionをスローします。
     *
     * @param ref 検証する整数値です。
     * @param exceptionSupplier 値が範囲外の場合にRuntimeExceptionを返す関数です。 この関数は null であってはなりません。
     * @param min 範囲の最小値です。
     * @return 検証された整数値。
     * @throws RuntimeException 整数値が指定された最小値以上でない場合にスローされます。
     */
    public static <VO extends LongValue<VO>> VO checkAtLest(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Long min) {
      if (isNull(ref)) {
        return null;
      }
      LongVerifiers.verifyAtLest(ref.getValue(), exceptionSupplier, min);
      return ref;

    }

    /**
     * 指定されたLong値が、最大値によって定義された範囲内にあるかどうかを検証します。
     *
     * @param ref 検証するLong値。
     * @param exceptionSupplier Long値が範囲外の場合に、RuntimeExceptionを返すサプライヤー関数。
     * @param max 範囲の最大値。
     * @return 検証されたLong値を返します。
     * @throws RuntimeException Long値が指定範囲内に存在しない場合にスローされます。
     */
    public static <VO extends LongValue<VO>> VO checkAtMost(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Long max) {
      if (isNull(ref)) {
        return null;
      }

      LongVerifiers.verifyAtMost(ref.getValue(), exceptionSupplier, max);
      return ref;

    }

    /**
     * 与えられたLongの値が、指定された最大値よりも小さいかどうかを検証します。
     *
     * @param ref 検証対象となるLongの値。
     * @param exceptionSupplier Longの値が範囲内にない場合にスローされるRuntimeExceptionを返すSupplier関数。
     * @param max 範囲の最大値。
     * @return 検証済みのLongの値。
     * @throws RuntimeException Longの値が指定された最大値未満でない場合。
     */
    public static <VO extends LongValue<VO>> VO checkLessThan(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Long max) {
      if (isNull(ref)) {
        return null;
      }
      LongVerifiers.verifyLessThan(ref.getValue(), exceptionSupplier, max);
      return ref;

    }

    /**
     * 指定されたLong値が最小値よりも大きいかどうかを検証します。
     *
     * @param ref 検証するLong値。
     * @param exceptionSupplier 範囲内に値がない場合にスローされるRuntimeExceptionを返す供給関数。
     * @param min 最小値。
     * @return 検証されたLong値。
     * @throws RuntimeException Long値が最小値よりも大きくない場合。
     */
    public static <VO extends LongValue<VO>> VO checkGreaterThan(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Long min) {
      if (ref == null) {
        return ref;
      }

      LongVerifiers.verifyGreaterThan(ref.getValue(), exceptionSupplier, min);
      return ref;

    }

  }
}
