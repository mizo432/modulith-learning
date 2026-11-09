package undecided.supporting.notification.spi;

import java.util.List;
import java.util.UUID;

/**
 * 通知サービスインターフェースです。
 * <p>
 * 各事業領域からこのインターフェースを注入して、
 * システム内通知を送信できます。
 * </p>
 *
 * @see NotificationRequest
 * @see NotificationDTO
 */
public interface NotificationService {

    /**
     * 通知を送信します。
     * <p>
     * 指定された通知リクエストに基づいて通知を作成し、データベースに保存します。
     * </p>
     *
     * @param request 通知リクエスト
     * @return 作成された通知（不変のDTO）
     */
    NotificationDTO send(NotificationRequest request);

    /**
     * 受信者の通知一覧を取得します。
     *
     * @param recipientId 受信者ID
     * @return 通知のリスト（不変のDTO）
     */
    List<NotificationDTO> getNotificationsByRecipient(UUID recipientId);

    /**
     * 受信者の未読通知一覧を取得します。
     *
     * @param recipientId 受信者ID
     * @return 未読通知のリスト（不変のDTO）
     */
    List<NotificationDTO> getUnreadNotificationsByRecipient(UUID recipientId);

    /**
     * 通知を既読としてマークします。
     *
     * @param notificationId 通知ID（数値文字列）
     * @throws BusinessException notificationIdが数値として解析できない場合、または通知が見つからない場合
     */
    void markAsRead(String notificationId);
}
