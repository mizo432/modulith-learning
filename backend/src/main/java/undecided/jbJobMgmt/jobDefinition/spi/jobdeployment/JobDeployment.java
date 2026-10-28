package undecided.jbJobMgmt.jobDefinition.spi.jobdeployment;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobCode;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ジョブとサーバーの対応関係を管理するエンティティです。
 */
@Entity
@Table(name = "job_deployment")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobDeployment {

  @jakarta.persistence.Id
  private SnowflakeId id;

  @Embedded
  private JobCode jobCode;

  @Embedded
  private ServerAddress serverAddress;

  @Embedded
  private DeploymentVersion deploymentVersion;

  @jakarta.persistence.Column(name = "deployed_at", nullable = false)
  private LocalDateTime deployedAt;

  @Enumerated(EnumType.STRING)
  @jakarta.persistence.Column(name = "status", nullable = false, length = 20)
  private DeploymentStatus status;

  public enum DeploymentStatus {
    ACTIVE,
    INACTIVE
  }

  public JobDeployment(JobCode jobCode, ServerAddress serverAddress, DeploymentVersion deploymentVersion) {
    this.id = SnowflakeId.of(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    this.jobCode = jobCode;
    this.serverAddress = serverAddress;
    this.deploymentVersion = deploymentVersion;
    this.deployedAt = LocalDateTime.now();
    this.status = DeploymentStatus.ACTIVE;
  }

  public void updateVersion(DeploymentVersion deploymentVersion) {
    if (deploymentVersion == null) {
      throw new IllegalArgumentException("DeploymentVersion must not be null");
    }
    this.deploymentVersion = deploymentVersion;
    this.deployedAt = LocalDateTime.now();
  }

  public void deactivate() {
    this.status = DeploymentStatus.INACTIVE;
  }
}
