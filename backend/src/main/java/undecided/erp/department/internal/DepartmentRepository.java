package undecided.erp.department.internal;


import org.springframework.data.repository.CrudRepository;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.department.Department;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, SnowflakeId> {

    Optional<Department> findByDepartmentCode(DepartmentCode departmentCode);

    void deleteByDepartmentCode(DepartmentCode departmentCode);
}
