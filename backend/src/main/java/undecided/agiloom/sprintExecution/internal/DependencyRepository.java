package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.sprintExecution.spi.Dependency;
import undecided.supporting.snowflake.SnowflakeId;

public interface DependencyRepository extends CrudRepository<Dependency, SnowflakeId>, PagingAndSortingRepository<Dependency, SnowflakeId> {

    List<Dependency> findBySourceItemId(SnowflakeId sourceItemId);

    List<Dependency> findByTargetItemId(SnowflakeId targetItemId);
}
