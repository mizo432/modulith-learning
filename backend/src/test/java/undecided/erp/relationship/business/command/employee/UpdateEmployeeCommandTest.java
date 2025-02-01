package undecided.erp.relationship.business.command.employee;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.EmployeeRepository;

@DataJpaTest
@DisplayName("UpdateEmployeeCommandのテスト")
class UpdateEmployeeCommandTest {

  @Autowired
  private UpdateEmployeeCommand updateEmployeeCommand;

  @MockitoBean
  private EmployeeRepository employeeRepository;

  @Nested
  @DisplayName("executeメソッドのテスト")
  class ExecuteMethodTest {

    @Test
    @DisplayName("正しいIDとEmployeeを渡した場合、例外が発生しないこと")
    void shouldExecuteSuccessfullyWhenValidIdAndEmployeeProvided() {
      // Setup
      Employee employee = new Employee(1L, "John Doe", "JD", "ジョン ドウ");
      long employeeId = 1L;

      when(employeeRepository.existsById(employeeId)).thenReturn(true);
      when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

      // Act & Assert
      assertThatNoException().isThrownBy(() -> updateEmployeeCommand.execute(employeeId, employee));

      // Verify
      verify(employeeRepository, times(1)).existsById(employeeId);
      verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    @DisplayName("存在しないIDを渡した場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowIllegalArgumentExceptionWhenEmployeeIdNotFound() {
      // Setup
      Employee employee = new Employee(1L, "John Doe", "JD", "ジョン ドウ");
      long employeeId = 1L;

      when(employeeRepository.existsById(employeeId)).thenReturn(false);

      // Act & Assert
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> updateEmployeeCommand.execute(employeeId, employee))
          .withMessageContaining("Employee not found. employeeId: " + employeeId);

      // Verify
      verify(employeeRepository, times(1)).existsById(employeeId);
      verify(employeeRepository, never()).save(employee);
    }

    @Test
    @DisplayName("employeeIdがnullの場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowIllegalArgumentExceptionWhenEmployeeIdIsNull() {
      // Setup
      Employee employee = new Employee(1L, "John Doe", "JD", "ジョン ドウ");

      // Act & Assert
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> updateEmployeeCommand.execute(null, employee))
          .withMessageContaining("EmployeeId must be positive");

      // Verify
      verify(employeeRepository, never()).existsById(any());
      verify(employeeRepository, never()).save(employee);
    }

    @Test
    @DisplayName("Employeeがnullの場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowIllegalArgumentExceptionWhenEmployeeIsNull() {
      // Setup
      long employeeId = 1L;

      // Act & Assert
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> updateEmployeeCommand.execute(employeeId, null));

      // Verify
      verify(employeeRepository, never()).existsById(any());
      verify(employeeRepository, never()).save(any());
    }
  }
}
