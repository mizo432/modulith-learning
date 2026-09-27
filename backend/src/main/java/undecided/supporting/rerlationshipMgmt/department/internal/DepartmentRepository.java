package undecided.supporting.rerlationshipMgmt.department.internal;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.generic.entity.SnowflakeId;
import undecided.supporting.rerlationshipMgmt.department.Department;

public interface DepartmentRepository extends CrudRepository<Department, SnowflakeId> {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  void deleteByCode(DepartmentCode departmentCode);
}
