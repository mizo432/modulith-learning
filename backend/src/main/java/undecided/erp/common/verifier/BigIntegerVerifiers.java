package undecided.erp.common.verifier;

import com.google.common.collect.Range;
import java.math.BigInteger;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BigIntegerVerifiers {

  /*
   * 指定されたBigInteger値が正数であることを検証します。値がnullまたは負である場合、 提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigInteger値。
   * @param exceptionSupplier 値が正数でないときにスローされる例外を提供する供給装置。
   * @return 検証に合格した場合は、正数のBigInteger値。
   * @throws RuntimeException 値がnullまたは負であるときにスローされます。
   */
  public static BigInteger verifyPositive(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (Range.greaterThan(BigInteger.ZERO).contains(ref)) {
      return ref;
    }
    throw exceptionSupplier.get();
  }

  /**
   * BigInteger値が正またはゼロであることを確認します。
   *
   * @param ref 確認するBigInteger値。
   * @param exceptionSupplier 値が正またはゼロでない場合にスローされる例外を提供するサプライヤー。
   * @return 値が正またはゼロである場合、元のBigInteger値。
   * @throws RuntimeException 値が正またはゼロでない場合。
   */
  public static BigInteger verifyPositiveOrZero(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atLeast(BigInteger.ZERO).contains(ref)) {
      throw exceptionSupplier.get();
    }
    return ref;
  }

  /**
   * 与えられたBigIntegerが負数であるかどうかを検証します。
   *
   * @param ref 検証されるBigInteger
   * @param exceptionSupplier BigIntegerが負数でない場合にスローされる例外の提供者
   * @return BigIntegerが負の場合は検証されたBigIntegerを返し、そうでない場合は与えられた参照BigIntegerを返します
   * @throws RuntimeException BigIntegerが負数でない場合
   */
  public static BigInteger verifyNegative(final BigInteger ref,
      final @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (Range.lessThan(BigInteger.ZERO).contains(ref)) {
      return ref;
    }
    throw exceptionSupplier.get();
  }

  /**
   * このメソッドは、指定された BigInteger 値が負数またはゼロであるかどうかを確認します。
   *
   * @param ref 確認される BigInteger 値。 null であることもあります。
   * @param exceptionSupplier 値が負数やゼロではない場合にスローされる RuntimeException を提供する supplier。
   * @return 入力値が null の場合は null、それ以外の場合は負数またはゼロの BigInteger 値を返します。
   * @throws RuntimeException 値が負数またはゼロでない場合にスローされます。
   */
  public static BigInteger verifyNegativeOrZero(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atMost(BigInteger.ZERO).contains(ref)) {
      throw exceptionSupplier.get();
    }
    return ref;
  }

  /**
   * 指定した最小値と最大値によって定義される閉範囲内に特定のBigIntegerが存在するかを検証します。
   *
   * @param ref 検証されるBigInteger値。
   * @param exceptionSupplier BigIntegerが範囲内にない場合にスローされるRuntimeExceptionを返すsupplier関数。
   * @param min 閉範囲の最小値。
   * @param max 閉範囲の最大値。
   * @return 検証済みのBigInteger値。
   * @throws RuntimeException BigInteger値が指定された閉範囲内にない場合。
   */
  public static BigInteger verifyRangeClosed(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigInteger min, @NonNull BigInteger max) {
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 特定のBigInteger値が最小値と最大値で定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証対象となるBigInteger値。
   * @param exceptionSupplier BigInteger値が範囲外である場合にスローするRuntimeExceptionを返すsupplier関数。
   * このsupplier関数はnullであってはなりません。
   * @param min 範囲の最小値。
   * @param max 範囲の最大値。
   * @return 検証されたBigInteger値。
   * @throws RuntimeException BigInteger値が指定した開放範囲内にない場合。
   */
  public static BigInteger verifyRangeOpen(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigInteger min, @NonNull BigInteger max) {
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたBigInteger型の値が、最小値と最大値で定義された閉区間-開区間の範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するBigInteger型の値。
   * @param exceptionSupplier BigInteger型の値が範囲外の場合にRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数は非nullでなければなりません。
   * @param min 閉区間-開区間の範囲の最小値。
   * @param max 閉区間-開区間の範囲の最大値。
   * @return 検証済みのBigInteger型の値。
   * @throws RuntimeException BigInteger型の値が指定された閉区間-開区間の範囲内にない場合。
   */
  public static BigInteger verifyRangeClosedOpen(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigInteger min,
      @NonNull BigInteger max) {
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定された開放-閉鎖範囲（最小値と最大値によって定義される）内に、与えられたBigInteger値が存在するかどうかを確認します。
   *
   * @param ref 検証するBigInteger値。
   * @param exceptionSupplier BigInteger値が範囲外の場合に投げられるRuntimeExceptionを返すサプライヤー関数。
   * このサプライヤー関数はnullであってはなりません。
   * @param min 開放-閉鎖範囲の最小値。
   * @param max 開放-閉鎖範囲の最大値。
   * @return 検証したBigInteger値。
   * @throws RuntimeException BigInteger値が指定した開放-閉鎖範囲内にない場合。
   */
  public static BigInteger verifyRangeOpenClosed(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigInteger min,
      @NonNull BigInteger max) {
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
  public static BigInteger verifyAtLest(final BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      final @NonNull BigInteger min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたBigInteger値が、最大値によって定義された範囲内にあるかどうかを検証します。
   *
   * @param ref 検証するBigInteger値。
   * @param exceptionSupplier BigInteger値が範囲外の場合に、RuntimeExceptionを返すサプライヤー関数。
   * @param max 範囲の最大値。
   * @return 検証されたBigInteger値を返します。
   * @throws RuntimeException BigInteger値が指定範囲内に存在しない場合にスローされます。
   */
  public static BigInteger verifyAtMost(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigInteger max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 指定されたBigInteger値が最小値よりも大きいかどうかを検証します。
   *
   * @param ref 検証するBigInteger値。
   * @param exceptionSupplier 範囲内に値がない場合にスローされるRuntimeExceptionを返す供給関数。
   * @param min 最小値。
   * @return 検証されたBigInteger値。
   * @throws RuntimeException BigInteger値が最小値よりも大きくない場合。
   */
  public static BigInteger verifyGreaterThan(final BigInteger ref,
      final @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      final @NonNull BigInteger min) {
    if (ref == null) {
      return ref;
    }
    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

  /**
   * 与えられたBigIntegerの値が、指定された最大値よりも小さいかどうかを検証します。
   *
   * @param ref 検証対象となるBigIntegerの値。
   * @param exceptionSupplier BigIntegerの値が範囲内にない場合にスローされるRuntimeExceptionを返すSupplier関数。
   * @param max 範囲の最大値。
   * @return 検証済みのBigIntegerの値。
   * @throws RuntimeException BigIntegerの値が指定された最大値未満でない場合。
   */
  public static BigInteger verifyLessThan(BigInteger ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier, @NonNull BigInteger max) {
    if (ref == null) {
      return ref;
    }
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;

  }

}
