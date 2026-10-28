package undecided.jobMgmt.permissionManagement.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jobMgmt.permissionManagement.application.command.PermissionCommand;
import undecided.jobMgmt.permissionManagement.presentation.dto.AuditLogResponse;
import undecided.jobMgmt.permissionManagement.presentation.dto.UserPermissionRequest;
import undecided.jobMgmt.permissionManagement.presentation.dto.UserPermissionResponse;
import undecided.jobMgmt.permissionManagement.spi.auditlog.ActionResult;
import undecided.jobMgmt.permissionManagement.spi.auditlog.ActionType;
import undecided.jobMgmt.permissionManagement.spi.auditlog.AuditLog;
import undecided.jobMgmt.permissionManagement.spi.auditlog.TargetInfo;
import undecided.jobMgmt.permissionManagement.spi.userpermission.PermissionLevel;
import undecided.jobMgmt.permissionManagement.spi.userpermission.SystemPermission;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserPermission;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserId;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserName;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
public class PermissionCommandController {

  private final PermissionCommand permissionCommand;

  // --- User Permission ---

  @PostMapping("/users")
  public ResponseEntity<UserPermissionResponse> registerUser(@RequestBody UserPermissionRequest request) {
    UserId userId = new UserId(request.getUserId());
    UserName userName = new UserName(request.getUserName());
    PermissionLevel permissionLevel = new PermissionLevel(request.getPermissionLevel());
    SystemPermission systemPermission = new SystemPermission(request.getSystemPermission());

    UserPermission up = permissionCommand.registerUserPermission(userId, userName, permissionLevel, systemPermission);
    return ResponseEntity.ok(UserPermissionResponse.from(up));
  }

  @PutMapping("/users/{id}/level")
  public ResponseEntity<UserPermissionResponse> changePermissionLevel(@PathVariable String id,
      @RequestBody UserPermissionRequest request) {
    PermissionLevel permissionLevel = new PermissionLevel(request.getPermissionLevel());
    UserPermission up = permissionCommand.changePermissionLevel(SnowflakeId.of(Long.parseLong(id)), permissionLevel);
    return ResponseEntity.ok(UserPermissionResponse.from(up));
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    permissionCommand.deleteUserPermission(SnowflakeId.of(Long.parseLong(id)));
    return ResponseEntity.noContent().build();
  }

  // --- Audit Log ---

  @PostMapping("/audit-logs")
  public ResponseEntity<AuditLogResponse> logAction(@RequestBody AuditLogResponse request) {
    ActionType actionType = new ActionType(request.getActionType());
    TargetInfo targetInfo = new TargetInfo(request.getTargetType(), request.getTargetId());
    ActionResult actionResult = new ActionResult(request.getResult(), request.getMessage());

    AuditLog auditLog = permissionCommand.logAction(actionType, targetInfo, actionResult,
        request.getUserId(), request.getIpAddress());
    return ResponseEntity.ok(AuditLogResponse.from(auditLog));
  }
}
