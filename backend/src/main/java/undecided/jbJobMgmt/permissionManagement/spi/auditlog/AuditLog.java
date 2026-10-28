package undecided.jbJobMgmt.permissionManagement.spi.auditlog;

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
 * 監査ログを管理するルートエンティティです。
 */
@Entity
@Table(name = "audit_log")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLog {

  @jakarta.persistence.Id
  private SnowflakeId id;

  @Embedded
  private ActionType actionType;

  @Embedded
  private TargetInfo targetInfo;

  @Embedded
  private ActionResult actionResult;

  @jakarta.persistence.Column(name = "user_id", nullable = false, length = 50)
  private String userId;

  @jakarta.persistence.Column(name = "performed_at", nullable = false)
  private LocalDateTime performedAt;

  @jakarta.persistence.Column(name = "ip_address", length = 45)
  private String ipAddress;

  public AuditLog(ActionType actionType, TargetInfo targetInfo, ActionResult actionResult,
      String userId, String ipAddress) {
    this.id = SnowflakeId.of(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    this.actionType = actionType;
    this.targetInfo = targetInfo;
    this.actionResult = actionResult;
    this.userId = userId;
    this.ipAddress = ipAddress;
    this.performedAt = LocalDateTime.now();
  }
}
