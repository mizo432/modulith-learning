package undecided.erp.department.internal;

import undecided.erp.department.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentQuery {

    Optional<Department> findByCode(DepartmentCode departmentCode);

    List<Department> findAll();

}
