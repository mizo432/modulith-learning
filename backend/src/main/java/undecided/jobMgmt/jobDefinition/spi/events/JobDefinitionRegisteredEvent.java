package undecided.jobMgmt.jobDefinition.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブ定義が登録されたことを示すドメインイベントです。
 */
@Value
public class JobDefinitionRegisteredEvent {

  String jobCode;
  String jobName;
  String version;
  LocalDateTime occurredAt;

  public JobDefinitionRegisteredEvent(String jobCode, String jobName, String version) {
    this.jobCode = jobCode;
    this.jobName = jobName;
    this.version = version;
    this.occurredAt = LocalDateTime.now();
  }
}
