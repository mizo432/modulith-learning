package undecided.erp.relationship.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.relationship.business.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.partyRole.employee.Employee;

@RestController
@RequestMapping("relationship/api/v1/employees")
public class EmployeeApi {

  private final EmployeeQuery employeeQuery;

  public EmployeeApi(EmployeeQuery employeeQuery) {
    this.employeeQuery = employeeQuery;
  }

  @RequestMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long employeeId) {
    return ResponseEntity.of(employeeQuery.findByEmployeeId(employeeId));

  }

}
