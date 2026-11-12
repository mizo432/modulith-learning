package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobCode {

  public static final int MAX_LENGTH = 50;
  private String value;

  public JobCode(String value) {
    this.value = value;
  }

  public static JobCode of(String value) {
    com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull(
        value, () -> new IllegalArgumentException("JobCode cannot be null"));
    com.github.mizo432.shared.common.precondition.StringPrecondition.checkMaxLength(
        value,
        MAX_LENGTH,
        () -> new IllegalArgumentException("JobCode cannot exceed " + MAX_LENGTH + " characters"));
    return new JobCode(value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return com.github.mizo432.shared.common.primitive.Strings2.toString(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JobCode jobCode = (JobCode) o;
    return java.util.Objects.equals(value, jobCode.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
