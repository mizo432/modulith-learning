package undecided.jbJobMgmt.jobDefinition.domain.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * サーバー配備情報が更新されたことを示すドメインイベントです。
 */
@Value
public class ServerDeploymentUpdatedEvent {

  String jobCode;
  String serverUrl;
  String version;
  LocalDateTime occurredAt;

  public ServerDeploymentUpdatedEvent(String jobCode, String serverUrl, String version) {
    this.jobCode = jobCode;
    this.serverUrl = serverUrl;
    this.version = version;
    this.occurredAt = LocalDateTime.now();
  }
}
