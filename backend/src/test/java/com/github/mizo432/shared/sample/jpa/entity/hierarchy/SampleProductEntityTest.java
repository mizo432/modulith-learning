package com.github.mizo432.shared.sample.jpa.entity.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleProductEntityの単体テスト")
class SampleProductEntityTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数でエンティティが作成される")
    void shouldCreateEntityWithValidArguments() {
      // Arrange
      SnowflakeId productId = SnowflakeId.of(1L);
      String productName = "テスト製品";
      Long price = 1000L;

      // Act
      SampleProductEntity entity = new SampleProductEntity(productId, productName, price);

      // Assert
      assertThat(entity).isNotNull();
      assertThat(entity.getProductId()).isEqualTo(productId);
      assertThat(entity.getProductName()).isEqualTo(productName);
      assertThat(entity.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("価格が0でもエンティティが作成される")
    void shouldCreateEntityWhenPriceIsZero() {
      // Arrange
      SnowflakeId productId = SnowflakeId.of(1L);
      String productName = "無料製品";
      Long price = 0L;

      // Act
      SampleProductEntity entity = new SampleProductEntity(productId, productName, price);

      // Assert
      assertThat(entity.getPrice()).isZero();
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("製品名が設定・取得できる")
    void shouldSetAndGetProductName() {
      // Arrange
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "初期名", 100L);

      // Act
      entity.setProductName("変更後の名前");

      // Assert
      assertThat(entity.getProductName()).isEqualTo("変更後の名前");
    }

    @Test
    @DisplayName("価格が設定・取得できる")
    void shouldSetAndGetPrice() {
      // Arrange
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "製品", 100L);

      // Act
      entity.setPrice(200L);

      // Assert
      assertThat(entity.getPrice()).isEqualTo(200L);
    }

    @Test
    @DisplayName("productIdが設定・取得できる")
    void shouldSetAndGetProductId() {
      // Arrange
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "製品", 100L);

      // Act
      SnowflakeId newId = SnowflakeId.of(2L);
      entity.setProductId(newId);

      // Assert
      assertThat(entity.getProductId()).isEqualTo(newId);
    }
  }

  @Nested
  @DisplayName("継承フィールドのテスト")
  class InheritedFieldsTest {

    @Test
    @DisplayName("createdAtフィールドが存在する")
    void shouldHaveCreatedAtField() {
      // Arrange & Act
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "製品", 100L);

      // Assert
      assertThat(entity.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("createdByフィールドが存在する")
    void shouldHaveCreatedByField() {
      // Arrange & Act
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "製品", 100L);

      // Assert
      assertThat(entity.getCreatedBy()).isNull();
    }

    @Test
    @DisplayName("createdByが設定・取得できる")
    void shouldSetAndGetCreatedBy() {
      // Arrange
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "製品", 100L);

      // Act
      entity.setCreatedBy(100L);

      // Assert
      assertThat(entity.getCreatedBy()).isEqualTo(100L);
    }
  }

  @Nested
  @DisplayName("継承階層のテスト")
  class InheritanceHierarchyTest {

    @Test
    @DisplayName("SampleProductEntityはSampleCategoryEntityのサブクラスである")
    void shouldBeSubclassOfSampleCategoryEntity() {
      // Arrange & Act
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "製品", 100L);

      // Assert
      assertThat(entity).isInstanceOf(SampleCategoryEntity.class);
    }

    @Test
    @DisplayName("SampleProductEntityはSampleBusinessEntityのサブクラスである")
    void shouldBeSubclassOfSampleBusinessEntity() {
      // Arrange & Act
      SampleProductEntity entity = new SampleProductEntity(SnowflakeId.of(1L), "製品", 100L);

      // Assert
      assertThat(entity).isInstanceOf(SampleBusinessEntity.class);
    }
  }
}
