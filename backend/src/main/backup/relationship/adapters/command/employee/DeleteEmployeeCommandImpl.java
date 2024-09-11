package undecided.erp.relationship.adapters.command.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.relationship.application.command.employee.DeleteEmployeeCommand;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeNo;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteEmployeeCommandImpl implements DeleteEmployeeCommand {

  /**
   * 指定した企業コードと従業員番号で指定されたコマンドを実行します。
   *
   * @param companyCode 企業コード
   * @param employeeNo 従業員番号
   */
  @Override
  public void execute(CompanyCode companyCode, EmployeeNo employeeNo) {

  }

}
