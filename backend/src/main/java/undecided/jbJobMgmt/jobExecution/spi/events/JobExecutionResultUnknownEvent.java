package undecided.jbJobMgmt.jobExecution.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブ実行結果が不明になったことを示すドメインイベントです。
 */
@Value
public class JobExecutionResultUnknownEvent {

  String jobCode;
  String executionId;
  LocalDateTime occurredAt;

  public JobExecutionResultUnknownEvent(String jobCode, String executionId) {
    this.jobCode = jobCode;
    this.executionId = executionId;
    this.occurredAt = LocalDateTime.now();
  }
}
