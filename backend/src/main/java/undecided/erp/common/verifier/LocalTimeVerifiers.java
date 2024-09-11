package undecided.erp.common.verifier;

import com.google.common.collect.Range;
import java.time.LocalTime;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * LocalTimeの値が指定した範囲内にあるかどうかを検証するメソッドを含むユーティリティクラス。
 */
@UtilityClass
public class LocalTimeVerifiers {

  /**
   * 指定した最小値と最大値によって定義される閉範囲内に特定のLocalTimeが存在するかを検証します。
   *
   * @param ref 検証されるLocalTime値。
   * @param exceptionSupplier LocalTimeが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
   * @param min 閉範囲の最小値。
   * @param max 閉範囲の最大値。
   * @return 検証済みのLocalTime値。
   * @throws RuntimeException LocalTime値が指定された閉範囲内にない場合。
   */
  public static LocalTime verifyRangeClosed(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull LocalTime min, @NonNull LocalTime max) {
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のLocalTime値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証対象となるLocalTime値。
   * @param exceptionSupplier LocalTime値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
   * このsupplier関数はnullであってはなりません。
   * @param min 範囲の最小値。
   * @param max 範囲の最大値。
   * @return 検証されたLocalTime値。
   * @throws RuntimeException LocalTime値が指定した開放範囲内にない場合。
   */
  public static LocalTime verifyRangeOpen(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull LocalTime min, @NonNull LocalTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のLocalTime値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを確認します。
   *
   * @param ref 確認するLocalTimeの値。
   * @param exceptionSupplier LocalTimeの値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
   * @param min 閉区間の最小値。
   * @param max 開区間の最大値（排他的）。
   * @return 検証済みのLocalTimeの値。
   * @throws RuntimeException LocalTimeの値が指定された範囲外の場合。
   */
  public static LocalTime verifyRangeClosedOpen(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalTime min,
      @NonNull LocalTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalTime値が指定された開いた範囲（最小値と最大値によって定義）内にあるかどうかを確認します。
   *
   * @param ref 検証するLocalTimeの値。
   * @param exceptionSupplier LocalTimeの値が範囲外の場合にスローするRuntimeExceptionを返すサプライヤー関数。
   * @param min 閉じた範囲の最小値。
   * @param max 開いた範囲の最大値。
   * @return 検証されたLocalTimeの値。
   * @throws RuntimeException LocalTimeの値が指定された範囲外の場合。
   */
  public static LocalTime verifyRangeOpenClosed(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalTime min,
      @NonNull LocalTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.openClosed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalTimeが最小値以上であることを検証します。
   *
   * @param ref 検証対象のLocalTime値。
   * @param exceptionSupplier LocalTime値が範囲外の場合にRuntimeExceptionを返す関数。
   * @param min 範囲の最小値。
   * @return 検証されたLocalTime値。
   * @throws RuntimeException LocalTime値が最小値以下の場合にスローされます。
   */
  public static LocalTime verifyAtLest(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalTime min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された最大値以下であるかどうか、与えられたLocalTime値を検証します。
   *
   * @param ref 検証対象のLocalTime値。
   * @param exceptionSupplier LocalTime値が範囲外である場合にスローされるRuntimeExceptionを返すSupplier関数。
   * @param max 範囲の最大値。
   * @return 検証済みのLocalTime値。
   * @throws RuntimeException LocalTime値が指定した最大値以下でない場合。
   */
  public static LocalTime verifyAtMost(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalTimeが指定された最大値よりも小さいかを検証します。
   *
   * @param ref 検証されるLocalTimeの値。
   * @param exceptionSupplier 最大値より大きい場合に投げられるRuntimeExceptionを返すSupplier関数。
   * @param max 最大値。
   * @return 検証されたLocalTimeの値。
   * @throws RuntimeException LocalTimeの値が指定された最大値よりも大きい場合。
   */
  public static LocalTime verifyLessThan(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalTimeの値が最小値より大きいかどうかを確認します。
   *
   * @param ref 検証されるLocalTimeの値。
   * @param exceptionSupplier
   * LocalTimeの値が最小値より大きくない場合にスローされるRuntimeExceptionを返すサプライヤ関数。nullであってはならない。
   * @param min 最小値。
   * @return 検証されたLocalTimeの値。
   * @throws RuntimeException LocalTimeの値が最小値より大きくない場合。
   */
  public static LocalTime verifyGreaterThan(LocalTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalTime min) {
    if (ref == null) {
      return ref;
    }

    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

}
