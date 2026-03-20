package undecided.erp.common.precondition;

import static undecided.shared.common.primitiveOld.Objects2.isNull;

import com.google.common.collect.Range;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * 数値型の検証を行うためのユーティリティクラスです。
 *
 * <p>指定された条件に基づいて、対象の {@link Integer} 値が適切な範囲や条件を 満たしているかを確認します。条件に違反した場合、指定された例外をスローします。
 *
 * <p>このクラスのメソッドは全て静的であり、インスタンス化することはできません。
 */
@UtilityClass
public class IntegerPrecondition {

  /**
   * 指定されたInteger値が正の値であることを確認します。
   *
   * <p>参照値がnullの場合はnullを返し、何も検証しません。 参照値が正の値でない場合は、指定された例外をスローします。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 正の値でない場合にスローするRuntimeExceptionを供給するSupplier。nullは許容されません。
   * @return 正の値であると検証された参照値。参照値がnullであればnullを返します。
   * @throws RuntimeException 参照値が正の値でない場合に、exceptionSupplierによって供給される例外をスローします。
   */
  public static Integer checkPositive(
      @Nullable Integer ref, @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return checkGreaterThan(ref, exceptionSupplier, 0);
  }

  /**
   * 指定されたInteger値が負でない（0以上）かを検証します。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 参照値が負である場合にスローするRuntimeExceptionを供給するSupplier。 nullは許容されません。
   * @return 参照値が0以上である場合はその値を返します。参照値がnullであればnullを返します。
   * @throws RuntimeException 参照値が負である場合に、exceptionSupplierによって供給される例外をスローします。
   */
  public static Integer checkNonNegative(
      @Nullable Integer ref, @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return checkAtLeast(ref, exceptionSupplier, 0);
  }

  /**
   * 指定されたInteger値が負であるかを検証します。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 値が負でない場合にスローされるRuntimeExceptionを供給するSupplier。nullは許容されません。
   * @return 負であると検証された参照値。参照値がnullであればnullを返します。
   * @throws RuntimeException 参照値が負でない場合に、exceptionSupplierによって供給される例外をスローします。
   */
  public static Integer checkNegative(
      @Nullable Integer ref, @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return checkLessThan(ref, exceptionSupplier, 0);
  }

  /**
   * 指定されたInteger値が0以下であるかを検証します。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 参照値が0以下でない場合にスローされるRuntimeExceptionを供給するSupplier。 nullは許容されません。
   * @return 参照値が0以下である場合はその値を返します。参照値がnullであればnullを返します。
   * @throws RuntimeException 参照値が0より大きい場合に、exceptionSupplierによって供給される例外をスローします。
   */
  public static Integer checkNegativeOrZero(
      @Nullable Integer ref, @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
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
  public static Integer checkRangeClosed(
      @Nullable Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer min,
      @NonNull Integer max) {
    if (isNull(ref)) return null;
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたIntegerの値が開区間(min, max)内にあるかを確認します。 参照値がnullの場合、そのままnullを返します。
   * 参照値が範囲外にある場合は、指定された例外をスローします。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 範囲外の場合にスローするRuntimeExceptionを供給するSupplier。nullは許容されません。
   * @param min 開区間の下限値。nullは許容されません。
   * @param max 開区間の上限値。nullは許容されません。
   * @return 開区間内であると検証されたInteger値。参照値がnullであればnullを返します。
   * @throws RuntimeException 指定された範囲に値が存在しない場合、exceptionSupplierによって供給される例外をスローします。
   */
  public static Integer checkRangeOpen(
      @Nullable Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer min,
      @NonNull Integer max) {
    if (isNull(ref)) return null;
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたInteger型の値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するInteger型の値。
   * @param exceptionSupplier Integer型の値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
   *     このサプライヤー関数は非nullでなければなりません。
   * @param min 閉区間-開区間の範囲の最小値。
   * @param max 閉区間-開区間の範囲の最大値。
   * @return 検証済みのInteger型の値。
   * @throws RuntimeException Integer型の値が指定された閉区間-開区間の範囲内にない場合。
   */
  public static Integer checkRangeClosedOpen(
      Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer min,
      @NonNull Integer max) {
    if (isNull(ref)) return null;
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたInteger値が、指定された最小値 (排他) と最大値 (包括) の範囲内に収まるかどうかを検証します。
   *
   * <p>参照値がnullの場合は、検証をスキップしnullを返します。 範囲外の場合は例外をスローします。
   *
   * @param ref 検証するInteger値。nullを許容します。
   * @param exceptionSupplier Integer値が範囲外だった場合にスローするRuntimeExceptionを供給するSupplier。 nullは許容されません。
   * @param min 範囲の最小値（排他）。nullは許容されません。
   * @param max 範囲の最大値（包括）。nullは許容されません。
   * @return 範囲内であると検証されたInteger値。また、null値の場合もそのまま返します。
   * @throws RuntimeException Integer値が範囲外の場合にexceptionSupplierによって供給される例外をスローします。
   */
  public static Integer checkRangeOpenClosed(
      @Nullable Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer min,
      @NonNull Integer max) {
    if (isNull(ref)) return null;
    if (!Range.openClosed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された参照値が指定された最小値以上であることを確認します。 参照値がnullの場合はnullを返し、何も検証しません。 参照値が最小値未満である場合は、指定された例外をスローします。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 最小値以上でない場合にスローするRuntimeExceptionを提供するSupplier。nullは許容されません。
   * @param min 許容される最小値。nullは許容されません。
   * @return 指定された最小値以上である参照値。参照値がnullであればnullを返します。
   * @throws RuntimeException 参照値が指定された最小値未満の場合にスローされます。
   */
  public static Integer checkAtLeast(
      @Nullable Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer min) {
    if (isNull(ref)) return null;
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された参照値が、指定された最大値以下であるかを検証します。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 最大値以下でない場合にスローするRuntimeExceptionを供給するSupplier。nullは許容されません。
   * @param max 許容される最大値。nullは許容されません。
   * @return 最大値以下であると検証された参照値。参照値がnullであればnullを返します。
   * @throws RuntimeException 参照値が指定された最大値よりも大きい場合にスローされます。
   */
  public static Integer checkAtMost(
      @Nullable Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer max) {
    if (isNull(ref)) return null;
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

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
  public static Integer checkLessThan(
      @Nullable Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer max) {
    if (isNull(ref)) return null;
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された参照値が指定された最小値より大きいことを確認します。
   *
   * <p>参照値がnullの場合はnullを返し、何も検証しません。 参照値が最小値以下である場合は、指定された例外をスローします。
   *
   * @param ref 検証対象のInteger値。nullを許容します。
   * @param exceptionSupplier 最小値より大きくない場合にスローするRuntimeExceptionを提供するSupplier。nullは許容されません。
   * @param min 許容される最小値。nullは許容されません。
   * @return 指定された最小値より大きい参照値。参照値がnullであればnullを返します。
   * @throws RuntimeException 参照値が指定された最小値以下の場合にスローされます。
   */
  public static Integer checkGreaterThan(
      @Nullable Integer ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Integer min) {
    if (isNull(ref)) return null;
    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }
}
