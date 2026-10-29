package undecided.agiloom.sprintExecution.spi;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.supporting.snowflake.SnowflakeId;

public interface SprintBacklogItemRepository extends CrudRepository<SprintBacklogItem, SnowflakeId>, PagingAndSortingRepository<SprintBacklogItem, SnowflakeId> {

    List<SprintBacklogItem> findBySprintId(SnowflakeId sprintId);

    List<SprintBacklogItem> findBySprintIdAndStatus(SnowflakeId sprintId, TaskStatus status);
}
