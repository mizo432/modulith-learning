package undecided.jbJobMgmt.executionMonitoring.application.command;

import org.springframework.transaction.annotation.Transactional;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationChannel;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationCondition;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationSetting;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationTarget;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationTemplate;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.repository.NotificationSettingRepository;
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
