package undecided.jbJobMgmt.jobDefinition.domain.jobdefinition;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 同時実行数、タイムアウトなどの実行制約を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionConstraint {

  private int maxConcurrentExecutions;
  private int timeoutSeconds;
  private int retryCount;

  public ExecutionConstraint(int maxConcurrentExecutions, int timeoutSeconds) {
    if (maxConcurrentExecutions < 1) {
      throw new IllegalArgumentException("Max concurrent executions must be at least 1");
    }
    if (timeoutSeconds < 1) {
      throw new IllegalArgumentException("Timeout seconds must be at least 1");
    }
    this.maxConcurrentExecutions = maxConcurrentExecutions;
    this.timeoutSeconds = timeoutSeconds;
    this.retryCount = 0;
  }

  public ExecutionConstraint(int maxConcurrentExecutions, int timeoutSeconds, int retryCount) {
    this(maxConcurrentExecutions, timeoutSeconds);
    if (retryCount < 0) {
      throw new IllegalArgumentException("Retry count must not be negative");
    }
    this.retryCount = retryCount;
  }
}
