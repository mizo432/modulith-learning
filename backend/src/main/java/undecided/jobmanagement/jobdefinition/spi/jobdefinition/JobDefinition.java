package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_definition", schema = "job_management")
@Getter
@Setter
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
}
