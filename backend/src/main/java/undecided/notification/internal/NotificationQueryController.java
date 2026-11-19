package undecided.notification.internal;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.notification.spi.NotificationEntity;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知取得API。
 *
 * <p>フロントエンドのスナックバーが30秒ポーリングで未読通知を取得するために使用されます。
 */
@RestController
@RequestMapping("/api/notifications")
class NotificationQueryController {

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
    SnowflakeId recipientId = SnowflakeId.of(Long.parseLong(employeeId));
    List<NotificationEntity> notifications =
        notificationQueryService.getUnreadNotifications(recipientId);
    return notifications.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  private NotificationResponse toResponse(NotificationEntity entity) {
    return new NotificationResponse(
        String.valueOf(entity.getId().value()),
        String.valueOf(entity.getBacklogItemId().value()),
        entity.getTitle(),
        entity.getContent(),
        entity.getType().name(),
        entity.getIsRead(),
        entity.getCreatedAt().toString());
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
