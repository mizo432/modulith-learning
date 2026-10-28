package undecided.jobmanagement.executionmonitoring.spi.notificationsetting;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_setting", schema = "job_management")
@Getter
@Setter
public class NotificationSetting {

  @Id
  private SnowflakeId id;

  @Column(name = "job_code", nullable = false, length = 50)
  private String jobCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel_type", nullable = false, length = 20)
  private NotificationChannelType channelType;

  @Column(name = "destination", nullable = false, length = 500)
  private String destination;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by", length = 50)
  private String createdBy;
}
