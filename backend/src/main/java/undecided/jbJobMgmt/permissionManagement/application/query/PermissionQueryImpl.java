package undecided.jbJobMgmt.permissionManagement.application.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.jbJobMgmt.permissionManagement.spi.auditlog.AuditLog;
import undecided.jbJobMgmt.permissionManagement.spi.auditlog.repository.AuditLogRepository;
import undecided.jbJobMgmt.permissionManagement.spi.userpermission.UserPermission;
import undecided.jbJobMgmt.permissionManagement.spi.userpermission.repository.UserPermissionRepository;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@RequiredArgsConstructor
public class PermissionQueryImpl implements PermissionQuery {

  private final UserPermissionRepository userPermissionRepository;
  private final AuditLogRepository auditLogRepository;

  @Override
  @Transactional(readOnly = true)
  public List<UserPermission> findAllUserPermissions() {
    return StreamSupport.stream(userPermissionRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserPermission> findUserPermissionById(SnowflakeId id) {
    return userPermissionRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuditLog> findAllAuditLogs() {
    return StreamSupport.stream(auditLogRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuditLog> findAuditLogsByUserId(String userId) {
    return auditLogRepository.findByUserId(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuditLog> findAuditLogsByPeriod(LocalDateTime from, LocalDateTime to) {
    return auditLogRepository.findByPerformedAtBetween(from, to);
  }
}
