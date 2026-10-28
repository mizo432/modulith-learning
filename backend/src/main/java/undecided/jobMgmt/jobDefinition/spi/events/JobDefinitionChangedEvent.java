package undecided.jobMgmt.jobDefinition.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ジョブ定義が変更されたことを示すドメインイベントです。
 */
@Value
public class JobDefinitionChangedEvent {

  String jobCode;
  String version;
  LocalDateTime occurredAt;

  public JobDefinitionChangedEvent(String jobCode, String version) {
    this.jobCode = jobCode;
    this.version = version;
    this.occurredAt = LocalDateTime.now();
  }
}
