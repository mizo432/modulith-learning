package undecided.shared.sample.jpa.feature.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

@Tag("small")
@DisplayName("リポジトリバリエーションの単体テスト")
class RepositoryVariationTest {

  @Nested
  @DisplayName("SampleUserRepositoryのテスト")
  class SampleUserRepositoryTest {

    @Test
    @DisplayName("SampleUserRepositoryはCrudRepositoryを拡張している")
    void shouldExtendCrudRepository() {
      // Arrange & Act
      Class<?>[] interfaces = SampleUserRepository.class.getInterfaces();

      // Assert
      assertThat(interfaces).contains(CrudRepository.class);
    }

    @Test
    @DisplayName("findByEmailメソッドが存在する")
    void shouldHaveFindByEmailMethod() {
      // Arrange & Act
      Method[] methods = SampleUserRepository.class.getMethods();
      boolean hasFindByEmail =
          Arrays.stream(methods).map(Method::getName).anyMatch(name -> name.equals("findByEmail"));

      // Assert
      assertThat(hasFindByEmail).isTrue();
    }

    @Test
    @DisplayName("findByNameAndActiveメソッドが存在する")
    void shouldHaveFindByNameAndActiveMethod() {
      // Arrange & Act
      Method[] methods = SampleUserRepository.class.getMethods();
      boolean hasFindByNameAndActive =
          Arrays.stream(methods)
              .map(Method::getName)
              .anyMatch(name -> name.equals("findByNameAndActive"));

      // Assert
      assertThat(hasFindByNameAndActive).isTrue();
    }
  }

  @Nested
  @DisplayName("SampleEmployeeRepositoryのテスト")
  class SampleEmployeeRepositoryTest {

    @Test
    @DisplayName("SampleEmployeeRepositoryはJpaRepositoryを拡張している")
    void shouldExtendJpaRepository() {
      // Arrange & Act
      Class<?>[] interfaces = SampleEmployeeRepository.class.getInterfaces();

      // Assert
      assertThat(interfaces).contains(JpaRepository.class);
    }

    @Test
    @DisplayName("JpaRepositoryはCrudRepositoryのサブタイプである")
    void shouldJpaRepositoryExtendCrudRepository() {
      // Arrange & Act & Assert
      assertThat(JpaRepository.class).isAssignableTo(CrudRepository.class);
    }

    @Test
    @DisplayName("メソッド名派生クエリメソッドが存在する")
    void shouldHaveDerivedQueryMethods() {
      // Arrange & Act
      Method[] methods = SampleEmployeeRepository.class.getMethods();
      var methodNames = Arrays.stream(methods).map(Method::getName).toList();

      // Assert
      assertThat(methodNames).contains("findByNameContaining", "findByDepartment");
      assertThat(methodNames).contains("findByHireDateBetween");
      assertThat(methodNames).contains("findByNameContainingAndDepartmentContaining");
    }
  }

  @Nested
  @DisplayName("リポジトリインターフェースの共通特性のテスト")
  class CommonRepositoryFeaturesTest {

    @Test
    @DisplayName("両方のリポジトリはRepositoryインターフェースを実装している")
    void shouldBothImplementRepositoryInterface() {
      // Arrange & Act & Assert
      assertThat(SampleUserRepository.class).isAssignableTo(Repository.class);
      assertThat(SampleEmployeeRepository.class).isAssignableTo(Repository.class);
    }
  }
}
