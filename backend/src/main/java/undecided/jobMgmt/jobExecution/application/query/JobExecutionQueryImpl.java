package undecided.jobMgmt.jobExecution.application.query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobMgmt.jobDefinition.spi.jobdefinition.JobCode;
import undecided.jobMgmt.jobExecution.spi.jobexecution.JobExecution;
import undecided.jobMgmt.jobExecution.spi.jobexecution.repository.JobExecutionRepository;
import undecided.supporting.snowflake.SnowflakeId;

@RequiredArgsConstructor
@Service
public class JobExecutionQueryImpl implements JobExecutionQuery {

  private final JobExecutionRepository repository;

  @Override
  public Optional<JobExecution> findById(String executionId) {
    return repository.findById(SnowflakeId.of(Long.parseLong(executionId)));
  }

  @Override
  public List<JobExecution> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public List<JobExecution> findByJobCode(String jobCode) {
    return repository.findByJobCode(new JobCode(jobCode));
  }
}
