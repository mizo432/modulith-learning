package undecided.association.relationshipMgmt.orgRole.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code DepartmentTest} は、Departmentエンティティの単体テストクラスです。
 * <p>
 * C5修正で追加された{@code create()}ファクトリメソッドの動作を検証します。
 */
@Tag("small")
@DisplayName("Departmentエンティティの単体テスト")
class DepartmentTest {

  @Nested
  @DisplayName("createメソッドのテスト")
  class CreateMethodTest {

    @Test
    @DisplayName("有効な引数でDepartmentインスタンスが作成されること")
    void shouldCreateDepartmentWithValidArguments() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(3000000001L);
      DepartmentCode code = new DepartmentCode("DEPT001");

      // Act
      Department department = Department.create(id, code);

      // Assert
      assertThat(department).as("Departmentインスタンスが作成されること").isNotNull();
      assertThat(department.getId()).isEqualTo(id);
      assertThat(department.getCode()).isEqualTo(code);
    }

    @Test
    @DisplayName("異なるIDで複数のDepartmentが作成できること")
    void shouldCreateMultipleDepartmentsWithDifferentIds() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(3000000001L);
      SnowflakeId id2 = SnowflakeId.of(3000000002L);
      DepartmentCode code1 = new DepartmentCode("DEPT001");
      DepartmentCode code2 = new DepartmentCode("DEPT002");

      // Act
      Department dept1 = Department.create(id1, code1);
      Department dept2 = Department.create(id2, code2);

      // Assert
      assertThat(dept1.getId()).isNotEqualTo(dept2.getId());
      assertThat(dept1.getCode()).isNotEqualTo(dept2.getCode());
    }
  }

  @Nested
  @DisplayName("エンティティアノテーションのテスト")
  class EntityAnnotationTest {

    @Test
    @DisplayName("Departmentクラスに@Entityアノテーションが付与されていること")
    void shouldHaveEntityAnnotation() {
      // Assert
      assertThat(Department.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
    }
  }
}
