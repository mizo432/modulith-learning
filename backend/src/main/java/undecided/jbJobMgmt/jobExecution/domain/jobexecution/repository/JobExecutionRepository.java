package undecided.jbJobMgmt.jobExecution.domain.jobexecution.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jbJobMgmt.jobDefinition.domain.jobdefinition.JobCode;
import undecided.jbJobMgmt.jobExecution.domain.jobexecution.ExecutionState;
import undecided.jbJobMgmt.jobExecution.domain.jobexecution.JobExecution;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface JobExecutionRepository extends CrudRepository<JobExecution, SnowflakeId> {

  List<JobExecution> findByJobCode(JobCode jobCode);

  List<JobExecution> findByExecutionState(ExecutionState state);

  List<JobExecution> findByJobCodeAndExecutionState(JobCode jobCode, ExecutionState state);
}
