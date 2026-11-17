package undecided.jobmanagement.jobexecution.spi.jobexecution;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionId {

  public static final int MAX_LENGTH = 50;
  private String value;

  public ExecutionId(String value) {
    this.value = value;
  }

  public static ExecutionId of(String value) {
    com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull(
        value, () -> new IllegalArgumentException("ExecutionId cannot be null"));
    com.github.mizo432.shared.common.precondition.StringPrecondition.checkMaxLength(
        value,
        MAX_LENGTH,
        () -> new IllegalArgumentException("ExecutionId cannot exceed " + MAX_LENGTH + " characters"));
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
