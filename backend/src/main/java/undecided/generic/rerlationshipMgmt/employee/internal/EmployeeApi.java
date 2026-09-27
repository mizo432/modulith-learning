package undecided.generic.rerlationshipMgmt.employee.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.generic.rerlationshipMgmt.employee.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApi {
  @GetMapping
  Employee get() {
    return new Employee();
  }
}
