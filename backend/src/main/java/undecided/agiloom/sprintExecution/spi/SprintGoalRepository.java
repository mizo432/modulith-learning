package undecided.agiloom.sprintExecution.spi;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

public interface SprintGoalRepository extends CrudRepository<SprintGoal, SnowflakeId> {

    Optional<SprintGoal> findBySprintId(SnowflakeId sprintId);
}
