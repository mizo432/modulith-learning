package undecided.erp.relationship.adapters.command.employee;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.relationship.application.command.employee.InsertEmployeeCommand;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;

@Service
@RequiredArgsConstructor
@Transactional
public class InsertEmployeeCommandImpl implements InsertEmployeeCommand {


  @Override
  public void execute(@NonNull CompanyCode companyCode, @NonNull Employee employee) {

  }
}
