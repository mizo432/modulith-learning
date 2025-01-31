package undecided.erp.relationship.business.command.employee;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.partyRole.employee.EmployeeRepository;

@DisplayName("DropEmployeeCommandTest クラスのテスト")
class DropEmployeeCommandTest {

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteMethodTest {

    @Test
    @DisplayName("有効な employeeId を持つ従業員が存在するとき、従業員を削除するべき")
    void shouldDeleteEmployeeWhenEmployeeExists() {
      // Arrange
      Long validEmployeeId = 1L;
      EmployeeRepository mockRepository = mock(EmployeeRepository.class);
      DropEmployeeCommand command = new DropEmployeeCommand(mockRepository);

      when(mockRepository.existsById(validEmployeeId)).thenReturn(true);

      // Act & Assert
      assertThatNoException().isThrownBy(() -> command.execute(validEmployeeId));
      verify(mockRepository, times(1)).deleteEmployeeByEmployeeId(validEmployeeId);
    }

    @Test
    @DisplayName("有効な employeeId が存在しないとき、例外をスローするべき")
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {
      // Arrange
      Long nonExistingEmployeeId = 1L;
      EmployeeRepository mockRepository = mock(EmployeeRepository.class);
      DropEmployeeCommand command = new DropEmployeeCommand(mockRepository);

      when(mockRepository.existsById(nonExistingEmployeeId)).thenReturn(false);

      // Act & Assert
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> command.execute(nonExistingEmployeeId))
          .withMessage("Employee not found. employeeId: " + nonExistingEmployeeId);

      verify(mockRepository, never()).deleteEmployeeByEmployeeId(nonExistingEmployeeId);
    }

    @Test
    @DisplayName("null の employeeId を渡したとき、例外をスローするべき")
    void shouldThrowExceptionWhenEmployeeIdIsNull() {
      // Arrange
      EmployeeRepository mockRepository = mock(EmployeeRepository.class);
      DropEmployeeCommand command = new DropEmployeeCommand(mockRepository);

      // Act & Assert
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> command.execute(null))
          .withMessage("EmployeeId must not be null");

      verify(mockRepository, never()).deleteEmployeeByEmployeeId(any());
    }

    @Test
    @DisplayName("負の値の employeeId を渡したとき、例外をスローするべき")
    void shouldThrowExceptionWhenEmployeeIdIsNegative() {
      // Arrange
      Long negativeEmployeeId = -1L;
      EmployeeRepository mockRepository = mock(EmployeeRepository.class);
      DropEmployeeCommand command = new DropEmployeeCommand(mockRepository);

      // Act & Assert
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> command.execute(negativeEmployeeId))
          .withMessage("EmployeeId must be positive");

      verify(mockRepository, never()).deleteEmployeeByEmployeeId(any());
    }

    @Test
    @DisplayName("employeeId が 0 のとき、例外をスローするべき")
    void shouldThrowExceptionWhenEmployeeIdIsZero() {
      // Arrange
      Long zeroEmployeeId = 0L;
      EmployeeRepository mockRepository = mock(EmployeeRepository.class);
      DropEmployeeCommand command = new DropEmployeeCommand(mockRepository);

      // Act & Assert
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> command.execute(zeroEmployeeId))
          .withMessage("EmployeeId must be positive");

      verify(mockRepository, never()).deleteEmployeeByEmployeeId(any());
    }
  }
}
