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
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_execution", schema = "job_management")
@Getter
@Setter
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
}
