package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import com.github.mizo432.shared.exception.BusinessException;

import jakarta.persistence.Embeddable;

@Embeddable
public final class ExecutionConstraint {

  private final Integer maxConcurrentExecutions;
  private final Integer timeoutSeconds;

  // JPA用
  protected ExecutionConstraint() {
    this.maxConcurrentExecutions = null;
    this.timeoutSeconds = null;
  }

  ExecutionConstraint(Integer maxConcurrentExecutions, Integer timeoutSeconds) {
    this.maxConcurrentExecutions = maxConcurrentExecutions;
    this.timeoutSeconds = timeoutSeconds;
  }

  public static ExecutionConstraint create(Integer maxConcurrentExecutions, Integer timeoutSeconds) {
    if (maxConcurrentExecutions != null && maxConcurrentExecutions <= 0) {
      throw new BusinessException("maxConcurrentExecutions must be positive");
    }
    if (timeoutSeconds != null && timeoutSeconds <= 0) {
      throw new BusinessException("timeoutSeconds must be positive");
    }
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
