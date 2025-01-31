package undecided.erp.relationship.infra.query.employee;

import java.util.Optional;
import org.springframework.stereotype.Service;
import undecided.erp.relationship.business.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.partyRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.employee.EmployeeRepository;

@Service
public class EmployeeQueryImpl implements EmployeeQuery {

  private final EmployeeRepository employeeRepository;

  public EmployeeQueryImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Optional<Employee> findByEmployeeId(Long employeeId) {
    return Optional.ofNullable(employeeRepository.findEmployeeByEmployeeId(employeeId));
  }
}
