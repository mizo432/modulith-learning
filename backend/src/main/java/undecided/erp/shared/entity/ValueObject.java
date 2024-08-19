package undecided.erp.shared.entity;

import java.util.Arrays;
import java.util.function.Supplier;
import lombok.NonNull;
import undecided.erp.common.primitive.Objects2;

public interface ValueObject {


  boolean isEmpty();

  public class ValueObjects {

    /**
     * 提供された値オブジェクトの配列内のすべての値が空でないかどうかを判定します。
     *
     * @param valueObjects 空かどうかを確認する値オブジェクト
     * @param <V> 値オブジェクトの型
     * @return すべての値が空でない場合は {@code true}、それ以外の場合は {@code false}
     */
    @SafeVarargs
    public static <V extends ValueObject> boolean isAllValuesNotEmpty(V... valueObjects) {
      return Arrays.stream(valueObjects).allMatch(ValueObjects::nonEmpty);

    }

    /**
     * 値オブジェクトが空でないかを判定します。
     *
     * @param valueObject 空でないかを確認する値オブジェクト
     * @return 値オブジェクトが空でない場合はtrue、そうでない場合はfalse
     */
    @undecided.erp.common.annotation.VisibleForTesting
    static <V extends ValueObject> boolean nonEmpty(V valueObject) {
      return !isEmpty(valueObject);

    }

    /**
     * 値オブジェクトが空であるかどうかを確認します。
     *
     * @param valueObject 空であるかを確認する値オブジェクト
     * @return 値オブジェクトが空の場合は true、そうでない場合は false
     */
    public static <V extends ValueObject> boolean isEmpty(V valueObject) {
      return valueObject.isEmpty();

    }


    /**
     * 指定された値オブジェクトが空でないかどうかをチェックします。
     * <p>
     * 値オブジェクトが空の場合、指定された例外サプライヤで提供されたランタイム例外をスローします。
     *
     * @param ref 空でないかをチェックする値オブジェクト
     * @param exceptionSupplier 値オブジェクトが空の場合にスローされるランタイム例外を提供するサプライヤ
     * @param <E> ランタイム例外の型
     * @param <V> 値オブジェクトの型
     * @return 値オブジェクトが空でない場合、その非空の値オブジェクト
     * @throws E 値オブジェクトが空の場合にスローされる例外
     */
    public static <E extends RuntimeException, V extends ValueObject> V checkNotEmpty(
        @NonNull V ref,
        @NonNull Supplier<E> exceptionSupplier) {
      if (ref.isEmpty()) {
        throw exceptionSupplier.get();
      }
      return ref;
    }

    /**
     * 参照値がnullでない場合はその参照値を返し、そうでない場合はデフォルト値を返します。
     *
     * @param ref nullかどうかをチェックする参照値
     * @param defaultValue 参照値がnullの場合に返すデフォルト値
     * @param <V> 値オブジェクトの型
     * @return 参照値がnullでない場合は参照値、それ以外の場合はデフォルト値
     */
    public static <V extends ValueObject> V defaultIfNull(V ref, V defaultValue) {
      if (Objects2.isNull(ref)) {
        return defaultValue;
      }
      return ref;

    }

    /**
     * 参照値が空でない場合はその値を返し、それ以外の場合はデフォルト値を返します。
     *
     * @param ref nullかどうかを確認する参照値
     * @param defaultValue 参照値がnullの場合に返すデフォルト値
     * @param <V> 値オブジェクトのタイプ
     * @return 参照値が空でない場合はその値、それ以外の場合はデフォルト値
     */
    public static <V extends ValueObject> V defaultIfEmpty(V ref, V defaultValue) {
      V resolvedRef = defaultIfNull(ref, defaultValue);
      return isEmpty(resolvedRef) ? defaultValue : resolvedRef;
    }
  }

}
