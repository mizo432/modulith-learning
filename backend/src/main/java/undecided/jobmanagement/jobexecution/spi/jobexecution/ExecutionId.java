package undecided.jobmanagement.jobexecution.spi.jobexecution;

import com.github.mizo432.shared.exception.BusinessException;

import jakarta.persistence.Embeddable;

@Embeddable
public final class ExecutionId {

  public static final int MAX_LENGTH = 50;
  private final String value;

  // JPA用
  protected ExecutionId() {
    this.value = null;
  }

  ExecutionId(String value) {
    this.value = value;
  }

  public static ExecutionId of(String value) {
    com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull(
        value, () -> new BusinessException("ExecutionId cannot be null"));
    com.github.mizo432.shared.common.precondition.StringPrecondition.checkMaxLength(
        value,
        MAX_LENGTH,
        () -> new BusinessException("ExecutionId cannot exceed " + MAX_LENGTH + " characters"));
    return new ExecutionId(value);
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
    ExecutionId that = (ExecutionId) o;
    return java.util.Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
