package undecided.jbJobMgmt.jobExecution.domain.jobexecution;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ジョブ実行の状態を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionState {

  @Column(name = "execution_state", nullable = false, length = 20)
  private String state;

  public static final ExecutionState PENDING = new ExecutionState("PENDING");
  public static final ExecutionState RUNNING = new ExecutionState("RUNNING");
  public static final ExecutionState SUCCESS = new ExecutionState("SUCCESS");
  public static final ExecutionState FAILURE = new ExecutionState("FAILURE");
  public static final ExecutionState UNKNOWN = new ExecutionState("UNKNOWN");
  public static final ExecutionState RE_EXECUTED = new ExecutionState("RE_EXECUTED");
  public static final ExecutionState RESUMED = new ExecutionState("RESUMED");
  public static final ExecutionState CANCELLED = new ExecutionState("CANCELLED");

  private ExecutionState(String state) {
    this.state = state;
  }

  public boolean isTerminal() {
    return this == SUCCESS || this == FAILURE || this == CANCELLED;
  }

  public boolean isRunnable() {
    return this == PENDING || this == RE_EXECUTED || this == RESUMED;
  }

  @Override
  public String toString() {
    return state;
  }
}
