package undecided.jbJobMgmt.executionMonitoring.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationChannel;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationCondition;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationSetting;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationTarget;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.NotificationTemplate;
import undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting.repository.NotificationSettingRepository;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@RequiredArgsConstructor
public class NotificationCommandImpl implements NotificationCommand {

  private final NotificationSettingRepository notificationSettingRepository;

  @Override
  public NotificationSetting registerNotificationSetting(NotificationTarget target, NotificationChannel channel,
      NotificationCondition condition, NotificationTemplate template) {
    NotificationSetting notificationSetting = new NotificationSetting(target, channel, condition, template);
    return notificationSettingRepository.save(notificationSetting);
  }

  @Override
  public NotificationSetting changeChannel(SnowflakeId id, NotificationChannel channel) {
    NotificationSetting notificationSetting = notificationSettingRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("NotificationSetting not found: " + id));
    notificationSetting.changeChannel(channel);
    return notificationSettingRepository.save(notificationSetting);
  }

  @Override
  public NotificationSetting changeCondition(SnowflakeId id, NotificationCondition condition) {
    NotificationSetting notificationSetting = notificationSettingRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("NotificationSetting not found: " + id));
    notificationSetting.changeCondition(condition);
    return notificationSettingRepository.save(notificationSetting);
  }

  @Override
  public void deleteNotificationSetting(SnowflakeId id) {
    if (!notificationSettingRepository.existsById(id)) {
      throw new IllegalArgumentException("NotificationSetting not found: " + id);
    }
    notificationSettingRepository.deleteById(id);
  }
}
