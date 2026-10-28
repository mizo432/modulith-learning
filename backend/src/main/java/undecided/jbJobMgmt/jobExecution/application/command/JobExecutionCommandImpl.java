package undecided.jbJobMgmt.jobExecution.application.command;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jbJobMgmt.jobExecution.spi.jobexecution.JobExecution;
import undecided.jbJobMgmt.jobExecution.spi.jobexecution.repository.JobExecutionRepository;
import undecided.supporting.snowflake.SnowflakeId;

@RequiredArgsConstructor
@Service
public class JobExecutionCommandImpl implements JobExecutionCommand {

  private final JobExecutionRepository repository;

  @Override
  public void startJob(JobExecution jobExecution) {
    jobExecution.start();
    repository.save(jobExecution);
  }

  @Override
  public void cancel(String executionId) {
    JobExecution execution = repository.findById(SnowflakeId.of(Long.parseLong(executionId)))
        .orElseThrow(() -> new EntityNotFoundException("JobExecution not found: " + executionId));
    execution.cancel();
    repository.save(execution);
  }

  @Override
  public void reExecute(String executionId) {
    JobExecution execution = repository.findById(SnowflakeId.of(Long.parseLong(executionId)))
        .orElseThrow(() -> new EntityNotFoundException("JobExecution not found: " + executionId));
    execution.reExecute();
    repository.save(execution);
  }

  @Override
  public void resume(String executionId) {
    JobExecution execution = repository.findById(SnowflakeId.of(Long.parseLong(executionId)))
        .orElseThrow(() -> new EntityNotFoundException("JobExecution not found: " + executionId));
    execution.resume();
    repository.save(execution);
  }
}
