package undecided.notification.internal;

import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

interface NotificationEmployeeEmailSubsetRepository
    extends CrudRepository<NotificationEmployeeEmailSubset, SnowflakeId> {}