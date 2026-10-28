package undecided.jobMgmt.jobExecution.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブが手動起動されたことを示すドメインイベントです。
 */
@Value
public class JobManuallyStartedEvent {

  String jobCode;
  String executedBy;
  LocalDateTime occurredAt;

  public JobManuallyStartedEvent(String jobCode, String executedBy) {
    this.jobCode = jobCode;
    this.executedBy = executedBy;
    this.occurredAt = LocalDateTime.now();
  }
}
