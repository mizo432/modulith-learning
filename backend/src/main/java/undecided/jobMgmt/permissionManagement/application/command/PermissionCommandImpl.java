package undecided.jobMgmt.permissionManagement.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobMgmt.permissionManagement.spi.auditlog.ActionResult;
import undecided.jobMgmt.permissionManagement.spi.auditlog.ActionType;
import undecided.jobMgmt.permissionManagement.spi.auditlog.AuditLog;
import undecided.jobMgmt.permissionManagement.spi.auditlog.TargetInfo;
import undecided.jobMgmt.permissionManagement.spi.auditlog.repository.AuditLogRepository;
import undecided.jobMgmt.permissionManagement.spi.userpermission.PermissionLevel;
import undecided.jobMgmt.permissionManagement.spi.userpermission.SystemPermission;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserPermission;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserId;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserName;
import undecided.jobMgmt.permissionManagement.spi.userpermission.repository.UserPermissionRepository;
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
