package undecided.erp.common.precondition;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.shared.common.primitiveOld.Objects2.isNull;

import com.google.common.collect.Range;
import java.math.BigDecimal;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * BigDecimalの値に対する事前条件をチェックするユーティリティクラスです。
 *
 * <p>このクラスは、各メソッドを使用してBigDecimalの値が特定の条件を満たしているかを検証し、 条件を満たさない場合には例外をスローします。
 * すべてのメソッドはstaticであり、インスタンス化することはできません。
 */
@UtilityClass
public class BigDecimalPrecondition {

  /**
   * 指定されたBigDecimal値が正数であることを検証します。値がnullまたは負である場合、 提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。
   * @param exceptionSupplier 値が正数でないときにスローされる例外を提供する供給装置。
   * @return 検証に合格した場合は、正数のBigDecimal値。
   * @throws RuntimeException 値がnullまたは負であるときにスローされます。
   */
  public static @Nullable BigDecimal checkPositive(
      @Nullable final BigDecimal ref,
      @NonNull final Supplier<? extends RuntimeException> exceptionSupplier) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    if (isNull(ref)) return null;
    return checkGreaterThan(ref, exceptionSupplier, BigDecimal.ZERO);
  }

  /**
   * 指定されたBigDecimal値が負でないことを検証します。
   *
   * <p>値がnullの場合はnullを返します。 値が負の場合、提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。 nullを許容します。
   * @param exceptionSupplier 値が負である場合にスローされる例外を提供する供給装置。nullであってはなりません。
   * @return 入力値がnullの場合はnull、値が負でない場合はその値を返します。
   * @throws RuntimeException 値が負である場合にスローされます。
   */
  public static @Nullable BigDecimal checkNotNegative(
      @Nullable BigDecimal ref, @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    if (isNull(ref)) return null;
    return checkAtLeast(ref, exceptionSupplier, BigDecimal.ZERO);
  }

  /**
   * 指定されたBigDecimal値が負数であることを検証します。
   *
   * <p>値がnullの場合はnullを返します。値が負数でない場合、提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。
   * @param exceptionSupplier 値が負数でない場合にスローされる例外を提供する供給装置。nullではありません。
   * @return 検証されたBigDecimal値が負数の場合はその値、値がnullの場合はnullを返します。
   * @throws RuntimeException 値が負数でない場合にスローされます。
   */
  public static @Nullable BigDecimal checkNegative(
      @Nullable BigDecimal ref, @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    if (isNull(ref)) return null;
    return checkLessThan(ref, exceptionSupplier, BigDecimal.ZERO);
  }

  /**
   * 指定されたBigDecimal値が正でないことを検証します。
   *
   * <p>検証対象の値がnullの場合はnullを返します。値が正である場合、 提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。
   * @param exceptionSupplier 値が正である場合にスローされる例外を提供する供給装置。nullであってはなりません。
   * @return 検証対象の値がnullの場合はnull、それ以外の場合、正でないBigDecimal値を返します。
   * @throws RuntimeException 値が正である場合にスローされます。
   */
  public static @Nullable BigDecimal checkNotPositive(
      @Nullable BigDecimal ref, @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    if (isNull(ref)) return null;
    return checkAtMost(ref, exceptionSupplier, BigDecimal.ZERO);
  }

  /**
   * 指定されたBigDecimal値が、最小値と最大値で定義される閉区間内にあるかどうかを検証します。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。この値が範囲内にあるかどうかを確認します。
   * @param exceptionSupplier refが範囲外の場合にスローされるRuntimeExceptionを提供するサプライヤー。nullであってはなりません。
   * @param min 範囲の最小値。nullであってはなりません。
   * @param max 範囲の最大値。nullであってはなりません。
   * @return refがnullの場合はnull、範囲内である場合は同じrefを返します。
   * @throws RuntimeException refがmin以上max以下の範囲外である場合にスローされます。
   */
  public static @Nullable BigDecimal checkRangeClosed(
      @Nullable BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal min,
      @NonNull BigDecimal max) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(min, () -> new NullPointerException("min must not be null."));
    checkNotNull(max, () -> new NullPointerException("max must not be null."));
    if (isNull(ref)) return null;
    if (!Range.closed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたBigDecimal値が、最小値と最大値によって定義される開区間内にあるかどうかを検証します。
   *
   * <p>値がnullの場合、nullを返します。値が範囲外の場合、提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。この値が範囲内にあるかどうかを確認します。
   * @param exceptionSupplier refが範囲外の場合にスローされるRuntimeExceptionを提供するサプライヤー。nullではありません。
   * @param min 範囲の最小値（開区間最小値）。nullではありません。
   * @param max 範囲の最大値（開区間最大値）。nullではありません。
   * @return refがnullの場合はnull、範囲内である場合は同じrefを返します。
   * @throws RuntimeException refがminより大きくmaxより小さい範囲外である場合にスローされます。
   */
  public static @Nullable BigDecimal checkRangeOpen(
      @Nullable BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal min,
      @NonNull BigDecimal max) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(min, () -> new NullPointerException("min must not be null."));
    checkNotNull(max, () -> new NullPointerException("max must not be null."));
    if (isNull(ref)) return null;
    if (!Range.open(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたBigDecimal値が、最小値以上で最大値未満の閉-開区間内にあるかを検証します。
   * 値がnullの場合はnullを返します。値が範囲外の場合、提供されたexceptionSupplierによって例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。 この値が[min, max)の範囲内にあるかどうかを確認します。
   * @param exceptionSupplier refが範囲外の場合にスローされるRuntimeExceptionを提供するサプライヤー。 nullであってはなりません。
   * @param min 範囲の最小値（閉区間の下限）。nullであってはなりません。
   * @param max 範囲の最大値（開区間の上限）。nullであってはなりません。
   * @return refがnullの場合はnull、範囲内にある場合はそのrefをそのまま返します。
   * @throws RuntimeException refがmin以上max未満の範囲外である場合にスローされます。
   */
  public static @Nullable BigDecimal checkRangeClosedOpen(
      @Nullable BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal min,
      @NonNull BigDecimal max) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(min, () -> new NullPointerException("min must not be null."));
    checkNotNull(max, () -> new NullPointerException("max must not be null."));
    if (isNull(ref)) return null;
    if (!Range.closedOpen(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたBigDecimal値が、最小値より大きく最大値以下の開-閉区間内にあるかを検証します。
   * 値がnullの場合はnullを返します。値が範囲外の場合、提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。この値が(min, max]の範囲内にあるかどうかを確認します。
   * @param exceptionSupplier refが範囲外の場合にスローされるRuntimeExceptionを提供するサプライヤー。nullではありません。
   * @param min 範囲の最小値（開区間の下限）。nullではありません。
   * @param max 範囲の最大値（閉区間の上限）。nullではありません。
   * @return refがnullの場合はnull、範囲内にある場合はそのrefをそのまま返します。
   * @throws RuntimeException refがminより小さい、またはmaxより大きい範囲外である場合にスローされます。
   */
  public static @Nullable BigDecimal checkRangeOpenClosed(
      @Nullable BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal min,
      @NonNull BigDecimal max) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(min, () -> new NullPointerException("min must not be null."));
    checkNotNull(max, () -> new NullPointerException("max must not be null."));
    if (isNull(ref)) return null;
    if (!Range.openClosed(min, max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたBigDecimal値が最小値以上であることを検証します。
   *
   * <p>検証対象の値がnullの場合はnullを返します。 値が最小値未満である場合、提供された例外サプライヤーに従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。
   * @param exceptionSupplier 値が最小値未満の場合にスローされる例外を提供するサプライヤー。nullであってはなりません。
   * @param min 最小値。nullであってはなりません。
   * @return refがnullの場合はnull、最小値以上の場合は同じrefを返します。
   * @throws RuntimeException refがminより小さい場合にスローされます。
   */
  public static @Nullable BigDecimal checkAtLeast(
      @Nullable final BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      final @NonNull BigDecimal min) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(min, () -> new NullPointerException("min must not be null."));
    if (isNull(ref)) return null;
    if (!Range.atLeast(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたBigDecimal値が最大値以下であることを検証します。
   *
   * <p>検証対象の値がnullの場合はnullを返します。 値が最大値を超える場合、提供された例外供給装置に従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。
   * @param exceptionSupplier 値が最大値を超えた場合にスローされる例外を提供するサプライヤー。nullであってはなりません。
   * @param max 最大値。nullであってはなりません。
   * @return refがnullの場合はnull、最大値以下の場合は同じrefを返します。
   * @throws RuntimeException refが最大値を超える場合にスローされます。
   */
  public static @Nullable BigDecimal checkAtMost(
      @Nullable BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal max) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(max, () -> new NullPointerException("max must not be null."));
    if (isNull(ref)) return null;
    if (!Range.atMost(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたBigDecimal値が指定された最大値未満であることを検証します。
   *
   * <p>検証対象の値がnullの場合はnullを返します。 値が最大値以上の場合、提供された例外サプライヤーに従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。
   * @param exceptionSupplier 値が最大値未満でない場合にスローされる例外を提供するサプライヤー。nullであってはなりません。
   * @param max 最大値。nullであってはなりません。
   * @return refがnullの場合はnull、検証に合格した場合は同じrefを返します。
   * @throws RuntimeException refが最大値以上の場合にスローされます。
   */
  public static @Nullable BigDecimal checkLessThan(
      @Nullable BigDecimal ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      @NonNull BigDecimal max) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(max, () -> new NullPointerException("max must not be null."));
    if (isNull(ref)) return null;
    if (!Range.lessThan(max).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定されたBigDecimal値が、指定された最小値より大きいことを検証します。
   *
   * <p>検証対象の値がnullの場合はnullを返します。値が最小値以下の場合、提供された例外 サプライヤーに従って例外がスローされます。
   *
   * @param ref 検証するBigDecimal値。nullを許容します。
   * @param exceptionSupplier 値が最小値以下の場合にスローされる例外を提供するサプライヤー。nullであってはなりません。
   * @param min 最小値（この値より大きい必要があります）。nullであってはなりません。
   * @return refがnullの場合はnull、最小値より大きい場合は同じrefを返します。
   * @throws RuntimeException refが最小値以下の場合にスローされます。
   */
  public static @Nullable BigDecimal checkGreaterThan(
      @Nullable final BigDecimal ref,
      final @NonNull Supplier<? extends RuntimeException> exceptionSupplier,
      final @NonNull BigDecimal min) {
    checkNotNull(
        exceptionSupplier, () -> new NullPointerException("exceptionSupplier must not be null."));
    checkNotNull(min, () -> new NullPointerException("min must not be null."));
    if (isNull(ref)) return null;
    if (!Range.greaterThan(min).contains(ref)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }
}
