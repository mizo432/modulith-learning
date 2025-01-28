package undecided.erp.relationship.domain.model.personRole.employee;

import java.util.Optional;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * EmployeeRepositoryインターフェースは、社員情報を取り扱うためのメソッドを提供します。
 */
public interface EmployeeRepository {

  /**
   * 指定されたスノーフレークID（SnowflakeId）で社員を取得します。
   *
   * @param id 取得したい社員のスノーフレークID
   * @return 見つかった社員を含むOptional、指定されたIDの社員が見つからない場合は空のOptional
   */
  Optional<Employee> findById(SnowflakeId id);

  /**
   * 提供されたEmployeeオブジェクトをリポジトリに永続化します。
   *
   * @param employee 保存するEmployeeオブジェクト
   * @return 保存された結果、更新がある場合は更新後のEmployeeオブジェクト
   */
  Employee save(Employee employee);

  void insert(Employee employee);

  void update(Employee employee);
  
}
