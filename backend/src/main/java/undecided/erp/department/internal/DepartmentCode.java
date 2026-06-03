package undecided.erp.department.internal;

import jakarta.persistence.Embeddable;

@Embeddable
public record DepartmentCode(String value) {
    public static final DepartmentCode EMPTY = new DepartmentCode("");
}
