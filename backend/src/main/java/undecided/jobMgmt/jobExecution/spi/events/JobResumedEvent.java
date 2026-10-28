package undecided.jobMgmt.jobExecution.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブが途中再開されたことを示すドメインイベントです。
 */
@Value
public class JobResumedEvent {

  String jobCode;
  String executionId;
  String resumedBy;
  LocalDateTime occurredAt;

  public JobResumedEvent(String jobCode, String executionId, String resumedBy) {
    this.jobCode = jobCode;
    this.executionId = executionId;
    this.resumedBy = resumedBy;
    this.occurredAt = LocalDateTime.now();
  }
}
