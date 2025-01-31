package undecided.erp.relationship.business.command.employee;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import org.springframework.stereotype.Service;
import undecided.erp.relationship.domain.model.partyRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.employee.EmployeeRepository;

@Service
public class CreateEmployeeCommand {

  private final EmployeeRepository employeeRepository;

  public CreateEmployeeCommand(
      EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Employee execute(Employee employee) {
    checkNotNull(employee,
        () -> new IllegalArgumentException("Employee must not be null"));
    return employeeRepository.save(employee);
  }
}
