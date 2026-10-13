package undecided.association.relationshipMgmt.personRole.spi.employee;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import undecided.shared.precondition.ObjectPrecondition;
import undecided.shared.precondition.StringPrecondition;
import undecided.shared.primitive.Strings2;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeNo {
  public static final String REGEX = "^[0-9]{10}$";
  public static final int MAX_LENGTH = 10;
  private String value;

  public EmployeeNo(String value) {
    this.value = value;
  }

  public static EmployeeNo of(String value) {
    ObjectPrecondition.checkNotNull(
        value, () -> new IllegalArgumentException("EmployeeNo cannot be null"));
    StringPrecondition.checkMaxLength(
        value,
        MAX_LENGTH,
        () -> new IllegalArgumentException("EmployeeNo cannot exceed 10 characters"));
    StringPrecondition.checkRegex(
        value,
        REGEX,
        () -> new IllegalArgumentException("EmployeeNo must match the pattern " + REGEX));
    return new EmployeeNo(value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EmployeeNo that = (EmployeeNo) o;
    return java.util.Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
