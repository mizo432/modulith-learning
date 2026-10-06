package undecided.generic.relationshipMgmt.orgRole.internal.department;

import undecided.generic.relationshipMgmt.orgRole.spi.Department;

public interface DepartmentCommand {
  void insert(Department department);

  void update(DepartmentCode departmentCode, Department department);

  void delete(DepartmentCode departmentCode);
}
