package undecided.association.relationshipMgmt.personRole.internal.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.personRole.spi.employee.CreateSimpleEmployeeCommand;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;

/** 従業員の新規作成コマンドサービスです。 EmployeeDtoからSimpleEmployeeエンティティを作成し、データベースに保存します。 */
@RequiredArgsConstructor
@Service
public class CreateSimpleEmployeeCommandService implements CreateSimpleEmployeeCommand {
  private final EmployeeFactory employeeFactory;
  private final EmployeeRepository employeeRepository;

  /**
   * 従業員情報を新規作成します。
   *
   * @param employeeDto 従業員情報のDTO。employeeId、employeeNo、searchName、fullNameを含みます。
   */
  @Override
  public void create(EmployeeDto employeeDto) {
    SimpleEmployee simpleEmployee = employeeFactory.create(employeeDto);
    employeeRepository.save(simpleEmployee);
  }
}
