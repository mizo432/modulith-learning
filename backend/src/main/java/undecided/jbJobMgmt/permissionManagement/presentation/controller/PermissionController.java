package undecided.jbJobMgmt.permissionManagement.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jbJobMgmt.permissionManagement.application.command.PermissionCommand;
import undecided.jbJobMgmt.permissionManagement.application.query.PermissionQuery;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.ActionResult;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.ActionType;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.AuditLog;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.TargetInfo;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.PermissionLevel;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.SystemPermission;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserPermission;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserId;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserName;
import undecided.jbJobMgmt.permissionManagement.presentation.dto.AuditLogResponse;
import undecided.jbJobMgmt.permissionManagement.presentation.dto.UserPermissionRequest;
import undecided.jbJobMgmt.permissionManagement.presentation.dto.UserPermissionResponse;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
public class PermissionController {

  private final PermissionCommand permissionCommand;
  private final PermissionQuery permissionQuery;

  // --- User Permission ---

  @GetMapping("/users")
  public List<UserPermissionResponse> findAllUsers() {
    return permissionQuery.findAllUserPermissions().stream()
        .map(UserPermissionResponse::from)
        .toList();
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<UserPermissionResponse> findUserById(@PathVariable String id) {
    return permissionQuery.findUserPermissionById(SnowflakeId.of(Long.parseLong(id)))
        .map(up -> ResponseEntity.ok(UserPermissionResponse.from(up)))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/users")
  public UserPermissionResponse registerUser(@RequestBody UserPermissionRequest request) {
    UserId userId = new UserId(request.getUserId());
    UserName userName = new UserName(request.getUserName());
    PermissionLevel permissionLevel = new PermissionLevel(request.getPermissionLevel());
    SystemPermission systemPermission = new SystemPermission(request.getSystemPermission());

    UserPermission up = permissionCommand.registerUserPermission(userId, userName, permissionLevel, systemPermission);
    return UserPermissionResponse.from(up);
  }

  @PutMapping("/users/{id}/level")
  public UserPermissionResponse changePermissionLevel(@PathVariable String id,
      @RequestBody UserPermissionRequest request) {
    PermissionLevel permissionLevel = new PermissionLevel(request.getPermissionLevel());
    UserPermission up = permissionCommand.changePermissionLevel(SnowflakeId.of(Long.parseLong(id)), permissionLevel);
    return UserPermissionResponse.from(up);
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    permissionCommand.deleteUserPermission(SnowflakeId.of(Long.parseLong(id)));
    return ResponseEntity.noContent().build();
  }

  // --- Audit Log ---

  @GetMapping("/audit-logs")
  public List<AuditLogResponse> findAllAuditLogs() {
    return permissionQuery.findAllAuditLogs().stream()
        .map(AuditLogResponse::from)
        .toList();
  }

  @PostMapping("/audit-logs")
  public AuditLogResponse logAction(@RequestBody AuditLogResponse request) {
    ActionType actionType = new ActionType(request.getActionType());
    TargetInfo targetInfo = new TargetInfo(request.getTargetType(), request.getTargetId());
    ActionResult actionResult = new ActionResult(request.getResult(), request.getMessage());

    AuditLog auditLog = permissionCommand.logAction(actionType, targetInfo, actionResult,
        request.getUserId(), request.getIpAddress());
    return AuditLogResponse.from(auditLog);
  }
}
