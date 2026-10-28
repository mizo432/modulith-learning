package undecided.jbJobMgmt.jobExecution.domain.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブがキャンセルされたことを示すドメインイベントです。
 */
@Value
public class JobCancelledEvent {

  String jobCode;
  String executionId;
  String cancelledBy;
  LocalDateTime occurredAt;

  public JobCancelledEvent(String jobCode, String executionId, String cancelledBy) {
    this.jobCode = jobCode;
    this.executionId = executionId;
    this.cancelledBy = cancelledBy;
    this.occurredAt = LocalDateTime.now();
  }
}
