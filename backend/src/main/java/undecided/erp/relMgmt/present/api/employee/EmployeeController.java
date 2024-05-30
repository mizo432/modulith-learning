package undecided.erp.relMgmt.present.api.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.relMgmt.EmployeeDto;
import undecided.erp.relMgmt.model.party.party.Party;
import undecided.erp.relMgmt.spi.EmployeeQuery;
import undecided.erp.shared.entity.SnowflakeId;

@RestController
@RequestMapping(path = "/rel")
public class EmployeeController {

  private final EmployeeQuery employeeQuery;

  public EmployeeController(EmployeeQuery employeeQuery) {
    this.employeeQuery = employeeQuery;
  }

  @GetMapping(path = "/api/v1/employees/{employeeId}")
  ResponseEntity<EmployeeDto> findOneById(@PathVariable("employeeId") SnowflakeId<Party> id) {
    return ResponseEntity.ok(employeeQuery.findById(id));

  }
}
