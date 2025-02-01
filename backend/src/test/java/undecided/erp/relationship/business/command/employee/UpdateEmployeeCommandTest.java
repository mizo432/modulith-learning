package undecided.erp.relationship.business.command.employee;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.EmployeeRepository;

@SpringBootTest
@DisplayName("UpdateEmployeeCommandのテスト")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UpdateEmployeeCommandTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private UpdateEmployeeCommand updateEmployeeCommand;

  UpdateEmployeeCommandTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("executeメソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("従業員IDがnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmployeeIdIsNull() {
      // Given
      Long employeeId = null;
      Employee employee = new Employee();
      employee.setName("John Doe");
      employee.setKanaName("ジョン ドウ");
      employee.setInitials("JD");

      // When / Then
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> updateEmployeeCommand.execute(employeeId, employee))
          .withMessage("EmployeeId must not be null");
    }

    @Test
    @DisplayName("従業員IDが負の値の場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmployeeIdIsNegative() {
      // Given
      Long employeeId = -1L;
      Employee employee = new Employee();
      employee.setName("John Doe");
      employee.setKanaName("ジョン ドウ");
      employee.setInitials("JD");

      // When / Then
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> updateEmployeeCommand.execute(employeeId, employee))
          .withMessage("EmployeeId must be positive");
    }

    @Test
    @DisplayName("従業員オブジェクトがnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmployeeIsNull() {
      // Given
      Long employeeId = 1L;
      Employee employee = null;

      // When / Then
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> updateEmployeeCommand.execute(employeeId, employee))
          .withMessage("Employee must not be null");
    }

    @Test
    @DisplayName("従業員IDがデータベースに存在しない場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmployeeIdNotFoundInDatabase() {
      // Given
      Long employeeId = 1L;
      Employee employee = new Employee();
      employee.setName("John Doe");
      employee.setKanaName("ジョン ドウ");
      employee.setInitials("JD");

      when(employeeRepository.existsById(employeeId)).thenReturn(false);

      // When / Then
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> updateEmployeeCommand.execute(employeeId, employee))
          .withMessage("Employee not found. employeeId: " + employeeId);
    }
  }
}
