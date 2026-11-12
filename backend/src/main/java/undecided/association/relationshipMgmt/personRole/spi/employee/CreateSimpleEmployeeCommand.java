package undecided.association.relationshipMgmt.personRole.spi.employee;

import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;

public interface CreateSimpleEmployeeCommand {

  void create(EmployeeDto employeeDto);
}
