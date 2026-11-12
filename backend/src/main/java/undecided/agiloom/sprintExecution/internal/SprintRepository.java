package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

public interface SprintRepository extends CrudRepository<Sprint, SnowflakeId>, PagingAndSortingRepository<Sprint, SnowflakeId> {

    List<Sprint> findByProductId(UUID productId);

    Optional<Sprint> findByProductIdAndSprintNumber(UUID productId, Integer sprintNumber);

    List<Sprint> findByTeamId(UUID teamId);

    List<Sprint> findByStatus(SprintStatus status);
}
