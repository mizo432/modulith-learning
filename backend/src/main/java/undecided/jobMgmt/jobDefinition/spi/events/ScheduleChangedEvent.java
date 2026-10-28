package undecided.jobMgmt.jobDefinition.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * スケジュールが変更されたことを示すドメインイベントです。
 */
@Value
public class ScheduleChangedEvent {

  String jobCode;
  String cronExpression;
  LocalDateTime occurredAt;

  public ScheduleChangedEvent(String jobCode, String cronExpression) {
    this.jobCode = jobCode;
    this.cronExpression = cronExpression;
    this.occurredAt = LocalDateTime.now();
  }
}
