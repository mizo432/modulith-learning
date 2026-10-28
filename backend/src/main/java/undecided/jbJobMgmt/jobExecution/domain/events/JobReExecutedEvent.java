package undecided.jbJobMgmt.jobExecution.domain.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブが再実行されたことを示すドメインイベントです。
 */
@Value
public class JobReExecutedEvent {

  String jobCode;
  String executionId;
  String reExecutedBy;
  LocalDateTime occurredAt;

  public JobReExecutedEvent(String jobCode, String executionId, String reExecutedBy) {
    this.jobCode = jobCode;
    this.executionId = executionId;
    this.reExecutedBy = reExecutedBy;
    this.occurredAt = LocalDateTime.now();
  }
}
