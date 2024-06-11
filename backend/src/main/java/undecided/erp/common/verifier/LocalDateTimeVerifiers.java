package undecided.erp.common.verifier;

import com.google.common.collect.Range;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import lombok.NonNull;

public class LocalDateTimeVerifiers {

  /**
   * 指定した最小値と最大値によって定義される閉範囲内に特定のLocalDateTimeが存在するかを検証します。
   *
   * @param ref 検証されるLocalDateTime値。
   * @param exceptionSupplier LocalDateTimeが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
   * @param min 閉範囲の最小値。
   * @param max 閉範囲の最大値。
   * @return 検証済みのLocalDateTime値。
   * @throws RuntimeException LocalDateTime値が指定された閉範囲内にない場合。
   */
  public static LocalDateTime verifyRangeClosed(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull LocalDateTime min, @NonNull LocalDateTime max) {
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のLocalDateTime値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証対象となるLocalDateTime値。
   * @param exceptionSupplier LocalDateTime値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
   * このsupplier関数はnullであってはなりません。
   * @param min 範囲の最小値。
   * @param max 範囲の最大値。
   * @return 検証されたLocalDateTime値。
   * @throws RuntimeException LocalDateTime値が指定した開放範囲内にない場合。
   */
  public static LocalDateTime verifyRangeOpen(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull LocalDateTime min, @NonNull LocalDateTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のLocalDateTime値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを確認します。
   *
   * @param ref 確認するLocalDateTimeの値。
   * @param exceptionSupplier LocalDateTimeの値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
   * @param min 閉区間の最小値。
   * @param max 開区間の最大値（排他的）。
   * @return 検証済みのLocalDateTimeの値。
   * @throws RuntimeException LocalDateTimeの値が指定された範囲外の場合。
   */
  public static LocalDateTime verifyRangeClosedOpen(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDateTime min,
      @NonNull LocalDateTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDateTime値が指定された開いた範囲（最小値と最大値によって定義）内にあるかどうかを確認します。
   *
   * @param ref 検証するLocalDateTimeの値。
   * @param exceptionSupplier LocalDateTimeの値が範囲外の場合にスローするRuntimeExceptionを返すサプライヤー関数。
   * @param min 閉じた範囲の最小値。
   * @param max 開いた範囲の最大値。
   * @return 検証されたLocalDateTimeの値。
   * @throws RuntimeException LocalDateTimeの値が指定された範囲外の場合。
   */
  public static LocalDateTime verifyRangeOpenClosed(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDateTime min,
      @NonNull LocalDateTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.openClosed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDateTimeが最小値以上であることを検証します。
   *
   * @param ref 検証対象のLocalDateTime値。
   * @param exceptionSupplier LocalDateTime値が範囲外の場合にRuntimeExceptionを返す関数。
   * @param min 範囲の最小値。
   * @return 検証されたLocalDateTime値。
   * @throws RuntimeException LocalDateTime値が最小値以下の場合にスローされます。
   */
  public static LocalDateTime verifyAtLest(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDateTime min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された最大値以下であるかどうか、与えられたLocalDateTime値を検証します。
   *
   * @param ref 検証対象のLocalDateTime値。
   * @param exceptionSupplier LocalDateTime値が範囲外である場合にスローされるRuntimeExceptionを返すSupplier関数。
   * @param max 範囲の最大値。
   * @return 検証済みのLocalDateTime値。
   * @throws RuntimeException LocalDateTime値が指定した最大値以下でない場合。
   */
  public static LocalDateTime verifyAtMost(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDateTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDateTimeが指定された最大値よりも小さいかを検証します。
   *
   * @param ref 検証されるLocalDateTimeの値。
   * @param exceptionSupplier 最大値より大きい場合に投げられるRuntimeExceptionを返すSupplier関数。
   * @param max 最大値。
   * @return 検証されたLocalDateTimeの値。
   * @throws RuntimeException LocalDateTimeの値が指定された最大値よりも大きい場合。
   */
  public static LocalDateTime verifyLessThan(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDateTime max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたLocalDateTimeの値が最小値より大きいかどうかを確認します。
   *
   * @param ref 検証されるLocalDateTimeの値。
   * @param exceptionSupplier
   * LocalDateTimeの値が最小値より大きくない場合にスローされるRuntimeExceptionを返すサプライヤ関数。nullであってはならない。
   * @param min 最小値。
   * @return 検証されたLocalDateTimeの値。
   * @throws RuntimeException LocalDateTimeの値が最小値より大きくない場合。
   */
  public static LocalDateTime verifyGreaterThan(LocalDateTime ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull LocalDateTime min) {
    if (ref == null) {
      return ref;
    }

    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

}
