package undecided.erp.common.primitive;

import java.util.Objects;
import java.util.function.Supplier;
import lombok.NonNull;

public class Objects2 {

  /**
   * 指定したオブジェクトがnullかどうかを調べます。
   *
   * @param obj nullかどうかを確認するオブジェクト
   * @param <T> オブジェクトの型
   * @return オブジェクトがnullの場合は{@code true}、そうでない場合は{@code false}
   */
  public static <T> boolean isNull(T obj) {
    return Objects.isNull(obj);

  }

  /**
   * 与えられたオブジェクトがnullの場合はデフォルト値を返します。
   *
   * @param obj nullチェックするオブジェクト
   * @param defaultValue オブジェクトがnullの場合に返すデフォルト値
   * @param <T> オブジェクトのタイプ
   * @return オブジェクトがnullでなければそのオブジェクト、それ以外の場合はデフォルト値
   */
  public static <T> T defaultIfNull(T obj, T defaultValue) {
    if (isNull(obj)) {
      return defaultValue;
    }
    return obj;
  }

  /**
   * 指定された値がnullではないかを判断します。
   *
   * @param value nullかどうかを確認する値
   * @return 値がnullでない場合は{@code true}、それ以外の場合は{@code false}
   */
  public static boolean nonNull(Object value) {
    return !isNull(value);
  }

  /**
   * 条件がtrueの場合、指定された{@code objSupplier}からオブジェクトを返します。
   * それ以外の場合は{@code defaultValueSupplier}からオブジェクトを返します。
   *
   * @param expression 評価する条件を提供するサプライヤー
   * @param objSupplier 条件がtrueの場合に返すオブジェクトを提供するサプライヤー
   * @param defaultValueSupplier 条件がfalseの場合に返すデフォルトオブジェクトを提供するサプライヤー
   * @param <T> 提供するオブジェクトの型
   * @return 条件がtrueの場合は{@code objSupplier}からのオブジェクト、それ以外の場合は{@code defaultValueSupplier}からのオブジェクト
   */
  public static <T> T defaultIfExpression(@NonNull Supplier<Boolean> expression,
      @NonNull Supplier<T> objSupplier, @NonNull Supplier<T> defaultValueSupplier) {
    if (expression.get()) {
      return objSupplier.get();
    }
    return defaultValueSupplier.get();
  }

}
