package undecided.erp.department.internal;

public record DepartmentCode(String value) {
    public static final DepartmentCode EMPTY = new DepartmentCode("");
}
