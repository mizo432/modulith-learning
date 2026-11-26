package undecided.jobmanagement.jobexecution.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.supporting.snowflake.SnowflakeId;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, SnowflakeId> {
  
  Optional<JobExecution> findByExecutionIdValue(String executionIdValue);
  
  List<JobExecution> findByJobCode(String jobCode);
  
  boolean existsByExecutionIdValue(String executionIdValue);

  long countByJobCodeAndStateIn(String jobCode, java.util.Collection<undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState> states);

  List<JobExecution> findAllByOrderByCreatedAtDesc();
}
