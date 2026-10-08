package undecided.generic.relationshipMgmt.orgRole.internal.department;

import undecided.generic.relationshipMgmt.orgRole.spi.Department;
import undecided.generic.relationshipMgmt.orgRole.spi.DepartmentCode;

public interface DepartmentCommand {
  void insert(Department department);

  void update(DepartmentCode departmentCode, Department department);

  void delete(DepartmentCode departmentCode);
}
