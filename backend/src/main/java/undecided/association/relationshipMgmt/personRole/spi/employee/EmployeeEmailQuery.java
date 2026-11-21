package undecided.association.relationshipMgmt.personRole.spi.employee;

import java.util.Optional;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 従業員のメールアドレスを解決するためのクエリインターフェースです。
 *
 * <p>employeeIdからpartyIdを経由してContactUse（EMAIL）を検索し、
 * 最新の{@code whenOccured}を持つメールアドレスを返します。
 */
public interface EmployeeEmailQuery {

  /**
   * 従業員IDからメールアドレスを解決します。
   *
   * @param employeeId 従業員のSnowflake ID
   * @return 解決されたメールアドレス。解決できない場合は{@code Optional.empty()}
   */
  Optional<String> resolveEmailByEmployeeId(SnowflakeId employeeId);
}
