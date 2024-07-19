package undecided.erp.relationship.application.command.employee;

import lombok.NonNull;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeNo;

/**
 * UpdateEmployeeCommandは、会社の従業員を更新するためのコマンドを表すインターフェースです。 このコマンドでは、会社のコード、従業員番号、更新された従業員情報が必要です。
 */
public interface UpdateEmployeeCommand {

  /**
   * 社員の更新を行うコマンドを実行します。
   *
   * @param companyCode 会社のコード。
   * @param employeeNo 社員番号。
   * @param employee 更新された社員情報。
   */
  void execute(@NonNull CompanyCode companyCode, @NonNull EmployeeNo employeeNo, Employee employee);

}
