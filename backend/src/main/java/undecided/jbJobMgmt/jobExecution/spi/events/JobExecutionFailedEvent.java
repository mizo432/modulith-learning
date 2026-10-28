package undecided.jbJobMgmt.jobExecution.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブ実行が失敗したことを示すドメインイベントです。
 */
@Value
public class JobExecutionFailedEvent {

  String jobCode;
  String executionId;
  String errorMessage;
  LocalDateTime occurredAt;

  public JobExecutionFailedEvent(String jobCode, String executionId, String errorMessage) {
    this.jobCode = jobCode;
    this.executionId = executionId;
    this.errorMessage = errorMessage;
    this.occurredAt = LocalDateTime.now();
  }
}
