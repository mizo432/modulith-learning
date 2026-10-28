package undecided.jbJobMgmt.jobExecution.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * スケジュール起動が実行されたことを示すドメインイベントです。
 */
@Value
public class JobScheduleStartedEvent {

  String jobCode;
  LocalDateTime occurredAt;

  public JobScheduleStartedEvent(String jobCode) {
    this.jobCode = jobCode;
    this.occurredAt = LocalDateTime.now();
  }
}
