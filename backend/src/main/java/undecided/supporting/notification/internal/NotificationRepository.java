package undecided.supporting.notification.internal;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.supporting.notification.spi.Notification;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知の永続化を行うリポジトリです。
 */
public interface NotificationRepository
    extends CrudRepository<Notification, SnowflakeId>,
        PagingAndSortingRepository<Notification, SnowflakeId> {

    /**
     * 受信者IDと既読フラグで通知を検索します。
     *
     * @param recipientId 受信者ID
     * @param isRead 既読フラグ
     * @return 該当する通知のリスト
     */
    List<Notification> findByRecipientIdAndIsRead(UUID recipientId, Boolean isRead);

    /**
     * 送信元のIDで通知を検索します。
     *
     * @param sourceId 送信元のID
     * @return 該当する通知のリスト
     */
    List<Notification> findBySourceId(UUID sourceId);

    /**
     * 送信元の事業領域名で通知を検索します。
     *
     * @param sourceDomain 送信元の事業領域名
     * @return 該当する通知のリスト
     */
    List<Notification> findBySourceDomain(String sourceDomain);

    /**
     * 受信者IDで通知を検索します。
     *
     * @param recipientId 受信者ID
     * @return 該当する通知のリスト
     */
    List<Notification> findByRecipientId(UUID recipientId);
}
