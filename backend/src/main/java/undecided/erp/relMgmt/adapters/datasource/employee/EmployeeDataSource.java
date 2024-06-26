package undecided.erp.relMgmt.adapters.datasource.employee;

import org.springframework.stereotype.Component;
import undecided.erp.relMgmt.adapters.dao.employeeTable.EmployeeTableDao;
import undecided.erp.relMgmt.domain.model.personRole.employee.EmployeeRepository;

@Component
public class EmployeeDataSource implements EmployeeRepository {

  EmployeeTableDao employeeTableDao;
}
