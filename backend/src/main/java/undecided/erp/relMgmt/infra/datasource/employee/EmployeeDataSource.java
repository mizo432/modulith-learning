package undecided.erp.relMgmt.infra.datasource.employee;

import org.springframework.stereotype.Component;
import undecided.erp.relMgmt.infra.dao.employeeTable.EmployeeTableDao;
import undecided.erp.relMgmt.model.personRole.employee.EmployeeRepository;

@Component
public class EmployeeDataSource implements EmployeeRepository {

  EmployeeTableDao employeeTableDao;
}
