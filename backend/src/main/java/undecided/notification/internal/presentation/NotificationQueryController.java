package undecided.notification.internal.presentation;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.notification.internal.NotificationEntity;
import undecided.notification.internal.NotificationQueryService;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知取得API。
 *
 * <p>フロントエンドのスナックバーが30秒ポーリングで未読通知を取得するために使用されます。
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationQueryController {

  private final NotificationQueryService notificationQueryService;

  public NotificationQueryController(NotificationQueryService notificationQueryService) {
    this.notificationQueryService = notificationQueryService;
  }

  /**
   * 指定されたユーザーの未読通知を取得します。
   *
   * @param employeeId 従業員ID
   * @return 未読通知のリスト
   */
  @GetMapping("/unread/{employeeId}")
  public List<NotificationResponse> getUnreadNotifications(@PathVariable("employeeId") String employeeId) {
    // TODO: 実装
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * 指定された通知を既読としてマークします。
   *
   * @param notificationId 通知ID
   */
  @PutMapping("/{notificationId}/read")
  public void markAsRead(@PathVariable("notificationId") String notificationId) {
    // TODO: 実装
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * 通知のレスポンスDTO。
   */
  public record NotificationResponse(
      String id,
      String backlogItemId,
      String title,
      String content,
      String type,
      boolean isRead,
      String createdAt) {}
}
