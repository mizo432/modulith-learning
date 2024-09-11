package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;
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
      assertThat(Objects2.isNull(value)).isTrue();
    }

    /**
     * このテストは、Objects2クラスのisNullメソッドを検証します。
     * <p>
     * シナリオ: 入力オブジェクトが非nullの場合、関数はfalseを返すべきです。
     */
    @Test
    void shouldReturnFalseWhenInputIsNotNull() {
      Integer value = 10;
      assertThat(Objects2.isNull(value)).isFalse();
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
      assertThat(Objects2.defaultIfNull(value, defaultValue)).isEqualTo(defaultValue);
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
      assertThat(Objects2.defaultIfNull(value, defaultValue)).isEqualTo(value);
    }
  }

  @Nested
  class NonNullTest {

    /**
     * This test verifies the nonNull method of the Objects2 class.
     * <p>
     * Scenario: When the input object is null, the function should return false.
     */
    @Test
    void shouldReturnFalseWhenValueIsNull() {
      String value = null;
      assertThat(Objects2.nonNull(value)).isFalse();
    }

    /**
     * This test verifies the nonNull method of the Objects2 class.
     * <p>
     * Scenario: When the input object is non-null, the function should return true.
     */
    @Test
    void shouldReturnTrueWhenValueIsNotNull() {
      String value = "test";
      assertThat(Objects2.nonNull(value)).isTrue();
    }
  }

  @Nested
  class DefaultIfExpressionTest {

    /**
     * このテストは、Objects2クラスのdefaultIfExpressionメソッドを検証します。
     * <p>
     * シナリオ: 式が真の場合、関数はオブジェクトを返すべきです。
     */
    @Test
    void shouldReturnObjectWhenExpressionIsTrue() {
      Supplier<Boolean> expression = () -> true;
      Supplier<String> objSupplier = () -> "test";
      Supplier<String> defaultValueSupplier = () -> "default";
      assertThat(
          Objects2.defaultIfExpression(expression, objSupplier, defaultValueSupplier)).isEqualTo(
          "test");
    }

    /**
     * このテストは、Objects2クラスのdefaultIfExpressionメソッドを検証します。
     * <p>
     * シナリオ: 式が偽の場合、関数はデフォルト値を返すべきです。
     */
    @Test
    void shouldReturnDefaultValueWhenExpressionIsFalse() {
      Supplier<Boolean> expression = () -> false;
      Supplier<String> objSupplier = () -> "test";
      Supplier<String> defaultValueSupplier = () -> "default";
      assertThat(
          Objects2.defaultIfExpression(expression, objSupplier, defaultValueSupplier)).isEqualTo(
          "default");
    }
  }
}
