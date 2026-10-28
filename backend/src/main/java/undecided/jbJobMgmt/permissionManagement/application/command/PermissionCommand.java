package undecided.jbJobMgmt.permissionManagement.application.command;

import org.springframework.transaction.annotation.Transactional;
import undecided.jbJobMgmt.permissionManagement.spi.auditlog.ActionResult;
import undecided.jbJobMgmt.permissionManagement.spi.auditlog.ActionType;
import undecided.jbJobMgmt.permissionManagement.spi.auditlog.AuditLog;
import undecided.jbJobMgmt.permissionManagement.spi.auditlog.TargetInfo;
import undecided.jbJobMgmt.permissionManagement.spi.userpermission.PermissionLevel;
import undecided.jbJobMgmt.permissionManagement.spi.userpermission.SystemPermission;
import undecided.jbJobMgmt.permissionManagement.spi.userpermission.UserPermission;
import undecided.jbJobMgmt.permissionManagement.spi.userpermission.UserId;
import undecided.jbJobMgmt.permissionManagement.spi.userpermission.UserName;
import undecided.supporting.snowflake.SnowflakeId;

public interface PermissionCommand {

  @Transactional
  UserPermission registerUserPermission(UserId userId, UserName userName, PermissionLevel permissionLevel,
      SystemPermission systemPermission);

  @Transactional
  UserPermission changePermissionLevel(SnowflakeId id, PermissionLevel permissionLevel);

  @Transactional
  void deleteUserPermission(SnowflakeId id);

  @Transactional
  AuditLog logAction(ActionType actionType, TargetInfo targetInfo, ActionResult actionResult,
      String userId, String ipAddress);
}
