package undecided.agiloom.sprintExecution.spi;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.supporting.snowflake.SnowflakeId;

public interface NotificationRepository extends CrudRepository<Notification, SnowflakeId>, PagingAndSortingRepository<Notification, SnowflakeId> {

    List<Notification> findByRecipientIdAndIsRead(UUID recipientId, Boolean isRead);

    List<Notification> findByProductId(UUID productId);
}
