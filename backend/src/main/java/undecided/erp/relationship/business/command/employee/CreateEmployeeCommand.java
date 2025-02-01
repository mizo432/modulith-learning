package undecided.erp.relationship.business.command.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class CreateEmployeeCommand {

  private final EmployeeRepository employeeRepository;

  public @NonNull Employee execute(@NonNull Employee employee) {
    return employeeRepository.save(employee);
  }
}
