package undecided.erp.common.verifier;

import com.google.common.collect.Range;
import java.time.LocalDate;
import java.util.function.Supplier;
import lombok.NonNull;

public class LocalDateVerifiers {

  /**
   * 指定した最小値と最大値によって定義される閉範囲内に特定のLocalDateが存在するかを検証します。
   *
   * @param ref 検証されるLocalDate値。
   * @param exceptionSupplier LocalDateが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
   * @param min 閉範囲の最小値。
   * @param max 閉範囲の最大値。
   * @return 検証済みのLocalDate値。
   * @throws RuntimeException LocalDate値が指定された閉範囲内にない場合。
   */
  public static LocalDate verifyRangeClosed(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull LocalDate min, @NonNull LocalDate max) {
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のLocalDate値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証対象となるLocalDate値。
   * @param exceptionSupplier LocalDate値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
   * このsupplier関数はnullであってはなりません。
   * @param min 範囲の最小値。
   * @param max 範囲の最大値。
   * @return 検証されたLocalDate値。
   * @throws RuntimeException LocalDate値が指定した開放範囲内にない場合。
   */
  public static LocalDate verifyRangeOpen(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull LocalDate min, @NonNull LocalDate max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたLocalDate値が最小値と最大値によって定義された閉開範囲内にあるかどうかを確認します。
   *
   * @param ref 検証対象のLocalDate値。
   * @param exceptionSupplier LocalDate値が範囲外である場合にスローするRuntimeExceptionを返す供給関数。この関数はnullであってはなりません。
   * @param min 閉開範囲の最小値。
   * @param max 閉開範囲の最大値。
   * @return 検証済みのLocalDate値。
   * @throws RuntimeException LocalDate値が指定された閉開範囲内にない場合。
   */
  public static LocalDate verifyRangeClosedOpen(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDate min,
      @NonNull LocalDate max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDate値が指定された開いた範囲（最小値と最大値によって定義）内にあるかどうかを確認します。
   *
   * @param ref 検証するLocalDateの値。
   * @param exceptionSupplier LocalDateの値が範囲外の場合にスローするRuntimeExceptionを返すサプライヤー関数。
   * @param min 閉じた範囲の最小値。
   * @param max 開いた範囲の最大値。
   * @return 検証されたLocalDateの値。
   * @throws RuntimeException LocalDateの値が指定された範囲外の場合。
   */
  public static LocalDate verifyRangeOpenClosed(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDate min,
      @NonNull LocalDate max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.openClosed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDateが最小値以上であることを検証します。
   *
   * @param ref 検証対象のLocalDate値。
   * @param exceptionSupplier LocalDate値が範囲外の場合にRuntimeExceptionを返す関数。
   * @param min 範囲の最小値。
   * @return 検証されたLocalDate値。
   * @throws RuntimeException LocalDate値が最小値以下の場合にスローされます。
   */
  public static LocalDate verifyAtLest(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDate min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された最大値以下であるかどうか、与えられたLocalDate値を検証します。
   *
   * @param ref 検証対象のLocalDate値。
   * @param exceptionSupplier LocalDate値が範囲外である場合にスローされるRuntimeExceptionを返すSupplier関数。
   * @param max 範囲の最大値。
   * @return 検証済みのLocalDate値。
   * @throws RuntimeException LocalDate値が指定した最大値以下でない場合。
   */
  public static LocalDate verifyAtMost(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDate max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDateが指定された最大値よりも小さいかを検証します。
   *
   * @param ref 検証されるLocalDateの値。
   * @param exceptionSupplier 最大値より大きい場合に投げられるRuntimeExceptionを返すSupplier関数。
   * @param max 最大値。
   * @return 検証されたLocalDateの値。
   * @throws RuntimeException LocalDateの値が指定された最大値よりも大きい場合。
   */
  public static LocalDate verifyLessThan(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDate max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDateの値が最小値より大きいかどうかを確認します。
   *
   * @param ref 検証されるLocalDateの値。
   * @param exceptionSupplier
   * LocalDateの値が最小値より大きくない場合にスローされるRuntimeExceptionを返すサプライヤ関数。nullであってはならない。
   * @param min 最小値。
   * @return 検証されたLocalDateの値。
   * @throws RuntimeException LocalDateの値が最小値より大きくない場合。
   */
  public static LocalDate verifyGreaterThan(LocalDate ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDate min) {
    if (ref == null) {
      return ref;
    }

    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

}
