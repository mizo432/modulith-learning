package undecided.association.relationshipMgmt.personRole.internal.command;

import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;

public interface EmployeeFactory {

  SimpleEmployee create(EmployeeDto employeeDto);
}
