package undecided.erp.department.internal;

import undecided.erp.department.Department;

public interface DepartmentCommand {
    void insert(Department department);

    void update(DepartmentCode departmentCode, Department department);

    void delete(DepartmentCode departmentCode);
}
