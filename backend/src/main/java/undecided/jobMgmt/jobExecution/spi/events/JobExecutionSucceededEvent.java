package undecided.jobMgmt.jobExecution.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブ実行が成功したことを示すドメインイベントです。
 */
@Value
public class JobExecutionSucceededEvent {

  String jobCode;
  String executionId;
  LocalDateTime occurredAt;

  public JobExecutionSucceededEvent(String jobCode, String executionId) {
    this.jobCode = jobCode;
    this.executionId = executionId;
    this.occurredAt = LocalDateTime.now();
  }
}
