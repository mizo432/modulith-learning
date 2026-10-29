package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.sprintExecution.spi.Notification;
import undecided.supporting.snowflake.SnowflakeId;

public interface NotificationRepository extends CrudRepository<Notification, SnowflakeId>, PagingAndSortingRepository<Notification, SnowflakeId> {

    List<Notification> findByRecipientIdAndIsRead(UUID recipientId, Boolean isRead);

    List<Notification> findByProductId(UUID productId);
}
