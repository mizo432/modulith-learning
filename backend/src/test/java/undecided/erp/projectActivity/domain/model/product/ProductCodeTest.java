package undecided.erp.projectActivity.domain.model.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductCodeのテスト")
class ProductCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethod {

    @Test
    @DisplayName("正常な文字列を入力した場合、ProductCodeが生成されること")
    void shouldProduceProductCodeWhenValidStringIsInput() {
      String expectedValue = "123456";
      ProductCode productCode = ProductCode.of(expectedValue);
      assertThat(productCode.getValue()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("nullを入力した場合、NullPointerExceptionが送出されること")
    void shouldThrowNullPointerExceptionWhenNullIsInput() {
      assertThatThrownBy(() -> ProductCode.of(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("空文字を入力した場合、IllegalArgumentExceptionが送出されること")
    void shouldThrowIllegalArgumentExceptionWhenEmptyStringIsInput() {
      assertThatThrownBy(() -> ProductCode.of("")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("長さ6以外の文字列を入力した場合、IllegalArgumentExceptionが送出されること")
    void shouldThrowIllegalArgumentExceptionWhenStringLengthIsNot6() {
      assertThatThrownBy(() -> ProductCode.of("12345")).isInstanceOf(
          IllegalArgumentException.class);
      assertThatThrownBy(() -> ProductCode.of("1234567")).isInstanceOf(
          IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethod {

    @Test
    @DisplayName("valueがnullの場合、trueが返されること")
    void shouldReturnTrueWhenValueIsNull() {
      ProductCode productCode = new ProductCode();
      assertThat(productCode.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("valueが非nullの場合、falseが返されること")
    void shouldReturnFalseWhenValueIsNotNull() {
      String expectedValue = "123456";
      ProductCode productCode = ProductCode.of(expectedValue);
      assertThat(productCode.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethod {

    @Test
    @DisplayName("valueがnullの場合、'null'が返されること")
    void shouldReturnNullWhenValueIsNull() {
      ProductCode productCode = new ProductCode();
      assertThat(productCode.toString()).isEqualTo("null");
    }

    @Test
    @DisplayName("valueが非nullの場合、そのvalueが返されること")
    void shouldReturnValueWhenValueIsNotNull() {
      String expectedValue = "123456";
      ProductCode productCode = ProductCode.of(expectedValue);
      assertThat(productCode.toString()).isEqualTo(expectedValue);
    }
  }
}
