package undecided.generic.relationshipMgmt.orgRole.internal.department;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.generic.relationshipMgmt.orgRole.spi.Department;
import undecided.supporting.snowflake.spi.SnowflakeId;

public interface DepartmentRepository extends CrudRepository<Department, SnowflakeId> {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  void deleteByCode(DepartmentCode departmentCode);
}
