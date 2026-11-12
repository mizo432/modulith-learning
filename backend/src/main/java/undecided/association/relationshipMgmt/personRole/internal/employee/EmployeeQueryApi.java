package undecided.association.relationshipMgmt.personRole.internal.employee;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;

@RestController
@RequestMapping("/api/employees")
public class EmployeeQueryApi {
  @GetMapping
  EmployeeDto get() {
    return new EmployeeDto(null, null, null, null);
  }
}
