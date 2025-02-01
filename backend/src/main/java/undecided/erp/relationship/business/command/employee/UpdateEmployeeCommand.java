package undecided.erp.relationship.business.command.employee;

import static undecided.erp.common.precondition.LongPrecondition.checkPositive;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.EmployeeRepository;

/**
 * このクラスは、既存の従業員の詳細をシステム内で更新するための機能を提供します。
 * <p>
 * 更新操作を実行する前に、指定された従業員が存在するかどうかを検証します。
 * <p>
 * 特定のIDで識別される従業員の情報を更新するために、このコマンドを使用してください。
 */
@Service
@RequiredArgsConstructor
public class UpdateEmployeeCommand {

  /**
   * システム内の従業員に関連するデータへのアクセスと管理を提供します。
   * <p>
   * 従業員の存在確認、更新された従業員情報の保存、その他の永続化に関わる操作を実行するために使用されます。
   * <p>
   * このインスタンスは、従業員管理機能のためのデータ永続化層として機能します。
   */
  private final EmployeeRepository employeeRepository;

  /**
   * 指定された employeeId によって識別される既存の従業員の詳細を更新します。 まず、指定されたIDの従業員がシステム内に存在するかどうかを検証します。
   * 従業員が存在する場合、その詳細を更新し、リポジトリに保存します。 従業員が存在しない場合は、{@code IllegalArgumentException} がスローされます。
   *
   * @param employeeId 更新対象の従業員の一意のID。nullであってはなりません。
   * @param employee 更新された詳細を含む {@code Employee} オブジェクト。nullであってはなりません。
   * @throws IllegalArgumentException 指定されたIDの従業員が見つからない場合にスローされます。
   */
  public void execute(@NonNull Long employeeId, @NonNull Employee employee) {
    checkPositive(employeeId, () -> new IllegalArgumentException("EmployeeId must be positive"));
    if (employeeRepository.existsById(employeeId)) {
      employee.setEmployeeId(employeeId);
      employeeRepository.save(employee);
      return;

    }
    throw new IllegalArgumentException("Employee not found. employeeId: " + employeeId);
  }
}
