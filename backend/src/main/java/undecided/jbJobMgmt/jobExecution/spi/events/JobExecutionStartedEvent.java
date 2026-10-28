package undecided.jbJobMgmt.jobExecution.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブ実行が開始されたことを示すドメインイベントです。
 */
@Value
public class JobExecutionStartedEvent {

  String jobCode;
  String executionId;
  LocalDateTime occurredAt;

  public JobExecutionStartedEvent(String jobCode, String executionId) {
    this.jobCode = jobCode;
    this.executionId = executionId;
    this.occurredAt = LocalDateTime.now();
  }
}
