package undecided.jobMgmt.executionMonitoring.spi.notificationsetting;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知設定を管理するルートエンティティです。
 */
@Entity
@Table(name = "notification_setting")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSetting {

  @jakarta.persistence.Id
  private SnowflakeId id;

  @Embedded
  private NotificationTarget target;

  @Embedded
  private NotificationChannel channel;

  @Embedded
  private NotificationCondition condition;

  @Embedded
  private NotificationTemplate template;

  @jakarta.persistence.Column(name = "enabled", nullable = false)
  private boolean enabled;

  @jakarta.persistence.Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @jakarta.persistence.Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public NotificationSetting(NotificationTarget target, NotificationChannel channel,
      NotificationCondition condition, NotificationTemplate template) {
    this.id = SnowflakeId.of(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    this.target = target;
    this.channel = channel;
    this.condition = condition;
    this.template = template != null ? template : NotificationTemplate.defaultJobNotification();
    this.enabled = true;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public void changeChannel(NotificationChannel channel) {
    if (channel == null) {
      throw new IllegalArgumentException("Channel must not be null");
    }
    this.channel = channel;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeCondition(NotificationCondition condition) {
    if (condition == null) {
      throw new IllegalArgumentException("Condition must not be null");
    }
    this.condition = condition;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeTemplate(NotificationTemplate template) {
    if (template == null) {
      throw new IllegalArgumentException("Template must not be null");
    }
    this.template = template;
    this.updatedAt = LocalDateTime.now();
  }

  public void enable() {
    this.enabled = true;
    this.updatedAt = LocalDateTime.now();
  }

  public void disable() {
    this.enabled = false;
    this.updatedAt = LocalDateTime.now();
  }
}
