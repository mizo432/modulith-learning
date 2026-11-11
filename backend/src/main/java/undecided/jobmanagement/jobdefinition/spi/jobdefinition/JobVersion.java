package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import com.github.mizo432.shared.exception.BusinessException;

import jakarta.persistence.Embeddable;

@Embeddable
public final class JobVersion {

  public static final int MAX_LENGTH = 20;
  private final String value;

  // JPA用
  protected JobVersion() {
    this.value = null;
  }

  JobVersion(String value) {
    this.value = value;
  }

  public static JobVersion of(String value) {
    com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull(
        value, () -> new BusinessException("JobVersion cannot be null"));
    com.github.mizo432.shared.common.precondition.StringPrecondition.checkMaxLength(
        value,
        MAX_LENGTH,
        () -> new BusinessException("JobVersion cannot exceed " + MAX_LENGTH + " characters"));
    return new JobVersion(value);
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
    JobVersion that = (JobVersion) o;
    return java.util.Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
