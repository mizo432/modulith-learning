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

  /**
   * 指定されたidと社員番号からEmployeeオブジェクトを再構築します。
   *
   * @param id SnowflakeIdオブジェクトを再構築するためのid
   * @param employeeNo EmployeeNoオブジェクトを再構築するための社員番号
   * @return 再構築されたEmployeeオブジェクト
   */
  public static Employee reconstruct(Long id, String employeeNo) {
    return new Employee(SnowflakeId.reconstruct(id), EmployeeNo.reconstruct(employeeNo));

  }

  @Override
  public String toString() {
    return "Employee{" +
        "id=" + id +
        ", employeeNo=" + employeeNo +
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
