package undecided.jobMgmt.executionMonitoring.application.command;

import org.springframework.transaction.annotation.Transactional;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationChannel;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationCondition;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationSetting;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationTarget;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationTemplate;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.repository.NotificationSettingRepository;
import undecided.supporting.snowflake.SnowflakeId;

public interface NotificationCommand {

  @Transactional
  NotificationSetting registerNotificationSetting(NotificationTarget target, NotificationChannel channel,
      NotificationCondition condition, NotificationTemplate template);

  @Transactional
  NotificationSetting changeChannel(SnowflakeId id, NotificationChannel channel);

  @Transactional
  NotificationSetting changeCondition(SnowflakeId id, NotificationCondition condition);

  @Transactional
  void deleteNotificationSetting(SnowflakeId id);
}
