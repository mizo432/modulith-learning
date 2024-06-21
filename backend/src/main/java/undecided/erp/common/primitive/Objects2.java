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
}
