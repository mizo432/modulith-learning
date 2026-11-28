package undecided.notification.internal;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.notification.spi.NotificationEntity;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知取得サービス。
 *
 * <p>指定されたユーザーの未読通知を取得し、既読更新を行います。
 */
@Service
class NotificationQueryService {

  private final NotificationEntityRepository notificationRepository;
  private final Clock clock;

  public NotificationQueryService(NotificationEntityRepository notificationRepository, Clock clock) {
    this.notificationRepository = notificationRepository;
    this.clock = clock;
  }

  /**
   * 指定されたユーザーの未読通知を取得します。
   *
   * @param recipientId 受信者ID
   * @return 未読通知のリスト
   */
  @Transactional(readOnly = true)
  public List<NotificationEntity> getUnreadNotifications(SnowflakeId recipientId) {
    return notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(recipientId);
  }

  /**
   * 指定された通知を既読としてマークします。
   *
   * @param notificationId 通知ID
   */
  @Transactional
  public void markAsRead(SnowflakeId notificationId) {
    notificationRepository
        .findById(notificationId)
        .ifPresent(notification -> {
          notification.markAsRead(LocalDateTime.now(clock));
          notificationRepository.save(notification);
        });
  }
}
