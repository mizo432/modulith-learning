package undecided.erp.relationship.adapters.query.employee;

import java.util.Optional;
import org.springframework.stereotype.Service;
import undecided.erp.relationship.application.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeNo;
import undecided.erp.shared.entity.SnowflakeId;

@Service
public class EmployeeQueryImpl implements EmployeeQuery {

  public Optional<Employee> findById(SnowflakeId<Party> id) {
    return Optional.empty();
  }

  @Override
  public Optional<Employee> findByCompanyCodeAndEmployeeNo(CompanyCode companyCode,
      EmployeeNo employeeNo) {
    return Optional.empty();
  }

  @Override
  public Employee[] selectByCompanyCode(CompanyCode companyCode) {
    return new Employee[0];
  }

}
