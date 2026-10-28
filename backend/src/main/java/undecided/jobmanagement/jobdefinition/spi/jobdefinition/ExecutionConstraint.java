package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionConstraint {

  private Integer maxConcurrentExecutions;
  private Integer timeoutSeconds;

  public ExecutionConstraint(Integer maxConcurrentExecutions, Integer timeoutSeconds) {
    this.maxConcurrentExecutions = maxConcurrentExecutions;
    this.timeoutSeconds = timeoutSeconds;
  }

  public static ExecutionConstraint of(Integer maxConcurrentExecutions, Integer timeoutSeconds) {
    return new ExecutionConstraint(maxConcurrentExecutions, timeoutSeconds);
  }

  public Integer getMaxConcurrentExecutions() {
    return maxConcurrentExecutions;
  }

  public Integer getTimeoutSeconds() {
    return timeoutSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExecutionConstraint that = (ExecutionConstraint) o;
    return java.util.Objects.equals(maxConcurrentExecutions, that.maxConcurrentExecutions) &&
        java.util.Objects.equals(timeoutSeconds, that.timeoutSeconds);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(maxConcurrentExecutions, timeoutSeconds);
  }
}
