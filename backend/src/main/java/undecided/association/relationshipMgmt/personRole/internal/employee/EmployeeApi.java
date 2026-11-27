package undecided.association.relationshipMgmt.personRole.internal.employee;
import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.association.relationshipMgmt.personRole.spi.employee.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApi {

  private final Clock clock;

  public EmployeeApi(Clock clock) {
    this.clock = clock;
  }
  @GetMapping
  Employee get() {
    return Employee.create(
        undecided.supporting.snowflake.SnowflakeId.of(1L),
        "",
        "",
        LocalDateTime.now(clock),
        null);
  }
}
