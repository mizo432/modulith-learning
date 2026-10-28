package undecided.jbJobMgmt.jobDefinition.spi.schedule.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobCode;
import undecided.jbJobMgmt.jobDefinition.spi.schedule.Schedule;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, SnowflakeId> {

  List<Schedule> findByJobCode(JobCode jobCode);

  List<Schedule> findByEnabled(boolean enabled);
}
