package undecided.erp.relationship.business.command.employee;

import static undecided.erp.common.precondition.LongPrecondition.checkPositive;
import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import org.springframework.stereotype.Service;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.EmployeeRepository;

@Service
public class DropEmployeeCommand {

  private final EmployeeRepository employeeRepository;

  public DropEmployeeCommand(
      EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  /**
   * 指定された従業員IDに基づいて従業員の削除処理を実行します。
   * <p>
   * 渡された従業員IDがnullではなく正の値であることを確認します。渡されたIDに該当する従業員が存在する場合、
   * リポジトリからその従業員を削除します。従業員が存在しない場合は例外をスローします。
   *
   * @param employeeId 削除対象となる従業員の一意な識別子。nullでなく、正の値である必要があります。
   * @throws IllegalArgumentException employeeIdがnull、正の値でない、または従業員が存在しない場合にスローされます。
   */
  public void execute(Long employeeId) {
    checkNotNull(employeeId, () -> new IllegalArgumentException("EmployeeId must not be null"));
    checkPositive(employeeId, () -> new IllegalArgumentException("EmployeeId must be positive"));

    if (employeeRepository.existsById(employeeId)) {
      employeeRepository.deleteEmployeeByEmployeeId(employeeId);
      return;

    }
    throw new IllegalArgumentException("Employee not found. employeeId: " + employeeId);
  }

}
