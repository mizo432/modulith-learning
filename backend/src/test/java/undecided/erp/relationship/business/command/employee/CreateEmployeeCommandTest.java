package undecided.erp.relationship.business.command.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.EmployeeRepository;

@SpringBootTest
@Import(CreateEmployeeCommand.class)
@DisplayName("CreateEmployeeCommand のテスト")
class CreateEmployeeCommandTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private CreateEmployeeCommand createEmployeeCommand;

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTests {

    @Test
    @DisplayName("正常な従業員データを保存する")
    void shouldSaveValidEmployee() {
      // Arrange
      Employee employee = new Employee(
          1L,
          "John Doe",
          "JD",
          "ジョン ドウ"
      );

      // Act
      Employee savedEmployee = createEmployeeCommand.execute(employee);

      // Assert
      assertThat(savedEmployee).isNotNull();
      assertThat(savedEmployee.getEmployeeId()).isEqualTo(1L);
      assertThat(savedEmployee.getName()).isEqualTo("John Doe");
      assertThat(savedEmployee.getInitials()).isEqualTo("JD");
      assertThat(savedEmployee.getKanaName()).isEqualTo("ジョン ドウ");
    }

    @Test
    @DisplayName("引数が null の場合に例外がスローされる")
    void shouldThrowExceptionWhenEmployeeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> createEmployeeCommand.execute(null))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("無効な従業員データのテスト")
  class InvalidEmployeeTest {

    @Test
    @DisplayName("名前が null の場合に例外がスローされる")
    void shouldThrowExceptionWhenNameIsNull() {
      // Arrange
      Employee employee = new Employee(
          1L,
          null,
          "JD",
          "ジョン ドウ"
      );

      // Act & Assert
      assertThatThrownBy(() -> createEmployeeCommand.execute(employee))
          .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("イニシャルが null の場合に例外がスローされる")
    void shouldThrowExceptionWhenInitialsAreNull() {
      // Arrange
      Employee employee = new Employee(
          1L,
          "John Doe",
          null,
          "ジョン ドウ"
      );

      // Act & Assert
      assertThatThrownBy(() -> createEmployeeCommand.execute(employee))
          .isInstanceOf(Exception.class);
    }
  }

  @Nested
  @DisplayName("有効な従業員データのテスト")
  class ValidEmployeeTests {

    @Test
    @DisplayName("異なる名前とイニシャルの従業員を保存する")
    void shouldSaveEmployeeWithDifferentAttributes() {
      // Arrange
      Employee employee = new Employee(
          2L,
          "Jane Smith",
          "JS",
          "ジェーン スミス"
      );

      // Act
      Employee savedEmployee = createEmployeeCommand.execute(employee);

      // Assert
      assertThat(savedEmployee).isNotNull();
      assertThat(savedEmployee.getEmployeeId()).isEqualTo(2L);
      assertThat(savedEmployee.getName()).isEqualTo("Jane Smith");
      assertThat(savedEmployee.getInitials()).isEqualTo("JS");
      assertThat(savedEmployee.getKanaName()).isEqualTo("ジェーン スミス");
    }
  }
}
