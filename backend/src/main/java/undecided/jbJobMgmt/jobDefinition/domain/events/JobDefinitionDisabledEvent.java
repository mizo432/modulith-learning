package undecided.jbJobMgmt.jobDefinition.domain.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブ定義が無効化されたことを示すドメインイベントです。
 */
@Value
public class JobDefinitionDisabledEvent {

  String jobCode;
  LocalDateTime occurredAt;

  public JobDefinitionDisabledEvent(String jobCode) {
    this.jobCode = jobCode;
    this.occurredAt = LocalDateTime.now();
  }
}
