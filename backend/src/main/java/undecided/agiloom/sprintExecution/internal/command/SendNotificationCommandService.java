package undecided.agiloom.sprintExecution.internal.command;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import undecided.supporting.notification.spi.NotificationDTO;
import undecided.supporting.notification.spi.NotificationRequest;
import undecided.supporting.notification.spi.NotificationService;

/**
 * 通知送信コマンドサービスの実装クラスです。
 * <p>
 * UC-SP-013 通知を送信する に基づき、NotificationService を通じて通知を送信します。
 * トランザクション管理は NotificationService 側で行うため、このクラスでは @Transactional を付与しません。
 * </p>
 */
@Service
public class SendNotificationCommandService implements SendNotificationCommandServiceInterface {

    private static final String SOURCE_DOMAIN = "agiloom";

    private final NotificationService notificationService;

    public SendNotificationCommandService(@NonNull NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public NotificationDTO execute(@NonNull SendNotificationCommand command) {
        NotificationRequest request = NotificationRequest.builder(SOURCE_DOMAIN)
                .recipientId(command.getRecipientId())
                .sourceId(command.getSourceId())
                .title(command.getTitle())
                .content(command.getContent())
                .type(command.getType())
                .build();

        return notificationService.send(request);
    }
}
