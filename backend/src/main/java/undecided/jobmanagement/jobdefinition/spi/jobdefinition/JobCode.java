package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import com.github.mizo432.shared.common.precondition.ObjectPrecondition;
import com.github.mizo432.shared.common.precondition.StringPrecondition;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.exception.BusinessException;

import jakarta.persistence.Embeddable;

@Embeddable
public final class JobCode {

  public static final int MAX_LENGTH = 50;
  private final String value;

  // JPA用
  protected JobCode() {
    this.value = null;
  }

  JobCode(String value) {
    this.value = value;
  }

  public static JobCode of(String value) {
    ObjectPrecondition.checkNotNull(value, () -> new BusinessException("JobCode cannot be null"));
    StringPrecondition.checkMaxLength(value, MAX_LENGTH,
        () -> new BusinessException("JobCode cannot exceed " + MAX_LENGTH + " characters"));
    return new JobCode(value);
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
    JobCode jobCode = (JobCode) o;
    return java.util.Objects.equals(value, jobCode.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
