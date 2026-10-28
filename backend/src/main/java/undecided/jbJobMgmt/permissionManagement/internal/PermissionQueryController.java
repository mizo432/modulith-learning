package undecided.jbJobMgmt.permissionManagement.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jbJobMgmt.permissionManagement.application.query.PermissionQuery;
import undecided.jbJobMgmt.permissionManagement.presentation.dto.AuditLogResponse;
import undecided.jbJobMgmt.permissionManagement.presentation.dto.UserPermissionResponse;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
public class PermissionQueryController {

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

  // --- Audit Log ---

  @GetMapping("/audit-logs")
  public List<AuditLogResponse> findAllAuditLogs() {
    return permissionQuery.findAllAuditLogs().stream()
        .map(AuditLogResponse::from)
        .toList();
  }
}
