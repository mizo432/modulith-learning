package undecided.supporting.notification.internal;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.notification.spi.Notification;
import undecided.supporting.notification.spi.NotificationRequest;
import undecided.supporting.notification.spi.NotificationService;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * 通知サービスの実装クラスです。
 * <p>
 * 各事業領域から注入して、システム内通知を送信できます。
 * </p>
 *
 * @see NotificationService
 * @see NotificationRequest
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SnowflakeIdGenerator snowflakeIdGenerator) {
        this.notificationRepository = notificationRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    @Override
    public Notification send(NotificationRequest request) {
        Notification notification = Notification.builder()
            .id(SnowflakeId.of(snowflakeIdGenerator.nextId()))
            .recipientId(request.getRecipientId())
            .sourceId(request.getSourceId())
            .sourceDomain(request.getSourceDomain())
            .title(request.getTitle())
            .content(request.getContent())
            .type(request.getType())
            .build();

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByRecipient(UUID recipientId) {
        return notificationRepository.findByRecipientId(recipientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotificationsByRecipient(UUID recipientId) {
        return notificationRepository.findByRecipientIdAndIsRead(recipientId, false);
    }

    @Override
    public void markAsRead(String notificationId) {
        SnowflakeId id = SnowflakeId.of(Long.parseLong(notificationId));
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
