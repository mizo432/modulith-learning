package undecided.erp.relationship.business.query.employee;

import java.util.Optional;
import undecided.erp.relationship.domain.model.partyRole.personRole.employee.Employee;

/**
 * システム内の従業員エンティティをクエリするためのメソッドを提供します。
 */
public interface EmployeeQuery {

  /**
   * 従業員をその一意の識別子で取得します。
   *
   * @param employeeId 取得する従業員の一意の識別子
   * @return 従業員エンティティが見つかった場合はそれを含むOptional、 一致する従業員が存在しない場合は空のOptional
   */
  Optional<Employee> findByEmployeeId(Long employeeId);

  Iterable<Employee> findByInitials(String initials);

}
