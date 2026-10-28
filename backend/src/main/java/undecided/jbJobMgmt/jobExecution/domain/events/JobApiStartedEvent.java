package undecided.jbJobMgmt.jobExecution.domain.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * API経由でジョブ起動が要求されたことを示すドメインイベントです。
 */
@Value
public class JobApiStartedEvent {

  String jobCode;
  String apiCaller;
  LocalDateTime occurredAt;

  public JobApiStartedEvent(String jobCode, String apiCaller) {
    this.jobCode = jobCode;
    this.apiCaller = apiCaller;
    this.occurredAt = LocalDateTime.now();
  }
}
