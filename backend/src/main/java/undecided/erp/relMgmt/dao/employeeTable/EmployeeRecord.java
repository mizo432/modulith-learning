package undecided.erp.relMgmt.dao.employeeTable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import undecided.erp.relMgmt.model.personRole.employee.Employee;

@Data
public class EmployeeRecord {

  @Id
  private Long id;

  @Column(nullable = false, length = 30)
  private String employeeNo;

  static EmployeeRecord fromEntity(Employee employee) {
    return new EmployeeRecord();
  }

}
