package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.supporting.notification.spi.Notification;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * @deprecated {@link undecided.supporting.notification.internal.NotificationRepository} を使用してください。
 * このクラスは後方互換性のために残されています。
 */
@Deprecated
public interface NotificationRepository extends CrudRepository<Notification, SnowflakeId>, PagingAndSortingRepository<Notification, SnowflakeId> {

    List<Notification> findByRecipientIdAndIsRead(UUID recipientId, Boolean isRead);

    List<Notification> findBySourceId(UUID sourceId);
}
