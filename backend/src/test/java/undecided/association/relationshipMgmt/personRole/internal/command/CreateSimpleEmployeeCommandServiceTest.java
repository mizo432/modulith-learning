package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("CreateSimpleEmployeeCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateSimpleEmployeeCommandServiceTest {

  @Mock private EmployeeFactory employeeFactory;

  @Mock private EmployeeRepository employeeRepository;

  private CreateSimpleEmployeeCommandService service;

  @BeforeEach
  void setUp() {
    service = new CreateSimpleEmployeeCommandService(employeeFactory, employeeRepository);
  }

  @Nested
  @DisplayName("create メソッドのテスト")
  class CreateTest {

    @Test
    @DisplayName("EmployeeDtoからSimpleEmployeeを作成してRepositoryに保存すること")
    void shouldCreateAndSaveEmployee() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName);

      SimpleEmployee simpleEmployee = new SimpleEmployee();
      simpleEmployee.setEmployeeId(employeeId);
      simpleEmployee.setEmployeeNo(employeeNo);
      simpleEmployee.setSearchName(searchName);
      simpleEmployee.setFullName(fullName);

      when(employeeFactory.create(dto)).thenReturn(simpleEmployee);

      // Act
      service.create(dto);

      // Assert
      verify(employeeFactory).create(dto);
      verify(employeeRepository).save(simpleEmployee);
    }

    @Test
    @DisplayName("Factoryで作成されたSimpleEmployeeがRepositoryに保存されること")
    void shouldSaveEmployeeCreatedByIdFactory() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(9876543210L);
      EmployeeNo employeeNo = EmployeeNo.of("0987654321");
      SearchName searchName = SearchName.of("佐藤花子");
      FullName fullName = FullName.of("佐藤花子");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName);

      SimpleEmployee simpleEmployee = new SimpleEmployee();
      simpleEmployee.setEmployeeId(employeeId);
      simpleEmployee.setEmployeeNo(employeeNo);
      simpleEmployee.setSearchName(searchName);
      simpleEmployee.setFullName(fullName);

      when(employeeFactory.create(dto)).thenReturn(simpleEmployee);

      // Act
      service.create(dto);

      // Assert
      verify(employeeRepository).save(any(SimpleEmployee.class));
    }
  }
}
