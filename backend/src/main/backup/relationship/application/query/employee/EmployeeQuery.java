package undecided.erp.relationship.application.query.employee;

import java.util.Optional;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeNo;
import undecided.erp.shared.entity.SnowflakeId;

public interface EmployeeQuery {

  Optional<Employee> findById(SnowflakeId<Party> id);

  Optional<Employee> findByCompanyCodeAndEmployeeNo(CompanyCode companyCode, EmployeeNo employeeNo);

  Employee[] selectByCompanyCode(CompanyCode companyCode);

}
