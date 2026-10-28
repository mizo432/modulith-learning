package undecided.jobMgmt.executionMonitoring.application.query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationSetting;
import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.repository.NotificationSettingRepository;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@RequiredArgsConstructor
public class NotificationQueryImpl implements NotificationQuery {

  private final NotificationSettingRepository notificationSettingRepository;

  @Override
  @Transactional(readOnly = true)
  public List<NotificationSetting> findAllNotificationSettings() {
    return StreamSupport.stream(notificationSettingRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<NotificationSetting> findNotificationSettingById(SnowflakeId id) {
    return notificationSettingRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<NotificationSetting> findByTarget(String targetType, String targetId) {
    return notificationSettingRepository.findByTargetTargetTypeAndTargetTargetId(targetType, targetId);
  }
}
