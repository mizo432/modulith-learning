package undecided.relationshipMgmt.orgRole.internal.department;

import java.util.List;
import java.util.Optional;
import undecided.relationshipMgmt.orgRole.spi.Department;

public interface DepartmentQuery {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  List<Department> findAll();
}
