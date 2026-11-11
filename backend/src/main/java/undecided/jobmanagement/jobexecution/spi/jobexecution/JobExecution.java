package undecided.jobmanagement.jobexecution.spi.jobexecution;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_execution", schema = "job_management")
@Getter
public class JobExecution {

  @Id
  private SnowflakeId id;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "execution_id", nullable = false, length = 50, unique = true)))
  private ExecutionId executionId;

  @Column(name = "job_code", nullable = false, length = 50)
  private String jobCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false, length = 20)
  private ExecutionState state;

  @Column(name = "started_at")
  private LocalDateTime startedAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @Column(name = "error_message", length = 2000)
  private String errorMessage;

  @Column(name = "processed_count")
  private Long processedCount;

  @Column(name = "checkpoint_position", length = 500)
  private String checkpointPosition;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by", length = 50)
  private String createdBy;

  protected JobExecution() {
    // JPA用
  }

  public static JobExecution create(
      @NonNull SnowflakeId id,
      @NonNull ExecutionId executionId,
      @NonNull String jobCode,
      @NonNull ExecutionState state,
      @NonNull LocalDateTime createdAt,
      @Nullable String createdBy) {
    JobExecution entity = new JobExecution();
    entity.id = id;
    entity.executionId = executionId;
    entity.jobCode = jobCode;
    entity.state = state;
    entity.createdAt = createdAt;
    entity.createdBy = createdBy;
    return entity;
  }

  // JPA用パッケージプライベートセッター
  void setId(SnowflakeId id) {
    this.id = id;
  }

  void setExecutionId(ExecutionId executionId) {
    this.executionId = executionId;
  }

  void setJobCode(String jobCode) {
    this.jobCode = jobCode;
  }

  void setState(ExecutionState state) {
    this.state = state;
  }

  void setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
  }

  void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
  }

  void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  void setProcessedCount(Long processedCount) {
    this.processedCount = processedCount;
  }

  void setCheckpointPosition(String checkpointPosition) {
    this.checkpointPosition = checkpointPosition;
  }

  void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
}
