package undecided.erp.shared.entity;

import java.util.Arrays;

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

  }

}
