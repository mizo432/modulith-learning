package undecided.erp.department;

import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.department.internal.DepartmentCode;

public class Department {
    private DepartmentCode departmentCode = DepartmentCode.EMPTY;
    private SnowflakeId id = SnowflakeId.EMPTY;

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public Department update(Department department) {
        return null;
    }

    public SnowflakeId getId() {
        return id;
    }
}
