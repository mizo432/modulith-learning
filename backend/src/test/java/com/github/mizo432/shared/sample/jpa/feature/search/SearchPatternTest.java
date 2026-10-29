package com.github.mizo432.shared.sample.jpa.feature.search;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("検索パターンの単体テスト")
class SearchPatternTest {

  @Nested
  @DisplayName("SampleProductSearchエンティティのテスト")
  class SampleProductSearchTest {

    @Test
    @DisplayName("SampleProductSearchが正常に作成される")
    void shouldCreateSampleProductSearch() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(1L);
      String name = "テスト製品";
      String category = "電子機器";
      Long price = 10000L;

      // Act
      SampleProductSearch product = new SampleProductSearch(id, name, category, price);

      // Assert
      assertThat(product).isNotNull();
      assertThat(product.getProductId()).isEqualTo(id);
      assertThat(product.getName()).isEqualTo(name);
      assertThat(product.getCategory()).isEqualTo(category);
      assertThat(product.getPrice()).isEqualTo(price);
    }
  }

  @Nested
  @DisplayName("メソッド名派生クエリのテスト")
  class DerivedQueryMethodTest {

    @Test
    @DisplayName("findByNameStartingWithメソッドが存在する")
    void shouldHaveFindByNameStartingWithMethod() {
      // Arrange & Act
      boolean hasMethod =
          Arrays.stream(SampleProductSearchRepository.class.getMethods())
              .map(Method::getName)
              .anyMatch(name -> name.equals("findByNameStartingWith"));

      // Assert
      assertThat(hasMethod).isTrue();
    }

    @Test
    @DisplayName("findByNameContainingメソッドが存在する")
    void shouldHaveFindByNameContainingMethod() {
      // Arrange & Act
      boolean hasMethod =
          Arrays.stream(SampleProductSearchRepository.class.getMethods())
              .map(Method::getName)
              .anyMatch(name -> name.equals("findByNameContaining"));

      // Assert
      assertThat(hasMethod).isTrue();
    }

    @Test
    @DisplayName("findByNameEndingWithメソッドが存在する")
    void shouldHaveFindByNameEndingWithMethod() {
      // Arrange & Act
      boolean hasMethod =
          Arrays.stream(SampleProductSearchRepository.class.getMethods())
              .map(Method::getName)
              .anyMatch(name -> name.equals("findByNameEndingWith"));

      // Assert
      assertThat(hasMethod).isTrue();
    }

    @Test
    @DisplayName("複数フィールドの組み合わせ検索メソッドが存在する")
    void shouldHaveCombinedSearchMethod() {
      // Arrange & Act
      boolean hasMethod =
          Arrays.stream(SampleProductSearchRepository.class.getMethods())
              .map(Method::getName)
              .anyMatch(name -> name.equals("findByNameContainingAndCategoryContaining"));

      // Assert
      assertThat(hasMethod).isTrue();
    }
  }

  @Nested
  @DisplayName("JPQL LIKE検索のテスト")
  class JpqlLikeSearchTest {

    @Test
    @DisplayName("findByNameContainingIgnoreCaseは@Queryアノテーションを持つ")
    void shouldHaveQueryAnnotationOnFindByNameContainingIgnoreCase() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleProductSearchRepository.class.getMethod(
              "findByNameContainingIgnoreCase", String.class);

      // Act & Assert
      Query query = method.getAnnotation(Query.class);
      assertThat(query).isNotNull();
      assertThat(query.value()).contains("LOWER");
      assertThat(query.value()).contains("LIKE");
    }
  }

  @Nested
  @DisplayName("Specificationパターンのテスト")
  class SpecificationPatternTest {

    @Test
    @DisplayName("SampleProductSearchRepositoryはJpaSpecificationExecutorを実装している")
    void shouldImplementJpaSpecificationExecutor() {
      // Arrange & Act
      Class<?>[] interfaces = SampleProductSearchRepository.class.getInterfaces();

      // Assert
      assertThat(interfaces).contains(JpaSpecificationExecutor.class);
    }

    @Test
    @DisplayName("nameStartsWith Specificationが作成される")
    void shouldCreateNameStartsWithSpecification() {
      // Arrange & Act
      Specification<SampleProductSearch> spec = SampleProductSearch.nameStartsWith("テ");

      // Assert
      assertThat(spec).isNotNull();
    }

    @Test
    @DisplayName("nameContains Specificationが作成される")
    void shouldCreateNameContainsSpecification() {
      // Arrange & Act
      Specification<SampleProductSearch> spec = SampleProductSearch.nameContains("製品");

      // Assert
      assertThat(spec).isNotNull();
    }

    @Test
    @DisplayName("categoryEquals Specificationが作成される")
    void shouldCreateCategoryEqualsSpecification() {
      // Arrange & Act
      Specification<SampleProductSearch> spec = SampleProductSearch.categoryEquals("電子機器");

      // Assert
      assertThat(spec).isNotNull();
    }

    @Test
    @DisplayName("priceBetween Specificationが作成される")
    void shouldCreatePriceBetweenSpecification() {
      // Arrange & Act
      Specification<SampleProductSearch> spec = SampleProductSearch.priceBetween(1000L, 10000L);

      // Assert
      assertThat(spec).isNotNull();
    }
  }
}
