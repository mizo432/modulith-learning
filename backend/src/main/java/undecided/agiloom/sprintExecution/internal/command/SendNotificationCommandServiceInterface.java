package undecided.agiloom.sprintExecution.internal.command;

import org.jspecify.annotations.NonNull;
import undecided.supporting.notification.spi.Notification;

/**
 * 通知送信コマンドサービスのインターフェースです。
 * <p>
 * UC-SP-013 通知を送信する に基づき、通知を送信します。
 * </p>
 */
public interface SendNotificationCommandServiceInterface {

    /**
     * 通知を送信します。
     *
     * @param command 通知送信コマンド
     * @return 送信された通知
     */
    Notification execute(@NonNull SendNotificationCommand command);
}
