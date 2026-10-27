package undecided.association.relationshipMgmt.orgRole.internal.department;

import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

public interface DepartmentCommand {
  void insert(Department department);

  void update(DepartmentCode departmentCode, Department department);

  void delete(DepartmentCode departmentCode);
}
