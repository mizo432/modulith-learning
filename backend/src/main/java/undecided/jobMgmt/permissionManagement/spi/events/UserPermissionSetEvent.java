package undecided.jobMgmt.permissionManagement.spi.events;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * ユーザー権限が設定されたことを示すドメインイベントです。
 */
@Value
public class UserPermissionSetEvent {

  String userId;
  String permissionLevel;
  LocalDateTime occurredAt;

  public UserPermissionSetEvent(String userId, String permissionLevel) {
    this.userId = userId;
    this.permissionLevel = permissionLevel;
    this.occurredAt = LocalDateTime.now();
  }
}
