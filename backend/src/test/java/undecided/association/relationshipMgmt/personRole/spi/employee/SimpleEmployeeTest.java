package undecided.association.relationshipMgmt.personRole.spi.employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code SimpleEmployeeTest} は、SimpleEmployeeエンティティの単体テストクラスです。
 * <p>
 * C6修正で追加された{@code create()}ファクトリメソッドの動作を検証します。
 */
@Tag("small")
@DisplayName("SimpleEmployeeエンティティの単体テスト")
class SimpleEmployeeTest {

  @Nested
  @DisplayName("createメソッドのテスト")
  class CreateMethodTest {

    @Test
    @DisplayName("有効な引数でSimpleEmployeeインスタンスが作成されること")
    void shouldCreateSimpleEmployeeWithValidArguments() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(4000000001L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");

      // Act
      SimpleEmployee employee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      // Assert
      assertThat(employee).as("SimpleEmployeeインスタンスが作成されること").isNotNull();
      assertThat(employee.getEmployeeId()).isEqualTo(employeeId);
      assertThat(employee.getEmployeeNo()).isEqualTo(employeeNo);
      assertThat(employee.getSearchName()).isEqualTo(searchName);
      assertThat(employee.getFullName()).isEqualTo(fullName);
    }

    @Test
    @DisplayName("日本語の氏名で正常に作成されること")
    void shouldCreateSimpleEmployeeWithJapaneseName() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(4000000001L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");

      // Act
      SimpleEmployee employee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      // Assert
      assertThat(employee.getSearchName().getValue()).isEqualTo("田中太郎");
      assertThat(employee.getFullName().getValue()).isEqualTo("田中太郎");
    }

    @Test
    @DisplayName("異なるIDで複数のSimpleEmployeeが作成できること")
    void shouldCreateMultipleSimpleEmployeesWithDifferentIds() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(4000000001L);
      SnowflakeId id2 = SnowflakeId.of(4000000002L);
      EmployeeNo empNo1 = EmployeeNo.of("1234567890");
      EmployeeNo empNo2 = EmployeeNo.of("0987654321");
      SearchName searchName1 = SearchName.of("田中太郎");
      SearchName searchName2 = SearchName.of("鈴木花子");
      FullName fullName1 = FullName.of("田中太郎");
      FullName fullName2 = FullName.of("鈴木花子");

      // Act
      SimpleEmployee emp1 = SimpleEmployee.create(id1, empNo1, searchName1, fullName1);
      SimpleEmployee emp2 = SimpleEmployee.create(id2, empNo2, searchName2, fullName2);

      // Assert
      assertThat(emp1.getEmployeeId()).isNotEqualTo(emp2.getEmployeeId());
      assertThat(emp1.getEmployeeNo()).isNotEqualTo(emp2.getEmployeeNo());
    }
  }

  @Nested
  @DisplayName("エンティティアノテーションのテスト")
  class EntityAnnotationTest {

    @Test
    @DisplayName("SimpleEmployeeクラスに@Entityアノテーションが付与されていること")
    void shouldHaveEntityAnnotation() {
      // Assert
      assertThat(SimpleEmployee.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
    }

    @Test
    @DisplayName("テーブル名がemployeesであること")
    void shouldHaveCorrectTableName() {
      // Assert
      jakarta.persistence.Table table = SimpleEmployee.class.getAnnotation(jakarta.persistence.Table.class);
      assertThat(table).isNotNull();
      assertThat(table.name()).isEqualTo("employees");
    }
  }
}
