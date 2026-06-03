package undecided.erp.department.internal;


import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.department.Department;

public interface DepartmentRepository extends CrudRepository<Department, SnowflakeId> {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  void deleteByCode(DepartmentCode departmentCode);
}
