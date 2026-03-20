package undecided.erp.common.precondition;

import static undecided.shared.common.primitiveOld.Objects2.isNull;
import static undecided.shared.common.primitiveOld.Strings2.getHalfWidthCharCount;
import static undecided.shared.common.primitiveOld.Strings2.isAllCharacterHalfWidth;

import com.google.common.collect.Range;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import undecided.shared.common.primitiveOld.Strings2;

@UtilityClass
public class StringPrecondition {

  /**
   * 文字列が空でないことを確認するか、カスタマイズされた例外をスローします。
   *
   * @param ref 確認する文字列
   * @param exceptionSupplier 文字列が空の場合にカスタマイズされた例外を返す供給機能
   * @param <E> カスタマイズされた例外のタイプ
   * @return 文字列が非空の場合は、同じ文字列の参照を返します
   * @throws E 文字列が空の場合にスローされます
   */
  public static <E extends RuntimeException> String checkNonEmpty(
      String ref, @NonNull Supplier<E> exceptionSupplier) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (ref == null) {
      return null;
    }

    if (ref.isEmpty()) {
      throw exceptionSupplier.get();
    }
    return ref;
  }

  /**
   * 指定された文字列の半角長さが指定された範囲（上限・下限）内にあることを確認します（排他的）。
   *
   * @param ref 検証する文字列。
   * @param exceptionSupplier もし文字列が要件を満たしていない場合にスローされるカスタム例外を提供するサプライヤー関数。
   * @param min 許可される半角長の最小値。
   * @param max 許可される半角長の最大値。
   * @param <E> カスタム例外の型。
   * @return 半角長が許可された範囲内である場合、同じ文字列への参照。
   * @throws E 半角長が許可された範囲内でない場合。
   */
  public static <E extends RuntimeException> String checkHalfWidthLengthOpen(
      String ref, @NonNull Supplier<E> exceptionSupplier, int min, int max) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (ref == null) {
      return null;
    }
    int length = Strings2.getHalfWidthLength(ref);
    if (!Range.open(min, max).contains(length)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された文字列の半角長が指定された範囲内であることを確認します。
   *
   * @param ref 検証する文字列。
   * @param exceptionSupplier 文字列が基準を満たさない場合にカスタム例外を返すサプライヤー関数。
   * @param min 許容される半角長の最小値。
   * @param max 許容される半角長の最大値。
   * @param <E> カスタム例外のタイプ。
   * @return 半角長が許容範囲内である場合、同じ文字列への参照を返します。
   * @throws E 半角長が許容範囲内でない場合
   */
  public static <E extends RuntimeException> String checkHalfWidthLengthClosed(
      String ref, @NonNull Supplier<E> exceptionSupplier, int min, int max) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (ref == null) {
      return null;
    }
    int length = Strings2.getHalfWidthLength(ref);
    if (!Range.closed(min, max).contains(length)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された文字列の半角長さが最小値以上であることを検証します。
   *
   * @param ref 検証する文字列
   * @param exceptionSupplier 文字列が基準を満たしていない場合にカスタム例外を返すサプライヤー関数
   * @param min 許容される最小の半角長
   * @param <E> カスタム例外の型
   * @return 半角長が最小値以上であれば同じ文字列への参照を返します
   * @throws E 半角長が最小値以上でない場合
   */
  public static <E extends RuntimeException> String checkHalfWidthLengthAtLest(
      String ref, @NonNull Supplier<E> exceptionSupplier, int min) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (ref == null) {
      return null;
    }
    int length = Strings2.getHalfWidthLength(ref);
    if (!Range.atLeast(min).contains(length)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された文字列の半角長さが最小値より大きいことを検証します。
   *
   * @param ref 検証する文字列
   * @param exceptionSupplier 文字列が基準を満たしていない場合にカスタム例外を返すサプライヤー関数
   * @param min 許容される最小の半角長
   * @param <E> カスタム例外の型
   * @return 半角長が最小値より大きければ同じ文字列への参照を返します。{@code ref} が {@code null} の場合は {@code null} を返します。
   * @throws E 半角長が最小値より大きくない場合
   */
  public static <E extends RuntimeException> String checkHalfWidthLengthGraterThan(
      String ref, @NonNull Supplier<E> exceptionSupplier, int min) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (ref == null) {
      return null;
    }
    int length = Strings2.getHalfWidthLength(ref);
    if (!Range.greaterThan(min).contains(length)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された文字列の半角長さが最大値以下であることを検証します。
   *
   * @param ref 検証する文字列
   * @param exceptionSupplier 文字列が基準を満たしていない場合にカスタム例外を返すサプライヤー関数
   * @param max 許容される最大の半角長
   * @param <E> カスタム例外の型
   * @return 半角長が最大値以下であれば同じ文字列への参照を返します
   * @throws E 半角長が最大値以下でない場合
   */
  public static <E extends RuntimeException> String checkHalfWidthLengthAtMost(
      String ref, @NonNull Supplier<E> exceptionSupplier, int max) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (ref == null) {
      return null;
    }
    int length = Strings2.getHalfWidthLength(ref);
    if (!Range.atMost(max).contains(length)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 与えられた文字列の半角長さが指定された最大値より小さいことを検証します。
   *
   * @param ref 検証する文字列。
   * @param exceptionSupplier 文字列が基準を満たしていない場合にカスタム例外を提供するサプライヤー関数。
   * @param max 許容される半角長の最大値。
   * @param <E> カスタム例外のタイプ。
   * @return 半角長さが最大値より小さい場合、同じ文字列への参照を返します。
   * @throws E 半角長さが最大値よりも小さくなければならない場合、例外がスローされます。
   */
  public static <E extends RuntimeException> String checkHalfWidthLengthLessThan(
      String ref, @NonNull Supplier<E> exceptionSupplier, int max) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (ref == null) {
      return null;
    }
    int length = Strings2.getHalfWidthLength(ref);
    if (!Range.lessThan(max).contains(length)) {
      throw exceptionSupplier.get();
    }

    return ref;
  }

  /**
   * 指定された値が有効な10進数の文字列であることを検証します。値がnullの場合は、そのまま返されます。 値が有効な10進数の文字列でない場合、指定された {@link Supplier}
   * によって提供されるカスタム例外が投げられます。
   *
   * @param value 検証されるべき値。
   * @param exceptionSupplier 値が有効な10進数の文字列でない場合に投げられるカスタム例外を提供する関数。
   * @param <E> カスタム例外のタイプ。
   * @return それが有効な10進数の文字列である場合は、同じ値。
   * @throws E 値が有効な10進数の文字列でない場合。
   */
  public static <E extends RuntimeException> String checkAllDecimal(
      String value, @NonNull Supplier<E> exceptionSupplier) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (value == null) {
      return null;
    }
    if (!Strings2.isDecimal(value)) {
      throw exceptionSupplier.get();
    }
    return value;
  }

  /**
   * 指定された文字列が全て半角であり、指定された長さと一致することを検証します。
   *
   * <p>条件を満たさない場合、指定された例外をスローします。
   *
   * @param value 検証する文字列。{@code null} の場合はそのまま戻り値として返されます。
   * @param exceptionSupplier 検証に失敗した場合に例外を生成するサプライヤ。{@code null} であってはなりません。
   * @param length 要求される半角文字の長さ。
   * @param <E> カスタム例外の型。
   * @return 検証に成功した場合、入力された文字列をそのまま返します。
   * @throws NullPointerException {@code exceptionSupplier} が {@code null} の場合。
   * @throws E 文字列が全て半角文字でない場合、または指定された長さに一致しない場合。
   */
  public static <E extends RuntimeException> String checkHalfWidthFixedLength(
      String value, @NonNull Supplier<E> exceptionSupplier, int length) {
    if (isNull(exceptionSupplier)) {
      throw new NullPointerException("exceptionSupplier must not be null.");
    }
    if (value == null) {
      return null;
    }
    if (!isAllCharacterHalfWidth(value)) {
      throw exceptionSupplier.get();
    }
    if (getHalfWidthCharCount(value) != length) {
      throw exceptionSupplier.get();
    }
    return value;
  }
}
