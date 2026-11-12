package undecided.association.relationshipMgmt.personRole.internal.command;

import org.springframework.stereotype.Component;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;

/** 従業員エンティティのファクトリ実装クラスです。 EmployeeDtoからSimpleEmployeeエンティティへの変換を行います。 */
@Component
public class EmployeeFactoryImpl implements EmployeeFactory {

  /**
   * EmployeeDtoからSimpleEmployeeエンティティを作成します。
   *
   * @param employeeDto 従業員情報のDTO。employeeId、employeeNo、searchName、fullNameを含みます。
   * @return 作成されたSimpleEmployeeエンティティ。
   */
  @Override
  public SimpleEmployee create(EmployeeDto employeeDto) {
    SimpleEmployee simpleEmployee = new SimpleEmployee();
    simpleEmployee.setEmployeeId(employeeDto.employeeId());
    simpleEmployee.setEmployeeNo(employeeDto.employeeNo());
    simpleEmployee.setSearchName(employeeDto.searchName());
    simpleEmployee.setFullName(employeeDto.fullName());
    return simpleEmployee;
  }
}
