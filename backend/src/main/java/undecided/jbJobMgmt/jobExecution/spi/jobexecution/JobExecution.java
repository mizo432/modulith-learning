package undecided.jbJobMgmt.jobExecution.spi.jobexecution;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobCode;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobVersion;
import undecided.jbJobMgmt.jobDefinition.spi.jobdeployment.ServerAddress;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 実行状態・結果・履歴を管理するルートエンティティです。
 */
@Entity
@Table(name = "job_execution")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobExecution {

  @jakarta.persistence.Id
  private SnowflakeId id;

  @Embedded
  private JobCode jobCode;

  @Embedded
  private JobVersion jobVersion;

  @Embedded
  private ServerAddress serverAddress;

  @Embedded
  private ExecutionState executionState;

  @Embedded
  private Checkpoint checkpoint;

  @Column(name = "processed_count")
  private Long processedCount;

  @Embedded
  private ErrorInfo errorInfo;

  @Column(name = "started_at")
  private LocalDateTime startedAt;

  @Column(name = "finished_at")
  private LocalDateTime finishedAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public JobExecution(JobCode jobCode, JobVersion jobVersion, ServerAddress serverAddress) {
    this.id = SnowflakeId.of(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    this.jobCode = jobCode;
    this.jobVersion = jobVersion;
    this.serverAddress = serverAddress;
    this.executionState = ExecutionState.PENDING;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public void start() {
    if (this.executionState != ExecutionState.PENDING && this.executionState != ExecutionState.RE_EXECUTED && this.executionState != ExecutionState.RESUMED) {
      throw new IllegalStateException("Cannot start execution in state: " + this.executionState);
    }
    this.executionState = ExecutionState.RUNNING;
    this.startedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void succeed() {
    if (this.executionState != ExecutionState.RUNNING) {
      throw new IllegalStateException("Cannot succeed execution in state: " + this.executionState);
    }
    this.executionState = ExecutionState.SUCCESS;
    this.finishedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void fail(ErrorInfo errorInfo) {
    if (this.executionState != ExecutionState.RUNNING) {
      throw new IllegalStateException("Cannot fail execution in state: " + this.executionState);
    }
    if (errorInfo == null) {
      throw new IllegalArgumentException("ErrorInfo must not be null");
    }
    this.executionState = ExecutionState.FAILURE;
    this.errorInfo = errorInfo;
    this.finishedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void cancel() {
    if (this.executionState != ExecutionState.RUNNING && this.executionState != ExecutionState.PENDING) {
      throw new IllegalStateException("Cannot cancel execution in state: " + this.executionState);
    }
    this.executionState = ExecutionState.CANCELLED;
    this.finishedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void reExecute() {
    if (this.executionState != ExecutionState.FAILURE) {
      throw new IllegalStateException("Cannot re-execute in state: " + this.executionState);
    }
    this.executionState = ExecutionState.RE_EXECUTED;
    this.errorInfo = null;
    this.updatedAt = LocalDateTime.now();
  }

  public void resume() {
    if (this.executionState != ExecutionState.FAILURE) {
      throw new IllegalStateException("Cannot resume in state: " + this.executionState);
    }
    if (this.checkpoint == null) {
      throw new IllegalStateException("Cannot resume without checkpoint");
    }
    this.executionState = ExecutionState.RESUMED;
    this.errorInfo = null;
    this.updatedAt = LocalDateTime.now();
  }

  public void markUnknown() {
    if (this.executionState != ExecutionState.RUNNING) {
      throw new IllegalStateException("Cannot mark unknown in state: " + this.executionState);
    }
    this.executionState = ExecutionState.UNKNOWN;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateCheckpoint(Checkpoint checkpoint) {
    if (checkpoint == null) {
      throw new IllegalArgumentException("Checkpoint must not be null");
    }
    this.checkpoint = checkpoint;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateProcessedCount(Long count) {
    if (count == null || count < 0) {
      throw new IllegalArgumentException("Processed count must not be negative");
    }
    this.processedCount = count;
    this.updatedAt = LocalDateTime.now();
  }
}
