package undecided.jbJobMgmt.jobDefinition.spi.jobdeployment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 配備されたジョブのバージョンを表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeploymentVersion {

  @Column(name = "version", nullable = false, length = 20)
  private String version;

  public DeploymentVersion(String version) {
    if (version == null || version.isBlank()) {
      throw new IllegalArgumentException("Version must not be null or blank");
    }
    this.version = version;
  }

  @Override
  public String toString() {
    return version;
  }
}
