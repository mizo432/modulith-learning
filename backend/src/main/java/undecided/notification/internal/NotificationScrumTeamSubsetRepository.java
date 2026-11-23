package undecided.notification.internal;

import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

interface NotificationScrumTeamSubsetRepository
    extends CrudRepository<NotificationScrumTeamSubset, SnowflakeId> {}