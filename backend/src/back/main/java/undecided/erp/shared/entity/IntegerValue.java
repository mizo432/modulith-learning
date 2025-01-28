package undecided.erp.shared.entity;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.util.function.Supplier;
import lombok.NonNull;
import undecided.erp.common.precondition.IntegerPrecondition;


/**
 * IntegerValueクラスは、型Integerの単一の値をカプセル化する値オブジェクトを表します。
 * このクラスは、Integer型でパラメータ化されたSingleValueクラスの機能を拡張し、 整数値を処理するための特定の実装を提供します。
 * <p>
 * このクラスは、型付けが強い整数値の値オブジェクトとしての表現が必要な状況で使用されることを意図しています。
 *
 * @param <VO> 値オブジェクトのタイプを表します。
 */
public interface IntegerValue<VO extends IntegerValue<VO>> extends SingleValue<Integer> {

  class IntegerValues {

    /**
     * 与えられた引用が正の整数であるかを検証し、そうでない場合はカスタム例外をスローします。
     *
     * @param ref 正のものであるかどうかを検証するための参照。null値も許可されます。
     * @param exceptionSupplier 参照が正でない場合にスローされるカスタム例外を提供する{@code Supplier}。
     * @return 参照がnullでなく、正の場合は正の参照。
     * @throws RuntimeException 参照がnullまたは正でない場合。
     */
    public static <VO extends IntegerValue<VO>> VO checkPositive(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkGreaterThan(ref, exceptionSupplier, 0);
    }

    /**
     * 与えられたIntegerの値が正またはゼロであることを検証します。
     *
     * @param ref 検証するIntegerの値。 nullの場合、メソッドは例外を投げずにnullを返します。
     * @param exceptionSupplier 与えられた値が負である場合に投げられる適切なRuntimeExceptionを提供するSupplier。
     * @return 与えられた値が正またはゼロである場合、同じIntegerの値が返ります。それ以外の場合、exceptionSupplierに基づいて例外が投げられます。
     * @throws RuntimeException 与えられた値が負の場合、exceptionSupplierに基づいて投げられます。
     */
    public static <VO extends IntegerValue<VO>> VO checkNonNegative(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkAtLest(ref, exceptionSupplier, 0);

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
    public static <VO extends IntegerValue<VO>> VO checkNegative(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkLessThan(ref, exceptionSupplier, 0);
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
    public static <VO extends IntegerValue<VO>> VO checkNegativeOrZero(final VO ref,
        final @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
      return checkAtMost(ref, exceptionSupplier, 0);
    }

    /**
     * 指定した最小値と最大値によって定義される閉範囲内に特定のIntegerが存在するかを検証します。
     *
     * @param ref 検証されるInteger値。
     * @param exceptionSupplier Integerが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
     * @param min 閉範囲の最小値。
     * @param max 閉範囲の最大値。
     * @return 検証済みのInteger値。
     * @throws RuntimeException Integer値が指定された閉範囲内にない場合。
     */
    public static <VO extends IntegerValue<VO>> VO checkRangeClosed(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
        @NonNull Integer min, @NonNull Integer max) {
      if (isNull(ref)) {
        return null;
      }
      IntegerPrecondition.checkRangeClosed(ref.getValue(), exceptionSupplier, min, max);
      return ref;

    }

    /**
     * 特定のInteger値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
     *
     * @param ref 検証対象となるInteger値。
     * @param exceptionSupplier Integer値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
     * このsupplier関数はnullであってはなりません。
     * @param min 範囲の最小値。
     * @param max 範囲の最大値。
     * @return 検証されたInteger値。
     * @throws RuntimeException Integer値が指定した開放範囲内にない場合。
     */
    public static <VO extends IntegerValue<VO>> VO checkRangeOpen(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
        @NonNull Integer min, @NonNull Integer max) {
      if (isNull(ref)) {
        return null;
      }
      IntegerPrecondition.checkRangeOpen(ref.getValue(), exceptionSupplier, min, max);
      return ref;

    }

    /**
     * 指定されたInteger型の値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを検証します。
     *
     * @param ref 検証するInteger型の値。
     * @param exceptionSupplier Integer型の値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
     * このサプライヤー関数は非nullでなければなりません。
     * @param min 閉区間-開区間の範囲の最小値。
     * @param max 閉区間-開区間の範囲の最大値。
     * @return 検証済みのInteger型の値。
     * @throws RuntimeException Integer型の値が指定された閉区間-開区間の範囲内にない場合。
     */
    public static <VO extends IntegerValue<VO>> VO checkRangeClosedOpen(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Integer min,
        @NonNull Integer max) {
      if (isNull(ref)) {
        return null;
      }
      IntegerPrecondition.checkRangeClosedOpen(ref.getValue(), exceptionSupplier, min, max);
      return ref;

    }

    /**
     * 指定された開放-閉鎖範囲（最小値と最大値によって定義される）内に、与えられたInteger値が存在するかどうかを確認します。
     *
     * @param ref 検証するInteger値。
     * @param exceptionSupplier Integer値が範囲外の場合に投げられるRuntimeExceptionを返すサプライヤー関数。
     * このサプライヤー関数はnullであってはなりません。
     * @param min 開放-閉鎖範囲の最小値。
     * @param max 開放-閉鎖範囲の最大値。
     * @return 検証したInteger値。
     * @throws RuntimeException Integer値が指定した開放-閉鎖範囲内にない場合。
     */
    public static <VO extends IntegerValue<VO>> VO checkRangeOpenClosed(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Integer min,
        @NonNull Integer max) {
      if (isNull(ref)) {
        return null;
      }
      IntegerPrecondition.checkRangeOpenClosed(ref.getValue(), exceptionSupplier, min, max);
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
    public static <VO extends IntegerValue<VO>> VO checkAtLest(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Integer min) {
      if (isNull(ref)) {
        return null;
      }
      IntegerPrecondition.checkAtLest(ref.getValue(), exceptionSupplier, min);
      return ref;

    }

    /**
     * 指定されたInteger値が、最大値によって定義された範囲内にあるかどうかを検証します。
     *
     * @param ref 検証するInteger値。
     * @param exceptionSupplier Integer値が範囲外の場合に、RuntimeExceptionを返すサプライヤー関数。
     * @param max 範囲の最大値。
     * @return 検証されたInteger値を返します。
     * @throws RuntimeException Integer値が指定範囲内に存在しない場合にスローされます。
     */
    public static <VO extends IntegerValue<VO>> VO checkAtMost(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Integer max) {
      if (isNull(ref)) {
        return null;
      }

      IntegerPrecondition.checkAtMost(ref.getValue(), exceptionSupplier, max);
      return ref;

    }

    /**
     * 与えられたIntegerの値が、指定された最大値よりも小さいかどうかを検証します。
     *
     * @param ref 検証対象となるIntegerの値。
     * @param exceptionSupplier Integerの値が範囲内にない場合にスローされるRuntimeExceptionを返すSupplier関数。
     * @param max 範囲の最大値。
     * @return 検証済みのIntegerの値。
     * @throws RuntimeException Integerの値が指定された最大値未満でない場合。
     */
    public static <VO extends IntegerValue<VO>> VO checkLessThan(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Integer max) {
      if (isNull(ref)) {
        return null;
      }
      IntegerPrecondition.checkLessThan(ref.getValue(), exceptionSupplier, max);
      return ref;

    }

    /**
     * 指定されたInteger値が最小値よりも大きいかどうかを検証します。
     *
     * @param ref 検証するInteger値。
     * @param exceptionSupplier 範囲内に値がない場合にスローされるRuntimeExceptionを返す供給関数。
     * @param min 最小値。
     * @return 検証されたInteger値。
     * @throws RuntimeException Integer値が最小値よりも大きくない場合。
     */
    public static <VO extends IntegerValue<VO>> VO checkGreaterThan(VO ref,
        @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Integer min) {
      if (ref == null) {
        return ref;
      }

      IntegerPrecondition.checkGreaterThan(ref.getValue(), exceptionSupplier, min);
      return ref;

    }

  }
}
