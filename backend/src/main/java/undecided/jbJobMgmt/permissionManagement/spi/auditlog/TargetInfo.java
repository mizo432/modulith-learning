package undecided.jbJobMgmt.permissionManagement.spi.auditlog;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 操作対象を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetInfo {

  @Column(name = "target_type", nullable = false, length = 50)
  private String targetType;

  @Column(name = "target_id", nullable = false, length = 50)
  private String targetId;

  public TargetInfo(String targetType, String targetId) {
    if (targetType == null || targetType.isBlank()) {
      throw new IllegalArgumentException("Target type must not be null or blank");
    }
    if (targetId == null || targetId.isBlank()) {
      throw new IllegalArgumentException("Target ID must not be null or blank");
    }
    this.targetType = targetType;
    this.targetId = targetId;
  }
}
