package undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 通知チャネルの種類を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationChannel {

  @Column(name = "channel_type", nullable = false, length = 20)
  private String type;

  @Column(name = "channel_config", columnDefinition = "text")
  private String config;

  public NotificationChannel(String type, String config) {
    if (type == null || type.isBlank()) {
      throw new IllegalArgumentException("Channel type must not be null or blank");
    }
    if (!type.equals("email") && !type.equals("slack") && !type.equals("webhook")) {
      throw new IllegalArgumentException("Channel type must be email, slack, or webhook");
    }
    this.type = type;
    this.config = config;
  }

  public static NotificationChannel email(String to) {
    return new NotificationChannel("email", to);
  }

  public static NotificationChannel slack(String webhookUrl) {
    return new NotificationChannel("slack", webhookUrl);
  }

  public static NotificationChannel webhook(String url) {
    return new NotificationChannel("webhook", url);
  }
}
