package undecided.generic.rerlationshipMgmt.orgRole.department.internal;

import java.util.List;
import java.util.Optional;
import undecided.generic.rerlationshipMgmt.orgRole.department.Department;

public interface DepartmentQuery {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  List<Department> findAll();
}
