package undecided.jbJobMgmt.permissionManagement.domain.auditlog.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jbJobMgmt.permissionManagement.domain.auditlog.AuditLog;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface AuditLogRepository extends CrudRepository<AuditLog, SnowflakeId> {

  List<AuditLog> findByUserId(String userId);

  List<AuditLog> findByActionTypeType(String actionType);

  List<AuditLog> findByPerformedAtBetween(LocalDateTime from, LocalDateTime to);
}
