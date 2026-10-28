package undecided.jbJobMgmt.permissionManagement.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.ActionResult;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.ActionType;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.AuditLog;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.TargetInfo;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.repository.AuditLogRepository;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.PermissionLevel;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.SystemPermission;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserPermission;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserId;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserName;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.repository.UserPermissionRepository;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@RequiredArgsConstructor
public class PermissionCommandImpl implements PermissionCommand {

  private final UserPermissionRepository userPermissionRepository;
  private final AuditLogRepository auditLogRepository;

  @Override
  public UserPermission registerUserPermission(UserId userId, UserName userName, PermissionLevel permissionLevel,
      SystemPermission systemPermission) {
    UserPermission userPermission = new UserPermission(userId, userName, permissionLevel, systemPermission);
    return userPermissionRepository.save(userPermission);
  }

  @Override
  public UserPermission changePermissionLevel(SnowflakeId id, PermissionLevel permissionLevel) {
    UserPermission userPermission = userPermissionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("UserPermission not found: " + id));
    userPermission.changePermissionLevel(permissionLevel);
    return userPermissionRepository.save(userPermission);
  }

  @Override
  public void deleteUserPermission(SnowflakeId id) {
    if (!userPermissionRepository.existsById(id)) {
      throw new IllegalArgumentException("UserPermission not found: " + id);
    }
    userPermissionRepository.deleteById(id);
  }

  @Override
  public AuditLog logAction(ActionType actionType, TargetInfo targetInfo, ActionResult actionResult,
      String userId, String ipAddress) {
    AuditLog auditLog = new AuditLog(actionType, targetInfo, actionResult, userId, ipAddress);
    return auditLogRepository.save(auditLog);
  }
}
