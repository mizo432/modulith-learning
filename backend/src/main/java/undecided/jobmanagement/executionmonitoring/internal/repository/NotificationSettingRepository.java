package undecided.jobmanagement.executionmonitoring.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.jobmanagement.executionmonitoring.spi.notificationsetting.NotificationSetting;
import undecided.supporting.snowflake.SnowflakeId;

import java.util.List;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, SnowflakeId> {
  
  List<NotificationSetting> findByJobCode(String jobCode);
  
  List<NotificationSetting> findByIsActive(Boolean isActive);
}
