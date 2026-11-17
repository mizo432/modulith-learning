package com.github.mizo432.shared.sample.jpa.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.github.mizo432.shared.sample.jpa.entity.converter.SampleCurrencyCode;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleProductの単体テスト")
class SampleProductTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数で商品エンティティが作成される")
    void shouldCreateProductWithValidArguments() {
      // Arrange
      SnowflakeId productId = SnowflakeId.of(1L);
      String productName = "テスト商品";
      Long price = 1000L;
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of("JPY");

      // Act
      SampleProduct product = new SampleProduct(productId, productName, price, currencyCode);

      // Assert
      assertThat(product).isNotNull();
      assertThat(product.getProductId()).isEqualTo(productId);
      assertThat(product.getProductName()).isEqualTo(productName);
      assertThat(product.getPrice()).isEqualTo(price);
      assertThat(product.getCurrencyCode()).isEqualTo(currencyCode);
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("商品名が設定・取得できる")
    void shouldSetAndGetProductName() {
      // Arrange
      SampleProduct product =
          new SampleProduct(SnowflakeId.of(1L), "初期名", 100L, SampleCurrencyCode.of("JPY"));

      // Act
      product.setProductName("変更後の名前");

      // Assert
      assertThat(product.getProductName()).isEqualTo("変更後の名前");
    }

    @Test
    @DisplayName("価格が設定・取得できる")
    void shouldSetAndGetPrice() {
      // Arrange
      SampleProduct product =
          new SampleProduct(SnowflakeId.of(1L), "商品", 100L, SampleCurrencyCode.of("JPY"));

      // Act
      product.setPrice(200L);

      // Assert
      assertThat(product.getPrice()).isEqualTo(200L);
    }

    @Test
    @DisplayName("通貨コードが設定・取得できる")
    void shouldSetAndGetCurrencyCode() {
      // Arrange
      SampleProduct product =
          new SampleProduct(SnowflakeId.of(1L), "商品", 100L, SampleCurrencyCode.of("JPY"));

      // Act
      SampleCurrencyCode usd = SampleCurrencyCode.of("USD");
      product.setCurrencyCode(usd);

      // Assert
      assertThat(product.getCurrencyCode()).isEqualTo(usd);
    }
  }

  @Nested
  @DisplayName("hasCurrencyCodeメソッドのテスト")
  class HasCurrencyCodeTest {

    @Test
    @DisplayName("通貨コードが設定されている場合はtrueを返す")
    void shouldReturnTrueWhenCurrencyCodeIsSet() {
      // Arrange
      SampleProduct product =
          new SampleProduct(SnowflakeId.of(1L), "商品", 100L, SampleCurrencyCode.of("JPY"));

      // Act & Assert
      assertThat(product.hasCurrencyCode()).isTrue();
    }

    @Test
    @DisplayName("通貨コードが空の場合はfalseを返す")
    void shouldReturnFalseWhenCurrencyCodeIsEmpty() {
      // Arrange
      SampleProduct product =
          new SampleProduct(SnowflakeId.of(1L), "商品", 100L, SampleCurrencyCode.empty());

      // Act & Assert
      assertThat(product.hasCurrencyCode()).isFalse();
    }

    @Test
    @DisplayName("通貨コードがnullの場合はfalseを返す")
    void shouldReturnFalseWhenCurrencyCodeIsNull() {
      // Arrange
      SampleProduct product = new SampleProduct(SnowflakeId.of(1L), "商品", 100L, null);

      // Act & Assert
      assertThat(product.hasCurrencyCode()).isFalse();
    }
  }

  @Nested
  @DisplayName("SnowflakeIdの使用例テスト")
  class SnowflakeIdTest {

    @Test
    @DisplayName("SnowflakeIdを@Idとして使用できる")
    void shouldUseSnowflakeIdAsId() {
      // Arrange
      SnowflakeId productId = SnowflakeId.of(1234567890L);

      // Act
      SampleProduct product =
          new SampleProduct(productId, "商品", 100L, SampleCurrencyCode.of("JPY"));

      // Assert
      assertThat(product.getProductId()).isEqualTo(productId);
      assertThat(product.getProductId().value()).isEqualTo(1234567890L);
    }
  }
}
