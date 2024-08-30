package undecided.erp.relationship.domain.model.personRole.employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.entity.SnowflakeId;

@DisplayName("Employeeクラスのテスト")
class EmployeeTest {

  @Nested
  @DisplayName("reconstructメソッドのテスト")
  class ReconstructTest {

    @Test
    @DisplayName("有効な引数の場合")
    void shouldReturnEmployeeWhenArgsValid() {
      Long id = 1L;
      String employeeNo = "Test";
      Employee result = Employee.reconstruct(id, employeeNo);
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(SnowflakeId.reconstruct(id));
      assertThat(result.getEmployeeNo()).isEqualTo(EmployeeNo.reconstruct(employeeNo));
    }

    @Test
    @DisplayName("IDがnullの場合")
    void shouldThrowExceptionWhenIdIsNull() {
      String employeeNo = "Test";
      Employee result = Employee.reconstruct(null, employeeNo);
      assertThat(result.getId()).isEqualTo(SnowflakeId.reconstruct(null));
      assertThat(result.getEmployeeNo()).isEqualTo(EmployeeNo.reconstruct(employeeNo));
    }

    @Test
    @DisplayName("EmployeeNoがnullの場合")
    void shouldThrowExceptionWhenEmployeeNoIsNull() {
      Long id = 1L;
      Employee result = Employee.reconstruct(id, null);
      assertThat(result.getId()).isEqualTo(SnowflakeId.reconstruct(id));
      assertThat(result.getEmployeeNo()).isEqualTo(EmployeeNo.reconstruct(null));
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("有効な引数の場合")
    void shouldReturnCorrectStringWhenArgsValid() {
      Long id = 1L;
      String employeeNo = "Test";
      Employee result = Employee.reconstruct(id, employeeNo);
      String expected =
          "Employee{id=" + SnowflakeId.reconstruct(id) + ", employeeNo=" + EmployeeNo.reconstruct(
              employeeNo) + "}";
      assertThat(result.toString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("引数がnullの場合")
    void shouldReturnCorrectStringWhenArgsAreNull() {
      Employee result = Employee.reconstruct(null, null);
      String expected =
          "Employee{id=" + SnowflakeId.empty() + ", employeeNo=" + EmployeeNo.EMPTY + "}";
      assertThat(result.toString()).isEqualTo(expected);
    }
  }
}
