package undecided.notification.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知コマンドAPI（既読更新など）。
 *
 * <p>CQRSパターンに従い、Command操作のみを扱います。
 */
@RestController
@RequestMapping("/api/notifications")
class NotificationCommandController {

  private final NotificationQueryService notificationQueryService;

  NotificationCommandController(NotificationQueryService notificationQueryService) {
    this.notificationQueryService = notificationQueryService;
  }

  /**
   * 指定された通知を既読としてマークします。
   *
   * @param notificationId 通知ID
   * @return 204 No Content
   */
  @PutMapping("/{notificationId}/read")
  public ResponseEntity<Void> markAsRead(@PathVariable("notificationId") String notificationId) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(notificationId));
    notificationQueryService.markAsRead(id);
    return ResponseEntity.noContent().build();
  }
}
