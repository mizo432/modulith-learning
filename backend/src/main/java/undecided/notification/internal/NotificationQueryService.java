package undecided.notification.internal;

import java.util.List;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知取得サービス。
 *
 * <p>指定されたユーザーの未読通知を取得し、既読更新を行います。
 */
public class NotificationQueryService {

  private final NotificationRepository notificationRepository;

  public NotificationQueryService(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  /**
   * 指定されたユーザーの未読通知を取得します。
   *
   * @param recipientId 受信者ID
   * @return 未読通知のリスト
   */
  public List<NotificationEntity> getUnreadNotifications(SnowflakeId recipientId) {
    // TODO: 実装
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * 指定された通知を既読としてマークします。
   *
   * @param notificationId 通知ID
   */
  public void markAsRead(SnowflakeId notificationId) {
    // TODO: 実装
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
