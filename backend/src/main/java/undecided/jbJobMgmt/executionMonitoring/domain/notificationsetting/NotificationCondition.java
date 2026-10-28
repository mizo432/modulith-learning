package undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 通知条件を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationCondition {

  @Column(name = "notify_on_success", nullable = false)
  private boolean notifyOnSuccess;

  @Column(name = "notify_on_failure", nullable = false)
  private boolean notifyOnFailure;

  @Column(name = "notify_on_unknown", nullable = false)
  private boolean notifyOnUnknown;

  public NotificationCondition(boolean notifyOnSuccess, boolean notifyOnFailure, boolean notifyOnUnknown) {
    this.notifyOnSuccess = notifyOnSuccess;
    this.notifyOnFailure = notifyOnFailure;
    this.notifyOnUnknown = notifyOnUnknown;
  }

  public static NotificationCondition onFailureOnly() {
    return new NotificationCondition(false, true, false);
  }

  public static NotificationCondition onAll() {
    return new NotificationCondition(true, true, true);
  }

  public static NotificationCondition onAbnormal() {
    return new NotificationCondition(false, true, true);
  }
}
