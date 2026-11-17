package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.sprintExecution.spi.Activity;
import undecided.supporting.snowflake.SnowflakeId;

public interface ActivityRepository extends CrudRepository<Activity, SnowflakeId>, PagingAndSortingRepository<Activity, SnowflakeId> {

    List<Activity> findBySprintId(SnowflakeId sprintId);
}
