package undecided.supporting.notification.internal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知の永続化を行うリポジトリです。
 */
public interface NotificationRepository
    extends CrudRepository<Notification, SnowflakeId>,
        PagingAndSortingRepository<Notification, SnowflakeId> {

    /**
     * 受信者IDと既読フラグで通知を検索します（論理削除された通知は除外）。
     *
     * @param recipientId 受信者ID
     * @param isRead 既読フラグ
     * @return 該当する通知のリスト
     */
    List<Notification> findByRecipientIdAndIsReadAndDeletedAtIsNull(UUID recipientId, Boolean isRead);

    /**
     * 送信元のIDで通知を検索します（論理削除された通知は除外）。
     *
     * @param sourceId 送信元のID
     * @return 該当する通知のリスト
     */
    List<Notification> findBySourceIdAndDeletedAtIsNull(UUID sourceId);

    /**
     * 送信元の事業領域名で通知を検索します（論理削除された通知は除外）。
     *
     * @param sourceDomain 送信元の事業領域名
     * @return 該当する通知のリスト
     */
    List<Notification> findBySourceDomainAndDeletedAtIsNull(String sourceDomain);

    /**
     * 受信者IDで通知を検索します（論理削除された通知は除外）。
     *
     * @param recipientId 受信者ID
     * @return 該当する通知のリスト
     */
    List<Notification> findByRecipientIdAndDeletedAtIsNull(UUID recipientId);

    /**
     * IDで通知を検索します（論理削除された通知は除外）。
     *
     * @param id 通知のID
     * @return 該当する通知
     */
    Optional<Notification> findByIdAndDeletedAtIsNull(SnowflakeId id);
}
