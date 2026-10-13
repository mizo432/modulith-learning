package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("EmployeeFactoryImplの単体テスト")
class EmployeeFactoryImplTest {

  private final EmployeeFactoryImpl factory = new EmployeeFactoryImpl();

  @Nested
  @DisplayName("create メソッドのテスト")
  class CreateTest {

    @Test
    @DisplayName("EmployeeDtoからSimpleEmployeeを正しく作成すること")
    void shouldCreateSimpleEmployeeFromDto() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName);

      // Act
      SimpleEmployee simpleEmployee = factory.create(dto);

      // Assert
      assertThat(simpleEmployee).isNotNull();
      assertThat(simpleEmployee.getEmployeeId()).isEqualTo(employeeId);
      assertThat(simpleEmployee.getEmployeeNo()).isEqualTo(employeeNo);
      assertThat(simpleEmployee.getSearchName()).isEqualTo(searchName);
      assertThat(simpleEmployee.getFullName()).isEqualTo(fullName);
    }

    @Test
    @DisplayName("異なる値を持つEmployeeDtoからSimpleEmployeeを作成すること")
    void shouldCreateSimpleEmployeeWithDifferentValues() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(9876543210L);
      EmployeeNo employeeNo = EmployeeNo.of("0987654321");
      SearchName searchName = SearchName.of("佐藤花子");
      FullName fullName = FullName.of("佐藤花子");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName);

      // Act
      SimpleEmployee simpleEmployee = factory.create(dto);

      // Assert
      assertThat(simpleEmployee).isNotNull();
      assertThat(simpleEmployee.getEmployeeId()).isEqualTo(employeeId);
      assertThat(simpleEmployee.getEmployeeNo()).isEqualTo(employeeNo);
      assertThat(simpleEmployee.getSearchName()).isEqualTo(searchName);
      assertThat(simpleEmployee.getFullName()).isEqualTo(fullName);
    }

    @Test
    @DisplayName("作成されたSimpleEmployeeは元のEmployeeDtoと独立したインスタンスであること")
    void shouldCreateIndependentInstance() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName);

      // Act
      SimpleEmployee simpleEmployee = factory.create(dto);

      // Assert
      assertThat(simpleEmployee).isNotSameAs(dto);
    }
  }
}
