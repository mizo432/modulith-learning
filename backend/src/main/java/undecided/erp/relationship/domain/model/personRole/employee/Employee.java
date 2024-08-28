package undecided.erp.relationship.domain.model.personRole.employee;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.shared.entity.SnowflakeId;

@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "create")
@NoArgsConstructor
@Getter
public class Employee {

  public static final Employee EMPTY = new Employee();

  private SnowflakeId id = SnowflakeId.empty();
  private EmployeeNo employeeNo = EmployeeNo.EMPTY;
  private String firstName;
  private String lastName;

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
