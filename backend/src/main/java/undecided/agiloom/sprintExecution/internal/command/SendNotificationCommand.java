package undecided.agiloom.sprintExecution.internal.command;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.notification.spi.NotificationType;

/**
 * 通知送信コマンドです。
 * <p>
 * UC-SP-013 通知を送信する に基づき、メンション、担当変更、期限、判断待ちなどの通知を送信します。
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationCommand {

    /**
     * 通知の受信者ID（従業員ID）
     */
    @NonNull
    private UUID recipientId;

    /**
     * 通知の送信元となるリソースのID（例: タスクID、スプリントIDなど）
     */
    private UUID sourceId;

    /**
     * 通知のタイトル
     */
    @NonNull
    private String title;

    /**
     * 通知の本文
     */
    @NonNull
    private String content;

    /**
     * 通知の種類（メンション、担当変更、期限、判断待ち）
     */
    @NonNull
    private NotificationType type;
}
