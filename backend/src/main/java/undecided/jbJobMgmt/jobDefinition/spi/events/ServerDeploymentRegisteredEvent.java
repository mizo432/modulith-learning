package undecided.jbJobMgmt.jobDefinition.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * サーバー配備情報が登録されたことを示すドメインイベントです。
 */
@Value
public class ServerDeploymentRegisteredEvent {

  String jobCode;
  String serverUrl;
  String version;
  LocalDateTime occurredAt;

  public ServerDeploymentRegisteredEvent(String jobCode, String serverUrl, String version) {
    this.jobCode = jobCode;
    this.serverUrl = serverUrl;
    this.version = version;
    this.occurredAt = LocalDateTime.now();
  }
}
