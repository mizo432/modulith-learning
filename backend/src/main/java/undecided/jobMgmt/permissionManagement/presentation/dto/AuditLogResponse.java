package undecided.jobMgmt.permissionManagement.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

  private Long id;
  private String actionType;
  private String targetType;
  private String targetId;
  private String result;
  private String message;
  private String userId;
  private String performedAt;
  private String ipAddress;

  public static AuditLogResponse from(undecided.jobMgmt.permissionManagement.spi.auditlog.AuditLog auditLog) {
    AuditLogResponse response = new AuditLogResponse();
    response.id = auditLog.getId().value();
    response.actionType = auditLog.getActionType().getType();
    response.targetType = auditLog.getTargetInfo().getTargetType();
    response.targetId = auditLog.getTargetInfo().getTargetId();
    response.result = auditLog.getActionResult().getResult();
    response.message = auditLog.getActionResult().getMessage();
    response.userId = auditLog.getUserId();
    response.performedAt = auditLog.getPerformedAt().toString();
    response.ipAddress = auditLog.getIpAddress();
    return response;
  }
}
