package undecided.erp.relationship.application.command.employee;

import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeNo;

public interface DeleteEmployeeCommand {

  /**
   * 指定した企業コードと従業員番号で指定されたコマンドを実行します。
   *
   * @param companyCode 企業コード
   * @param employeeNo 従業員番号
   */
  void execute(CompanyCode companyCode, EmployeeNo employeeNo);

}
