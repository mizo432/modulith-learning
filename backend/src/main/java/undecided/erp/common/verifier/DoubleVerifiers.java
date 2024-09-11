package undecided.erp.common.verifier;

import com.google.common.collect.Range;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DoubleVerifiers {

  /**
   * 与えられた引用が正の整数であるかを検証し、そうでない場合はカスタム例外をスローします。
   *
   * @param ref 正のものであるかどうかを検証するための参照。null値も許可されます。
   * @param exceptionSupplier 参照が正でない場合にスローされるカスタム例外を提供する{@code Supplier}。
   * @return 参照がnullでなく、正の場合は正の参照。
   * @throws RuntimeException 参照がnullまたは正でない場合。
   */
  public static Double verifyPositive(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyGreaterThan(ref, exceptionSupplier, (double) 0);
  }

  /**
   * 与えられたDoubleの値が正またはゼロであることを検証します。
   *
   * @param ref 検証するDoubleの値。 nullの場合、メソッドは例外を投げずにnullを返します。
   * @param exceptionSupplier 与えられた値が負である場合に投げられる適切なRuntimeExceptionを提供するSupplier。
   * @return 与えられた値が正またはゼロである場合、同じDoubleの値が返ります。それ以外の場合、exceptionSupplierに基づいて例外が投げられます。
   * @throws RuntimeException 与えられた値が負の場合、exceptionSupplierに基づいて投げられます。
   */
  public static Double verifyPositiveOrZero(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyAtLest(ref, exceptionSupplier, (double) 0);
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
  public static Double verifyNegative(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyLessThan(ref, exceptionSupplier, (double) 0);
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
  public static Double verifyNegativeOrZero(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    return verifyAtMost(ref, exceptionSupplier, (double) 0);

  }

  /**
   * 指定した最小値と最大値によって定義される閉範囲内に特定のDoubleが存在するかを検証します。
   *
   * @param ref 検証されるDouble値。
   * @param exceptionSupplier Doubleが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
   * @param min 閉範囲の最小値。
   * @param max 閉範囲の最大値。
   * @return 検証済みのDouble値。
   * @throws RuntimeException Double値が指定された閉範囲内にない場合。
   */
  public static Double verifyRangeClosed(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Double min, @NonNull Double max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のDouble値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証対象となるDouble値。
   * @param exceptionSupplier Double値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
   * このsupplier関数はnullであってはなりません。
   * @param min 範囲の最小値。
   * @param max 範囲の最大値。
   * @return 検証されたDouble値。
   * @throws RuntimeException Double値が指定した開放範囲内にない場合。
   */
  public static Double verifyRangeOpen(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull Double min, @NonNull Double max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたDouble型の値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するDouble型の値。
   * @param exceptionSupplier Double型の値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数は非nullでなければなりません。
   * @param min 閉区間-開区間の範囲の最小値。
   * @param max 閉区間-開区間の範囲の最大値。
   * @return 検証済みのDouble型の値。
   * @throws RuntimeException Double型の値が指定された閉区間-開区間の範囲内にない場合。
   */
  public static Double verifyRangeClosedOpen(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Double min,
      @NonNull Double max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された開放-閉鎖範囲（最小値と最大値によって定義される）内に、与えられたDouble値が存在するかどうかを確認します。
   *
   * @param ref 検証するDouble値。
   * @param exceptionSupplier Double値が範囲外の場合に投げられるRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数はnullであってはなりません。
   * @param min 開放-閉鎖範囲の最小値。
   * @param max 開放-閉鎖範囲の最大値。
   * @return 検証したDouble値。
   * @throws RuntimeException Double値が指定した開放-閉鎖範囲内にない場合。
   */
  public static Double verifyRangeOpenClosed(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Double min,
      @NonNull Double max) {
    if (ref == null) {
      return ref;
    }
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
  public static Double verifyAtLest(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Double min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたDouble値が、最大値によって定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するDouble値。
   * @param exceptionSupplier Double値が範囲外の場合に、RuntimeExceptionを返すサプライヤー関数。
   * @param max 範囲の最大値。
   * @return 検証されたDouble値を返します。
   * @throws RuntimeException Double値が指定範囲内に存在しない場合にスローされます。
   */
  public static Double verifyAtMost(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Double max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたDoubleの値が、指定された最大値よりも小さいかどうかを検証します。
   *
   * @param ref 検証対象となるDoubleの値。
   * @param exceptionSupplier Doubleの値が範囲内にない場合にスローされるRuntimeExceptionを返すSupplier関数。
   * @param max 範囲の最大値。
   * @return 検証済みのDoubleの値。
   * @throws RuntimeException Doubleの値が指定された最大値未満でない場合。
   */
  public static Double verifyLessThan(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Double max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたDouble値が最小値よりも大きいかどうかを検証します。
   *
   * @param ref 検証するDouble値。
   * @param exceptionSupplier 範囲内に値がない場合にスローされるRuntimeExceptionを返す供給関数。
   * @param min 最小値。
   * @return 検証されたDouble値。
   * @throws RuntimeException Double値が最小値よりも大きくない場合。
   */
  public static Double verifyGreaterThan(Double ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull Double min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

}
