package com.github.mizo432.shared.sample.jpa.feature.query;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Tag("small")
@DisplayName("@Query/@Modifyingパターンの単体テスト")
class QueryModifyingPatternTest {

  @Nested
  @DisplayName("SampleHolidayエンティティのテスト")
  class SampleHolidayTest {

    @Test
    @DisplayName("SampleHolidayが正常に作成される")
    void shouldCreateSampleHoliday() {
      // Arrange
      Long id = 1L;
      java.time.LocalDate date = java.time.LocalDate.of(2024, 1, 1);
      String name = "元日";

      // Act
      SampleHoliday holiday = new SampleHoliday(id, date, name);

      // Assert
      assertThat(holiday).isNotNull();
      assertThat(holiday.getId()).isEqualTo(id);
      assertThat(holiday.getHolidayDate()).isEqualTo(date);
      assertThat(holiday.getHolidayName()).isEqualTo(name);
      assertThat(holiday.getActive()).isTrue();
    }
  }

  @Nested
  @DisplayName("SampleHolidayRepositoryの@Queryメソッドのテスト")
  class QueryMethodTest {

    @Test
    @DisplayName("findByHolidayDateBetweenメソッドに@Queryアノテーションが付与されている")
    void shouldHaveQueryAnnotationOnFindByHolidayDateBetween() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleHolidayRepository.class.getMethod(
              "findByHolidayDateBetween", java.time.LocalDate.class, java.time.LocalDate.class);

      // Act & Assert
      assertThat(method.getAnnotation(Query.class)).isNotNull();
      Query query = method.getAnnotation(Query.class);
      assertThat(query.value()).contains("SampleHoliday");
      assertThat(query.value()).contains("ORDER BY");
    }

    @Test
    @DisplayName("findActiveHolidaysメソッドに@Queryアノテーションが付与されている")
    void shouldHaveQueryAnnotationOnFindActiveHolidays() throws NoSuchMethodException {
      // Arrange
      Method method = SampleHolidayRepository.class.getMethod("findActiveHolidays");

      // Act & Assert
      assertThat(method.getAnnotation(Query.class)).isNotNull();
      Query query = method.getAnnotation(Query.class);
      assertThat(query.value()).contains("active = true");
    }

    @Test
    @DisplayName("findByHolidayNameContainingメソッドに@Paramアノテーションが付与されている")
    void shouldHaveParamAnnotationOnFindByHolidayNameContaining() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleHolidayRepository.class.getMethod("findByHolidayNameContaining", String.class);

      // Act
      Param param = method.getParameters()[0].getAnnotation(Param.class);

      // Assert
      assertThat(param).isNotNull();
      assertThat(param.value()).isEqualTo("keyword");
    }

    @Test
    @DisplayName("countActiveHolidaysはネイティブクエリを使用している")
    void shouldUseNativeQueryForCountActiveHolidays() throws NoSuchMethodException {
      // Arrange
      Method method = SampleHolidayRepository.class.getMethod("countActiveHolidays");

      // Act & Assert
      Query query = method.getAnnotation(Query.class);
      assertThat(query).isNotNull();
      assertThat(query.nativeQuery()).isTrue();
    }
  }

  @Nested
  @DisplayName("SampleHolidayRepositoryの@Modifyingメソッドのテスト")
  class ModifyingMethodTest {

    @Test
    @DisplayName("deleteByIdNotメソッドに@Modifyingアノテーションが付与されている")
    void shouldHaveModifyingAnnotationOnDeleteByIdNot() throws NoSuchMethodException {
      // Arrange
      Method method = SampleHolidayRepository.class.getMethod("deleteByIdNot", Long.class);

      // Act & Assert
      assertThat(method.getAnnotation(Modifying.class)).isNotNull();
      assertThat(method.getAnnotation(Query.class).value()).startsWith("DELETE");
    }

    @Test
    @DisplayName("deactivateBeforeメソッドに@Modifyingアノテーションが付与されている")
    void shouldHaveModifyingAnnotationOnDeactivateBefore() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleHolidayRepository.class.getMethod("deactivateBefore", java.time.LocalDate.class);

      // Act & Assert
      assertThat(method.getAnnotation(Modifying.class)).isNotNull();
      assertThat(method.getAnnotation(Query.class).value()).startsWith("UPDATE");
    }

    @Test
    @DisplayName("@Modifyingメソッドの戻り値はint型である")
    void shouldModifyingMethodsReturnInt() throws NoSuchMethodException {
      // Arrange
      Method deleteMethod = SampleHolidayRepository.class.getMethod("deleteByIdNot", Long.class);
      Method updateMethod =
          SampleHolidayRepository.class.getMethod("deactivateBefore", java.time.LocalDate.class);

      // Assert
      assertThat(deleteMethod.getReturnType()).isEqualTo(int.class);
      assertThat(updateMethod.getReturnType()).isEqualTo(int.class);
    }
  }

  @Nested
  @DisplayName("クエリメソッドの一覧のテスト")
  class QueryMethodListTest {

    @Test
    @DisplayName("@Queryアノテーションが付与されたメソッドが6つ存在する")
    void shouldHaveSixQueryMethods() {
      // Arrange & Act
      long queryMethodCount =
          Arrays.stream(SampleHolidayRepository.class.getDeclaredMethods())
              .filter(m -> m.isAnnotationPresent(Query.class))
              .count();

      // Assert
      assertThat(queryMethodCount).isEqualTo(6);
    }

    @Test
    @DisplayName("@Modifyingアノテーションが付与されたメソッドが2つ存在する")
    void shouldHaveTwoModifyingMethods() {
      // Arrange & Act
      long modifyingMethodCount =
          Arrays.stream(SampleHolidayRepository.class.getDeclaredMethods())
              .filter(m -> m.isAnnotationPresent(Modifying.class))
              .count();

      // Assert
      assertThat(modifyingMethodCount).isEqualTo(2);
    }
  }
}
