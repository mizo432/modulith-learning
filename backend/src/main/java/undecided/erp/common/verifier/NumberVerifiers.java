package undecided.erp.common.verifier;

import com.google.common.collect.Range;
import java.util.function.Supplier;
import lombok.NonNull;

public class NumberVerifiers {

  /**
   * 指定した最小値と最大値によって定義される閉範囲内に特定のIntegerが存在するかを検証します。
   *
   * @param ref 検証されるInteger値。
   * @param min 閉範囲の最小値。
   * @param max 閉範囲の最大値。
   * @param exceptionSupplier Integerが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
   * @return 検証済みのInteger値。
   * @throws RuntimeException Integer値が指定された閉範囲内にない場合。
   */
  public static Integer verifyRangeClosed(Integer ref, @NonNull Integer min, @NonNull Integer max,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のInteger値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証対象となるInteger値。
   * @param min 範囲の最小値。
   * @param max 範囲の最大値。
   * @param exceptionSupplier Integer値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
   * このsupplier関数はnullであってはなりません。
   * @return 検証されたInteger値。
   * @throws RuntimeException Integer値が指定した開放範囲内にない場合。
   */
  public static Integer verifyRangeOpen(Integer ref, @NonNull Integer min, @NonNull Integer max,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたInteger型の値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するInteger型の値。
   * @param min 閉区間-開区間の範囲の最小値。
   * @param max 閉区間-開区間の範囲の最大値。
   * @param exceptionSupplier Integer型の値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数は非nullでなければなりません。
   * @return 検証済みのInteger型の値。
   * @throws RuntimeException Integer型の値が指定された閉区間-開区間の範囲内にない場合。
   */
  public static Integer verifyRangeClosedOpen(Integer ref, @NonNull Integer min,
      @NonNull Integer max,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された開放-閉鎖範囲（最小値と最大値によって定義される）内に、与えられたInteger値が存在するかどうかを確認します。
   *
   * @param ref 検証するInteger値。
   * @param min 開放-閉鎖範囲の最小値。
   * @param max 開放-閉鎖範囲の最大値。
   * @param exceptionSupplier Integer値が範囲外の場合に投げられるRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数はnullであってはなりません。
   * @return 検証したInteger値。
   * @throws RuntimeException Integer値が指定した開放-閉鎖範囲内にない場合。
   */
  public static Integer verifyRangeOpenClosed(Integer ref, @NonNull Integer min,
      @NonNull Integer max,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.openClosed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された整数値が範囲の最小値以上であることを検証します。
   * <p>
   * 値が範囲内にない場合、exceptionSupplierによって提供されるRuntimeExceptionをスローします。
   *
   * @param ref 検証する整数値です。
   * @param min 範囲の最小値です。
   * @param exceptionSupplier 値が範囲外の場合にRuntimeExceptionを返す関数です。 この関数は null であってはなりません。
   * @return 検証された整数値。
   * @throws RuntimeException 整数値が指定された最小値以上でない場合にスローされます。
   */
  public static Integer verifyRangeAtLest(Integer ref, @NonNull Integer min,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたInteger値が、最大値によって定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するInteger値。
   * @param max 範囲の最大値。
   * @param exceptionSupplier Integer値が範囲外の場合に、RuntimeExceptionを返すサプライヤー関数。
   * @return 検証されたInteger値を返します。
   * @throws RuntimeException Integer値が指定範囲内に存在しない場合にスローされます。
   */
  public static Integer verifyRangeAtMost(Integer ref, @NonNull Integer max,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたIntegerの値が、指定された最大値よりも小さいかどうかを検証します。
   *
   * @param ref 検証対象となるIntegerの値。
   * @param max 範囲の最大値。
   * @param exceptionSupplier Integerの値が範囲内にない場合にスローされるRuntimeExceptionを返すSupplier関数。
   * @return 検証済みのIntegerの値。
   * @throws RuntimeException Integerの値が指定された最大値未満でない場合。
   */
  public static Integer verifyRangeLessThan(Integer ref, @NonNull Integer max,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたInteger値が最小値よりも大きいかどうかを検証します。
   *
   * @param ref 検証するInteger値。
   * @param min 最小値。
   * @param exceptionSupplier 範囲内に値がない場合にスローされるRuntimeExceptionを返す供給関数。
   * @return 検証されたInteger値。
   * @throws RuntimeException Integer値が最小値よりも大きくない場合。
   */
  public static Integer verifyRangeGreaterThan(Integer ref, @NonNull Integer min,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

}
