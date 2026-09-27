package undecided.supporting.rerlationshipMgmt.department.internal;

import java.util.List;
import java.util.Optional;
import undecided.supporting.rerlationshipMgmt.department.Department;

public interface DepartmentQuery {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  List<Department> findAll();
}
