package undecided.supporting.notification.internal;

import java.util.List;
import java.util.UUID;
import com.github.mizo432.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.notification.spi.Notification;
import undecided.supporting.notification.spi.NotificationDTO;
import undecided.supporting.notification.spi.NotificationRequest;
import undecided.supporting.notification.spi.NotificationService;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * 通知サービスの実装クラスです。
 * <p>
 * 各事業領域から注入して、システム内通知を送信できます。
 * JPAエンティティとSPIのDTO間で変換を行います。
 * </p>
 *
 * @see NotificationService
 * @see NotificationRequest
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SnowflakeIdGenerator snowflakeIdGenerator) {
        this.notificationRepository = notificationRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    @Override
    @Transactional
    public NotificationDTO send(NotificationRequest request) {
        Notification notification = Notification.builder()
            .id(SnowflakeId.of(snowflakeIdGenerator.nextId()))
            .recipientId(request.getRecipientId())
            .sourceId(request.getSourceId())
            .sourceDomain(request.getSourceDomain())
            .title(request.getTitle())
            .content(request.getContent())
            .type(request.getType())
            .build();

        Notification saved = notificationRepository.save(notification);
        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByRecipient(UUID recipientId) {
        return notificationRepository.findByRecipientIdAndDeletedAtIsNull(recipientId).stream()
            .map(this::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotificationsByRecipient(UUID recipientId) {
        return notificationRepository.findByRecipientIdAndIsReadAndDeletedAtIsNull(recipientId, false).stream()
            .map(this::toDTO)
            .toList();
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        if (notificationId == null) {
            throw new BusinessException("notificationIdはnullであってはいけません");
        }
        long idValue;
        try {
            idValue = Long.parseLong(notificationId);
        } catch (NumberFormatException e) {
            throw new BusinessException("通知IDは数値である必要があります: " + notificationId);
        }
        SnowflakeId id = SnowflakeId.of(idValue);
        Notification notification = notificationRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new BusinessException("通知が見つかりません: " + notificationId));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    /**
     * JPAエンティティをSPIのDTOに変換します。
     */
    private NotificationDTO toDTO(Notification entity) {
        return NotificationDTO.builder()
            .id(entity.getId())
            .recipientId(entity.getRecipientId())
            .sourceId(entity.getSourceId())
            .sourceDomain(entity.getSourceDomain())
            .title(entity.getTitle())
            .content(entity.getContent())
            .type(entity.getType())
            .isRead(entity.getIsRead())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
