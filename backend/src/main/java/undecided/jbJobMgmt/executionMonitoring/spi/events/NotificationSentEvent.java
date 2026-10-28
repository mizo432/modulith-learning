package undecided.jbJobMgmt.executionMonitoring.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * 通知が送信されたことを示すドメインイベントです。
 */
@Value
public class NotificationSentEvent {

  String targetType;
  String targetId;
  String channelType;
  LocalDateTime occurredAt;

  public NotificationSentEvent(String targetType, String targetId, String channelType) {
    this.targetType = targetType;
    this.targetId = targetId;
    this.channelType = channelType;
    this.occurredAt = LocalDateTime.now();
  }
}
