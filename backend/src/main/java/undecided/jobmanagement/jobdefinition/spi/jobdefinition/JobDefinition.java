package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_definition", schema = "job_management")
@Getter
public class JobDefinition {

  @Id
  private SnowflakeId id;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "job_code", nullable = false, length = 50, unique = true)))
  private JobCode jobCode;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "version", nullable = false, length = 20)))
  private JobVersion version;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "parameter_spec", length = 1000)))
  private ParameterSpec parameterSpec;

  @Embedded
  private ExecutionConstraint executionConstraint;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "created_by", length = 50)
  private String createdBy;

  @Column(name = "updated_by", length = 50)
  private String updatedBy;

  protected JobDefinition() {
    // JPA用
  }

  public static JobDefinition create(
      @NonNull SnowflakeId id,
      @NonNull JobCode jobCode,
      @NonNull JobVersion version,
      @Nullable ParameterSpec parameterSpec,
      @Nullable ExecutionConstraint executionConstraint,
      @NonNull LocalDateTime createdAt,
      @Nullable String createdBy) {
    if (id == null) {
      throw new IllegalArgumentException("idはnullであってはいけません");
    }
    if (jobCode == null) {
      throw new IllegalArgumentException("jobCodeはnullであってはいけません");
    }
    if (version == null) {
      throw new IllegalArgumentException("versionはnullであってはいけません");
    }
    if (createdAt == null) {
      throw new IllegalArgumentException("createdAtはnullであってはいけません");
    }
    JobDefinition entity = new JobDefinition();
    entity.id = id;
    entity.jobCode = jobCode;
    entity.version = version;
    entity.parameterSpec = parameterSpec;
    entity.executionConstraint = executionConstraint;
    entity.createdAt = createdAt;
    entity.createdBy = createdBy;
    entity.isActive = true;
    return entity;
  }

  // JPA用パッケージプライベートセッター
  void setId(SnowflakeId id) {
    this.id = id;
  }

  void setJobCode(JobCode jobCode) {
    this.jobCode = jobCode;
  }

  void setVersion(JobVersion version) {
    this.version = version;
  }

  void setParameterSpec(ParameterSpec parameterSpec) {
    this.parameterSpec = parameterSpec;
  }

  void setExecutionConstraint(ExecutionConstraint executionConstraint) {
    this.executionConstraint = executionConstraint;
  }

  void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
