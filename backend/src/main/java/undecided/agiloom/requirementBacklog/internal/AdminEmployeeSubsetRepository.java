package undecided.agiloom.requirementBacklog.internal;

import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

/** 管理者サブセットのリポジトリ。 */
interface AdminEmployeeSubsetRepository extends CrudRepository<AdminEmployeeSubset, SnowflakeId> {

  /** 指定した従業員がADMIN保持者かどうかを判定します。 */
  boolean existsByEmployeeId(SnowflakeId employeeId);
}
