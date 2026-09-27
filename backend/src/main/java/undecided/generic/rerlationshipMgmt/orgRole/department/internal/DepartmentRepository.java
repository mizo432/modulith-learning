package undecided.generic.rerlationshipMgmt.orgRole.department.internal;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.generic.rerlationshipMgmt.orgRole.department.Department;
import undecided.supporting.entity.SnowflakeId;

public interface DepartmentRepository extends CrudRepository<Department, SnowflakeId> {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  void deleteByCode(DepartmentCode departmentCode);
}
