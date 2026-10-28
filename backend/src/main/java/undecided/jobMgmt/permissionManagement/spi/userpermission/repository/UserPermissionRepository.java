package undecided.jobMgmt.permissionManagement.spi.userpermission.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserPermission;
import undecided.jobMgmt.permissionManagement.spi.userpermission.UserId;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface UserPermissionRepository extends CrudRepository<UserPermission, SnowflakeId> {

  Optional<UserPermission> findByUserId(UserId userId);

  List<UserPermission> findByEnabled(boolean enabled);
}
