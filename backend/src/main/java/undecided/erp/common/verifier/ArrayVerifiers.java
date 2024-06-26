package undecided.erp.common.verifier;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayVerifiers {

  /**
   * 与えられた配列が空でないことを確認します。
   * <p>
   * 配列が空の場合、IllegalArgumentExceptionの種類の例外がスローされます。
   *
   * @param array 検証される配列
   * @param <T> 配列要素の型
   * @return 非空の場合は同じ配列
   * @throws IllegalArgumentException 配列が空の場合
   */
  public static <T> T[] verifyNotEmpty(T[] array) {
    if (array == null) {
      return array;
    }
    return verifyNotEmpty(array, () -> new IllegalArgumentException("Array must not be empty"));

  }

  /**
   * 与えられた配列が空でないことを確認します。
   *
   * <p>
   * 配列が空の場合、指定されたタイプの例外がスローされます。
   *
   * @param array 確認対象の配列
   * @param supplier 配列が空の場合にスローされる例外を提供するサプライヤー
   * @param <T> 配列要素のタイプ
   * @return 空でない場合は同じ配列
   * @throws RuntimeException 配列が空の場合
   */
  public static <T> T[] verifyNotEmpty(T[] array,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (array == null) {
      return array;
    }

    if (array.length == 0) {
      throw supplier.get();
    }
    return array;
  }

  /**
   * 与えられた配列のすべての要素がnullでないか確認します。
   *
   * @param <E> 配列内の要素のタイプ
   * @param array チェックする配列
   * @param function 要素がnullの場合に{@link IndexedRuntimeException}を返す関数
   * @return すべての要素がnullでない場合は元の配列
   * @throws IndexedRuntimeException 配列のどれかの要素がnullの場合
   */
  public static <E> E[] verifyAllElementNotNull(E[] array,
      @NonNull Function<Integer, ? extends IndexedRuntimeException> function) {
    if (array == null) {
      return array;
    }

    for (int i = 0; i < array.length; i++) {
      if (array[i] == null) {
        throw function.apply(i);
      }

    }
    return array;
  }

  /**
   * 与えられた配列内のいずれかの要素がnullでないかを確認するメソッド。
   * <p>
   * すべての要素がnullの場合、 supplierによって提供されたランタイム例外が発生します。
   *
   * @param <E> 配列内の要素の型
   * @param array 確認する配列
   * @param supplier ランタイム例外を提供するsupplier。
   * 配列がnullまたは配列のすべての要素がnullの場合、supplierによって提供されたランタイム例外が投げられます
   * @return いずれかの要素がnullでない場合、元の配列を返します
   * @throws RuntimeException 配列がnullまたは配列内の全要素がnullの場合、ランタイム例外がスローされます。
   */
  public static <E> E[] verifyAnyElementNotNull(E[] array,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (array == null) {
      return array;
    }
    boolean result = false;
    for (E e : array) {
      if (e != null) {
        result = true;
        break;
      }
    }
    if (!result) {
      throw supplier.get();
    }
    return array;
  }

  /**
   * 与えられた配列にちょうど1つのnullでない要素が存在することを確認するメソッド。そうでない場合は、サプライヤーによって指定されたランタイム例外をスローします。
   *
   * @param <E> 配列の要素の型
   * @param array チェック対象の配列
   * @param supplier チェックに失敗した場合に投げられるランタイム例外を提供するサプライヤー
   * @return チェックが通ればその配列自体を返します
   * @throws RuntimeException 配列がnullであるか、配列内のnullでない要素の数がちょうど1つでない場合に投げられます
   */
  public static <E> E[] verifyOneElementNotNull(E[] array,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (array == null) {
      return array;
    }
    int numberOfNonNullElements = 0;
    for (E e : array) {
      if (e != null) {
        numberOfNonNullElements++;
      }
    }
    if (numberOfNonNullElements != 1) {
      throw supplier.get();
    }
    return array;
  }

  /**
   * 与えられた配列の長さを検証します。
   *
   * @param array 検証されるべき配列
   * @param exceptionSupplier 長さが一致しない場合に投げられる例外を提供するサプライヤ
   * @param size 配列の期待される長さ
   * @param <O> 配列内の要素の型
   * @param <E> スローされる例外の型
   * @return 長さが一致する場合は入力配列
   * @throws E 配列の長さが期待した長さと一致しない場合
   */
  public static <O, E extends RuntimeException> O[] verifyLength(O[] array,
      @NonNull Supplier<E> exceptionSupplier, int size) {
    if (isNull(array)) {
      return null;
    }

    if (size != array.length) {
      throw exceptionSupplier.get();
    }
    return array;
  }

  /**
   * 与えられた配列の長さが指定された最小値よりも長いことを検証します。
   *
   * @param array 検証する配列
   * @param exceptionSupplier 最小値より小さい場合にスローする例外を提供するサプライヤー
   * @param min 配列の最小長
   * @param <ValueType> 配列の要素の型
   * @param <ExceptionType> スローされる例外の型
   * @return 配列の長さが最小値よりも大きい場合は入力配列
   * @throws ExceptionType 配列の長さが最小値より大きくない場合
   */
  public static <ValueType, ExceptionType extends RuntimeException> ValueType[] verifyLengthGreaterThan(
      ValueType[] array, @NonNull Supplier<ExceptionType> exceptionSupplier,
      int min) {
    if (isNull(array)) {
      return null;
    }

    if (array.length <= min) {
      throw exceptionSupplier.get();
    }

    return array;
  }

  /**
   * 与えられた配列の長さが指定された最小値以上であることを確認します。
   *
   * @param array 検証される配列
   * @param exceptionSupplier 配列の長さが最小値未満の場合に投げられる例外の供給元
   * @param min 配列の最小長さ
   * @param <ValueType> 配列内の要素の型
   * @param <ExceptionType> 投げられる例外の型
   * @return 配列の長さが最小値以上の場合、入力配列
   * @throws ExceptionType 配列の長さが最小値未満の場合
   */
  public static <ValueType, ExceptionType extends RuntimeException> ValueType[] verifyLengthAtLeast(
      ValueType[] array, @NonNull Supplier<ExceptionType> exceptionSupplier,
      int min) {
    if (isNull(array)) {
      return null;
    }

    if (array.length < min) {
      throw exceptionSupplier.get();
    }

    return array;
  }

}
