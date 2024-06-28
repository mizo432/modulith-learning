package undecided.erp.common.primitive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Objects2Test {

  @Nested
  class IsNullTest {

    /**
     * このテストは、Objects2クラスのisNullメソッドを検証します。
     * <p>
     * シナリオ: 入力オブジェクトがnullの場合、関数はtrueを返すべきです。
     */
    @Test
    void shouldReturnTrueWhenInputIsNull() {
      Integer value = null;
      assertTrue(Objects2.isNull(value));
    }

    /**
     * このテストは、Objects2クラスのisNullメソッドを検証します。
     * <p>
     * シナリオ: 入力オブジェクトが非nullの場合、関数はfalseを返すべきです。
     */
    @Test
    void shouldReturnFalseWhenInputIsNotNull() {
      Integer value = 10;
      assertFalse(Objects2.isNull(value));
    }
  }

  @Nested
  class DefaultIfNullTest {

    /**
     * このテストは、Objects2クラスのdefaultIfNullメソッドを検証します。
     * <p>
     * シナリオ: 入力オブジェクトがnullの場合、関数はデフォルト値を返すべきです。
     */
    @Test
    void shouldReturnDefaultWhenInputIsNull() {
      Integer value = null;
      Integer defaultValue = 10;
      assertEquals(defaultValue, Objects2.defaultIfNull(value, defaultValue));
    }

    /**
     * このテストは、Objects2クラスのdefaultIfNullメソッドを検証します。
     * <p>
     * シナリオ: 入力オブジェクトが非nullの場合、関数はオブジェクト自身を返すべきです。
     */
    @Test
    void shouldReturnInputWhenIfNotNull() {
      Integer value = 20;
      Integer defaultValue = 10;
      assertEquals(value, Objects2.defaultIfNull(value, defaultValue));
    }
  }
}
