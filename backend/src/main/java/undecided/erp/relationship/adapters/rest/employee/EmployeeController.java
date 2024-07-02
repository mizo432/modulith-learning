package undecided.erp.relationship.adapters.rest.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.relationship.application.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

@RestController
@RequestMapping(path = "/relationship")
public class EmployeeController {

  private final EmployeeQuery employeeQuery;

  public EmployeeController(EmployeeQuery employeeQuery) {
    this.employeeQuery = employeeQuery;
  }

  @GetMapping(path = "/api/v1/employees/{employeeId}")
  ResponseEntity<Employee> findOneById(@PathVariable("employeeId") SnowflakeId<Party> id) {
    return ResponseEntity.ok(employeeQuery.findById(id));

  }
}
