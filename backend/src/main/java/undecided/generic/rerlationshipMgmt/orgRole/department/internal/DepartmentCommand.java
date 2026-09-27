package undecided.generic.rerlationshipMgmt.orgRole.department.internal;

import undecided.generic.rerlationshipMgmt.orgRole.department.Department;

public interface DepartmentCommand {
  void insert(Department department);

  void update(DepartmentCode departmentCode, Department department);

  void delete(DepartmentCode departmentCode);
}
