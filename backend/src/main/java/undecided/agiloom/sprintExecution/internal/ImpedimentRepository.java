package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.sprintExecution.spi.Impediment;
import undecided.agiloom.sprintExecution.spi.ImpedimentStatus;
import undecided.supporting.snowflake.SnowflakeId;

public interface ImpedimentRepository extends CrudRepository<Impediment, SnowflakeId>, PagingAndSortingRepository<Impediment, SnowflakeId> {

    List<Impediment> findBySprintId(SnowflakeId sprintId);

    List<Impediment> findBySprintIdAndStatus(SnowflakeId sprintId, ImpedimentStatus status);
}
