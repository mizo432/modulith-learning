package undecided.erp.common.verifier;

import com.google.common.collect.Range;
import java.math.BigDecimal;
import java.util.function.Supplier;
import lombok.NonNull;

public class BigDecimalVerifiers {

  /**
   * 指定されたBigDecimal値が正数であることを検証します。値がnullまたは負である場合、 提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。
   * @param exceptionSupplier 値が正数でないときにスローされる例外を提供する供給装置。
   * @return 検証に合格した場合は、正数のBigDecimal値。
   * @throws RuntimeException 値がnullまたは負であるときにスローされます。
   */
  public static BigDecimal verifyPositive(final BigDecimal ref,
      final @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyGreaterThan(ref, exceptionSupplier, BigDecimal.ZERO);

  }

  /**
   * BigDecimal値が正またはゼロであることを確認します。
   *
   * @param ref 確認するBigDecimal値。
   * @param exceptionSupplier 値が正またはゼロでない場合にスローされる例外を提供するサプライヤー。
   * @return 値が正またはゼロである場合、元のBigDecimal値。
   * @throws RuntimeException 値が正またはゼロでない場合。
   */
  public static BigDecimal verifyPositiveOrZero(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyAtLest(ref, exceptionSupplier, BigDecimal.ZERO);
  }

  /**
   * 与えられたBigDecimalが負数であるかどうかを検証します。
   *
   * @param ref 検証されるBigDecimal
   * @param exceptionSupplier BigDecimalが負数でない場合にスローされる例外の提供者
   * @return BigDecimalが負の場合は検証されたBigDecimalを返し、そうでない場合は与えられた参照BigDecimalを返します
   * @throws RuntimeException BigDecimalが負数でない場合
   */
  public static BigDecimal verifyNegative(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyLessThan(ref, exceptionSupplier, BigDecimal.ZERO);
  }

  /**
   * このメソッドは、指定された BigDecimal 値が負数またはゼロであるかどうかを確認します。
   *
   * @param ref 確認される BigDecimal 値。 null であることもあります。
   * @param exceptionSupplier 値が負数やゼロではない場合にスローされる RuntimeException を提供する supplier。
   * @return 入力値が null の場合は null、それ以外の場合は負数またはゼロの BigDecimal 値を返します。
   * @throws RuntimeException 値が負数またはゼロでない場合にスローされます。
   */
  public static BigDecimal verifyNegativeOrZero(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyAtMost(ref, exceptionSupplier, BigDecimal.ZERO);
  }

  /**
   * 指定した最小値と最大値によって定義される閉範囲内に特定のBigDecimalが存在するかを検証します。
   *
   * @param ref 検証されるBigDecimal値。
   * @param exceptionSupplier BigDecimalが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
   * @param min 閉範囲の最小値。
   * @param max 閉範囲の最大値。
   * @return 検証済みのBigDecimal値。
   * @throws RuntimeException BigDecimal値が指定された閉範囲内にない場合。
   */
  public static BigDecimal verifyRangeClosed(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal min, @NonNull BigDecimal max) {
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のBigDecimal値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証対象となるBigDecimal値。
   * @param exceptionSupplier BigDecimal値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
   * このsupplier関数はnullであってはなりません。
   * @param min 範囲の最小値。
   * @param max 範囲の最大値。
   * @return 検証されたBigDecimal値。
   * @throws RuntimeException BigDecimal値が指定した開放範囲内にない場合。
   */
  public static BigDecimal verifyRangeOpen(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal min, @NonNull BigDecimal max) {
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたBigDecimal型の値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するBigDecimal型の値。
   * @param exceptionSupplier BigDecimal型の値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数は非nullでなければなりません。
   * @param min 閉区間-開区間の範囲の最小値。
   * @param max 閉区間-開区間の範囲の最大値。
   * @return 検証済みのBigDecimal型の値。
   * @throws RuntimeException BigDecimal型の値が指定された閉区間-開区間の範囲内にない場合。
   */
  public static BigDecimal verifyRangeClosedOpen(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigDecimal min,
      @NonNull BigDecimal max) {
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された開放-閉鎖範囲（最小値と最大値によって定義される）内に、与えられたBigDecimal値が存在するかどうかを確認します。
   *
   * @param ref 検証するBigDecimal値。
   * @param exceptionSupplier BigDecimal値が範囲外の場合に投げられるRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数はnullであってはなりません。
   * @param min 開放-閉鎖範囲の最小値。
   * @param max 開放-閉鎖範囲の最大値。
   * @return 検証したBigDecimal値。
   * @throws RuntimeException BigDecimal値が指定した開放-閉鎖範囲内にない場合。
   */
  public static BigDecimal verifyRangeOpenClosed(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigDecimal min,
      @NonNull BigDecimal max) {
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
   * @param exceptionSupplier 値が範囲外の場合にRuntimeExceptionを返す関数です。 この関数は null であってはなりません。
   * @param min 範囲の最小値です。
   * @return 検証された整数値。
   * @throws RuntimeException 整数値が指定された最小値以上でない場合にスローされます。
   */
  public static BigDecimal verifyAtLest(final BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      final @NonNull BigDecimal min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたBigDecimal値が、最大値によって定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するBigDecimal値。
   * @param exceptionSupplier BigDecimal値が範囲外の場合に、RuntimeExceptionを返すサプライヤー関数。
   * @param max 範囲の最大値。
   * @return 検証されたBigDecimal値を返します。
   * @throws RuntimeException BigDecimal値が指定範囲内に存在しない場合にスローされます。
   */
  public static BigDecimal verifyAtMost(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigDecimal max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたBigDecimalの値が、指定された最大値よりも小さいかどうかを検証します。
   *
   * @param ref 検証対象となるBigDecimalの値。
   * @param exceptionSupplier BigDecimalの値が範囲内にない場合にスローされるRuntimeExceptionを返すSupplier関数。
   * @param max 範囲の最大値。
   * @return 検証済みのBigDecimalの値。
   * @throws RuntimeException BigDecimalの値が指定された最大値未満でない場合。
   */
  public static BigDecimal verifyLessThan(BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigDecimal max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたBigDecimal値が最小値よりも大きいかどうかを検証します。
   *
   * @param ref 検証するBigDecimal値。
   * @param exceptionSupplier 範囲内に値がない場合にスローされるRuntimeExceptionを返す供給関数。
   * @param min 最小値。
   * @return 検証されたBigDecimal値。
   * @throws RuntimeException BigDecimal値が最小値よりも大きくない場合。
   */
  public static BigDecimal verifyGreaterThan(final BigDecimal ref,
      final @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      final @NonNull BigDecimal min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

}
