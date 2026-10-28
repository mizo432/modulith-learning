package undecided.jobMgmt.executionMonitoring.spi.notificationsetting.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationSetting;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface NotificationSettingRepository extends CrudRepository<NotificationSetting, SnowflakeId> {

  List<NotificationSetting> findByTargetTargetTypeAndTargetTargetId(String targetType, String targetId);

  List<NotificationSetting> findByEnabled(boolean enabled);
}
