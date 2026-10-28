package undecided.jobMgmt.executionMonitoring.application.query;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationSetting;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.repository.NotificationSettingRepository;
import undecided.supporting.snowflake.SnowflakeId;

public interface NotificationQuery {

  @Transactional(readOnly = true)
  List<NotificationSetting> findAllNotificationSettings();

  @Transactional(readOnly = true)
  Optional<NotificationSetting> findNotificationSettingById(SnowflakeId id);

  @Transactional(readOnly = true)
  List<NotificationSetting> findByTarget(String targetType, String targetId);
}
