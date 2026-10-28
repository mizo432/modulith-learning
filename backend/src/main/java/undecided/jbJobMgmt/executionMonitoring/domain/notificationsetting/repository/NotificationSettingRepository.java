package undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting.NotificationSetting;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface NotificationSettingRepository extends CrudRepository<NotificationSetting, SnowflakeId> {

  List<NotificationSetting> findByTargetTargetTypeAndTargetTargetId(String targetType, String targetId);

  List<NotificationSetting> findByEnabled(boolean enabled);
}
