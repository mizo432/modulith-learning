package undecided.agiloom.sprintExecution.internal;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.agiloom.sprintExecution.spi.SprintGoal;
import undecided.supporting.snowflake.SnowflakeId;

public interface SprintGoalRepository extends CrudRepository<SprintGoal, SnowflakeId> {

    Optional<SprintGoal> findBySprintId(SnowflakeId sprintId);
}
