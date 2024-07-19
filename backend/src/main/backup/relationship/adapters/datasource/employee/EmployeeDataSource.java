package undecided.erp.relationship.adapters.datasource.employee;

import org.springframework.stereotype.Component;
import undecided.erp.relationship.adapters.dao.employeeTable.EmployeeTableDao;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeRepository;

@Component
public class EmployeeDataSource implements EmployeeRepository {

  EmployeeTableDao employeeTableDao;
}
