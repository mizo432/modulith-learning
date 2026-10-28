package undecided.jobMgmt.jobDefinition.spi.jobdefinition;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * バージョン番号を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobVersion {

  private static final int MAX_LENGTH = 20;

  @Column(name = "version", nullable = false, length = MAX_LENGTH)
  private String version;

  public JobVersion(String version) {
    if (version == null || version.isBlank()) {
      throw new IllegalArgumentException("Version must not be null or blank");
    }
    if (version.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("Version must not exceed " + MAX_LENGTH + " characters");
    }
    this.version = version;
  }

  public static JobVersion initial() {
    return new JobVersion("1.0.0");
  }

  public JobVersion bumpPatch() {
    String[] parts = version.split("\\.");
    if (parts.length != 3) {
      throw new IllegalStateException("Invalid version format: " + version);
    }
    int patch = Integer.parseInt(parts[2]) + 1;
    return new JobVersion(parts[0] + "." + parts[1] + "." + patch);
  }

  @Override
  public String toString() {
    return version;
  }
}
