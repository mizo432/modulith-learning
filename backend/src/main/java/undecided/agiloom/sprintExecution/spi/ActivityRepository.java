package undecided.agiloom.sprintExecution.spi;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.supporting.snowflake.SnowflakeId;

public interface ActivityRepository extends CrudRepository<Activity, SnowflakeId>, PagingAndSortingRepository<Activity, SnowflakeId> {

    List<Activity> findBySprintId(SnowflakeId sprintId);
}
