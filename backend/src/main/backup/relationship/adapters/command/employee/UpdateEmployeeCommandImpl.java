package undecided.erp.relationship.adapters.command.employee;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.relationship.application.command.employee.UpdateEmployeeCommand;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeNo;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateEmployeeCommandImpl implements UpdateEmployeeCommand {


  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(@NonNull CompanyCode companyCode, @NonNull EmployeeNo employeeNo,
      Employee employee) {

  }
}
