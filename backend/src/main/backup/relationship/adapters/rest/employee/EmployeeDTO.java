package undecided.erp.relationship.adapters.rest.employee;

import undecided.erp.relationship.domain.model.personRole.employee.Employee;

public class EmployeeDTO {

  public static EmployeeDTO[] convertFromModelsToDTOs(Employee[] employees) {
    return new EmployeeDTO[employees.length];
  }

  public static EmployeeDTO convertFromModel(Employee employee) {
    return new EmployeeDTO();
  }

  public Employee convertToModel() {
    return Employee.EMPTY;
  }
}
