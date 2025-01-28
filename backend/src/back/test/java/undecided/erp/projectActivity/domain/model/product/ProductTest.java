package undecided.erp.projectActivity.domain.model.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.entity.SnowflakeId;

@DisplayName("Productクラスの")
class ProductTest {

  @Nested
  @DisplayName("createメソッドは")
  class CreateTest {

    @Test
    @DisplayName("ProductAttributeが正しい場合、新しいProductを生成する")
    void shouldCreateProductWhenProductAttributeIsValid() {
      ProductAttribute attribute = ProductAttribute.of(ProductName.of("test"),
          ProductCode.of("Code12"));
      Product product = Product.create(attribute);

      assertThat(product).isNotNull();
      assertThat(product.getId()).isNotNull();
      assertThat(product.getAttribute()).isEqualTo(attribute);
    }

    @Test
    @DisplayName("ProductAttributeがnullの場合、例外をスローする")
    void shouldThrowExceptionWhenProductAttributeIsNull() {
      assertThatThrownBy(() -> Product.create(null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("equalsメソッドは")
  class EqualsTest {

    @Test
    @DisplayName("同一のIdのProductオブジェクトを比較すると、trueを返す")
    void shouldReturnTrueWhenTwoProductsHaveSameId() {
      SnowflakeId id = SnowflakeId.newInstance();
      ProductAttribute attribute = ProductAttribute.of(ProductName.of("test"),
          ProductCode.of("Code12"));

      Product product1 = new Product(id, attribute);
      Product product2 = new Product(id, attribute);

      assertThat(product1.equals(product2)).isTrue();
    }

    @Test
    @DisplayName("異なるIdのProductオブジェクトを比較すると、falseを返す")
    void shouldReturnFalseWhenTwoProductsHaveDifferentId() {
      ProductAttribute attribute = ProductAttribute.of(ProductName.of("test"),
          ProductCode.of("Code12"));

      Product product1 = new Product(SnowflakeId.newInstance(), attribute);
      Product product2 = new Product(SnowflakeId.newInstance(), attribute);

      assertThat(product1.equals(product2)).isFalse();
    }

    @Test
    @DisplayName("nullと比較すると、falseを返す")
    void shouldReturnFalseWhenComparisonWithNull() {
      ProductAttribute attribute = ProductAttribute.of(ProductName.of("test"),
          ProductCode.of("Code12"));
      Product product = new Product(SnowflakeId.newInstance(), attribute);

      assertThat(product.equals(null)).isFalse();
    }
  }

  @Nested
  @DisplayName("hashCodeメソッドは")
  class HashCodeTest {

    @Test
    @DisplayName("同一のIdのProductオブジェクトを比較すると、同じハッシュコードを返す")
    void shouldReturnSameHashCodeWhenTwoProductsHaveSameId() {
      SnowflakeId id = SnowflakeId.newInstance();
      ProductAttribute attribute = ProductAttribute.of(ProductName.of("test"),
          ProductCode.of("Code12"));

      Product product1 = new Product(id, attribute);
      Product product2 = new Product(id, attribute);

      assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
    }

    @Test
    @DisplayName("異なるIdのProductオブジェクトを比較すると、異なるハッシュコードを返す")
    void shouldReturnDifferentHashCodeWhenTwoProductsHaveDifferentId() {
      ProductAttribute attribute = ProductAttribute.of(ProductName.of("test"),
          ProductCode.of("Code12"));

      Product product1 = new Product(SnowflakeId.newInstance(), attribute);
      Product product2 = new Product(SnowflakeId.newInstance(), attribute);

      assertThat(product1.hashCode()).isNotEqualTo(product2.hashCode());
    }
  }
}
