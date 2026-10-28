package undecided.jobMgmt.jobDefinition.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * スケジュールが登録されたことを示すドメインイベントです。
 */
@Value
public class ScheduleRegisteredEvent {

  String jobCode;
  String cronExpression;
  LocalDateTime occurredAt;

  public ScheduleRegisteredEvent(String jobCode, String cronExpression) {
    this.jobCode = jobCode;
    this.cronExpression = cronExpression;
    this.occurredAt = LocalDateTime.now();
  }
}
