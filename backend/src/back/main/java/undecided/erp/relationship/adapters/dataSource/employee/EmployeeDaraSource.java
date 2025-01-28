package undecided.erp.relationship.adapters.dataSource.employee;

import java.util.Optional;
import org.springframework.stereotype.Component;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeRepository;
import undecided.erp.shared.entity.SnowflakeId;


@Component
public class EmployeeDaraSource implements EmployeeRepository {

  @Override
  public Optional<Employee> findById(SnowflakeId id) {
    return Optional.empty();
  }

  @Override
  public Employee save(Employee employee) {
    return null;
  }

  @Override
  public void insert(Employee employee) {

  }

  @Override
  public void update(Employee employee) {
    
  }
}
