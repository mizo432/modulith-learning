package undecided.supporting.rerlationshipMgmt.department.internal;

import undecided.supporting.rerlationshipMgmt.department.Department;

public interface DepartmentCommand {
  void insert(Department department);

  void update(DepartmentCode departmentCode, Department department);

  void delete(DepartmentCode departmentCode);
}
