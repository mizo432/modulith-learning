package undecided.jbJobMgmt.permissionManagement.application.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.AuditLog;
import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserPermission;
import undecided.supporting.snowflake.SnowflakeId;

public interface PermissionQuery {

  @Transactional(readOnly = true)
  List<UserPermission> findAllUserPermissions();

  @Transactional(readOnly = true)
  Optional<UserPermission> findUserPermissionById(SnowflakeId id);

  @Transactional(readOnly = true)
  List<AuditLog> findAllAuditLogs();

  @Transactional(readOnly = true)
  List<AuditLog> findAuditLogsByUserId(String userId);

  @Transactional(readOnly = true)
  List<AuditLog> findAuditLogsByPeriod(LocalDateTime from, LocalDateTime to);
}
