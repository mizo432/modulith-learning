package undecided.erp.relationship.application.command.employee;

import lombok.NonNull;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;

public interface InsertEmployeeCommand {

  void execute(@NonNull CompanyCode companyCode, @NonNull Employee employee);

}
