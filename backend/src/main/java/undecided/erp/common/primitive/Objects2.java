package undecided.erp.common.primitive;

import java.util.Objects;

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
  public static boolean nonNull(String value) {
    return !isNull(value);
  }
}
