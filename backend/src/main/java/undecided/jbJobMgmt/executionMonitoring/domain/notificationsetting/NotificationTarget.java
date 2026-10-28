package undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 通知対象を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationTarget {

  @Column(name = "target_type", nullable = false, length = 20)
  private String targetType;

  @Column(name = "target_id", nullable = false, length = 50)
  private String targetId;

  public NotificationTarget(String targetType, String targetId) {
    if (targetType == null || targetType.isBlank()) {
      throw new IllegalArgumentException("Target type must not be null or blank");
    }
    if (targetId == null || targetId.isBlank()) {
      throw new IllegalArgumentException("Target ID must not be null or blank");
    }
    this.targetType = targetType;
    this.targetId = targetId;
  }

  public static NotificationTarget jobCode(String jobCode) {
    return new NotificationTarget("jobCode", jobCode);
  }

  public static NotificationTarget company(String companyId) {
    return new NotificationTarget("company", companyId);
  }

  public static NotificationTarget store(String storeId) {
    return new NotificationTarget("store", storeId);
  }
}
