package undecided.erp.projectActivity.domain.model.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductAttributeTest {

  @DisplayName("TestClass: ProductAttribute method: of")
  @Nested
  class OfTest {

    @DisplayName("TestMethod: 入力のProductNameとProductCodeがNullの場合はException")
    @Test
    void shouldThrowExceptionWhenInputsAreNull() {
      assertThatThrownBy(() -> ProductAttribute.of(null, null)).isInstanceOf(
          NullPointerException.class);
    }

    @DisplayName("TestMethod: 入力のProductNameがnullの場合Exception")
    @Test
    void shouldThrowExceptionWhenProductNameIsNull() {
      ProductCode productCode = ProductCode.of("Code12");
      assertThatThrownBy(() -> ProductAttribute.of(null, productCode)).isInstanceOf(
          NullPointerException.class);
    }

    @DisplayName("TestMethod: 入力のProductCodeがnullの場合Exception")
    @Test
    void shouldThrowExceptionWhenProductCodeIsNull() {
      ProductName productName = ProductName.of("Product-Name");
      assertThatThrownBy(() -> ProductAttribute.of(productName, null)).isInstanceOf(
          NullPointerException.class);
    }

    @DisplayName("TestMethod: 正常なProductNameとProductCodeが入力された場合ProductAttributeのインスタンス返す")
    @Test
    void shouldReturnProductAttributeInstanceWhenInputsAreValid() {
      ProductName productName = ProductName.of("Product-Name");
      ProductCode productCode = ProductCode.of("Code12");
      ProductAttribute productAttribute = ProductAttribute.of(productName, productCode);

      assertThat(productAttribute).isNotNull();
      assertThat(productAttribute.getName()).isEqualTo(productName);
      assertThat(productAttribute.getCode()).isEqualTo(productCode);
    }
  }

}
