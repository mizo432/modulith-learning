package undecided.jobMgmt.jobDefinition.spi.jobdefinition;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ジョブのコード、パラメータ、実行条件、バージョンを管理するルートエンティティです。
 */
@Entity
@Table(name = "job_definition")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobDefinition {

  @jakarta.persistence.Id
  private SnowflakeId id;

  @Embedded
  private JobCode jobCode;

  @jakarta.persistence.Column(name = "job_name", nullable = false, length = 100)
  private String jobName;

  @jakarta.persistence.Column(name = "description", length = 500)
  private String description;

  @Embedded
  private ParameterSpec parameterSpec;

  @Embedded
  private ExecutionConstraint executionConstraint;

  @Embedded
  private JobVersion version;

  @jakarta.persistence.Column(name = "enabled", nullable = false)
  private boolean enabled;

  @jakarta.persistence.Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @jakarta.persistence.Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public JobDefinition(JobCode jobCode, String jobName, String description,
      ParameterSpec parameterSpec, ExecutionConstraint executionConstraint) {
    this.id = SnowflakeId.of(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    this.jobCode = jobCode;
    this.jobName = jobName;
    this.description = description;
    this.parameterSpec = parameterSpec != null ? parameterSpec : ParameterSpec.empty();
    this.executionConstraint = executionConstraint;
    this.version = JobVersion.initial();
    this.enabled = true;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public void changeParameterSpec(ParameterSpec parameterSpec) {
    if (parameterSpec == null) {
      throw new IllegalArgumentException("ParameterSpec must not be null");
    }
    this.parameterSpec = parameterSpec;
    this.version = this.version.bumpPatch();
    this.updatedAt = LocalDateTime.now();
  }

  public void changeExecutionConstraint(ExecutionConstraint executionConstraint) {
    if (executionConstraint == null) {
      throw new IllegalArgumentException("ExecutionConstraint must not be null");
    }
    this.executionConstraint = executionConstraint;
    this.version = this.version.bumpPatch();
    this.updatedAt = LocalDateTime.now();
  }

  public void disable() {
    this.enabled = false;
    this.updatedAt = LocalDateTime.now();
  }

  public void enable() {
    this.enabled = true;
    this.updatedAt = LocalDateTime.now();
  }
}
