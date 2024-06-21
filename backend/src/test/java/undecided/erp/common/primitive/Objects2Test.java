package undecided.erp.common.primitive;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class Objects2Test {

  /**
   * このテストは、Objects2クラスのisNullメソッドを検証します。
   * <p>
   * シナリオ: 入力オブジェクトがnullの場合、関数はtrueを返すべきです。
   */
  @Test
  void isNullTrueTest() {
    Integer value = null;

    assertTrue(Objects2.isNull(value));
  }

  /**
   * このテストは、Objects2クラスのisNullメソッドを検証します。
   * <p>
   * シナリオ: 入力オブジェクトが非nullの場合、関数はfalseを返すべきです。
   */
  @Test
  void isNullFalseTest() {
    Integer value = 10;

    assertFalse(Objects2.isNull(value));
  }
}