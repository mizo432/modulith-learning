package undecided.jbJobMgmt.permissionManagement.application.command;

import org.springframework.transaction.annotation.Transactional;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.ActionResult;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.ActionType;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.AuditLog;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.TargetInfo;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.PermissionLevel;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.SystemPermission;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserPermission;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserId;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserName;
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
