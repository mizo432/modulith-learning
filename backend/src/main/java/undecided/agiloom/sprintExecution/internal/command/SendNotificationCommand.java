package undecided.agiloom.sprintExecution.internal.command;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.notification.spi.NotificationType;

/**
 * 通知送信コマンドです。
 * <p>
 * UC-SP-013 通知を送信する に基づき、メンション、担当変更、期限、判断待ちなどの通知を送信します。
 * このクラスは不変であり、コマンドの作成後の状態変更はできません。
 * </p>
 */
@Getter
@Builder
public class SendNotificationCommand {

    /**
     * 通知の受信者ID（従業員ID）
     */
    @NonNull
    private final UUID recipientId;

    /**
     * 通知の送信元となるリソースのID（例: タスクID、スプリントIDなど）
     */
    @Nullable
    private final UUID sourceId;

    /**
     * 通知のタイトル
     */
    @NonNull
    private final String title;

    /**
     * 通知の本文
     */
    @NonNull
    private final String content;

    /**
     * 通知の種類（メンション、担当変更、期限、判断待ち）
     */
    @NonNull
    private final NotificationType type;
}
