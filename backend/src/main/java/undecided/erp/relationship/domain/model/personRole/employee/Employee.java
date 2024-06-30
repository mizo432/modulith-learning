package undecided.erp.relationship.domain.model.personRole.employee;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import undecided.erp.shared.entity.SnowflakeId;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "create")
@Getter
public class Employee {

  private final SnowflakeId<Employee> id;
  private final EmployeeNo employeeNo;
  private final String firstName;
  private final String lastName;

  public static Employee reconstruct(Long id, String employeeNo) {
    return null;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "id=" + id +
        ", employeeNo=" + employeeNo +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Employee employee = (Employee) o;
    return Objects.equals(id, employee.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
