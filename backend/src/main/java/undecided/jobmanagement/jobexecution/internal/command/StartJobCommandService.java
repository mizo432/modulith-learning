package undecided.jobmanagement.jobexecution.internal.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionId;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommand;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class StartJobCommandService implements StartJobCommand {

  private final JobExecutionRepository jobExecutionRepository;

  @Override
  public void startJob(JobExecutionDTO jobExecutionDTO) {
    JobExecution jobExecution = new JobExecution();
    jobExecution.setId(SnowflakeId.nextId());
    jobExecution.setExecutionId(ExecutionId.of(jobExecutionDTO.getExecutionId()));
    jobExecution.setJobCode(jobExecutionDTO.getJobCode());
    jobExecution.setState(ExecutionState.PENDING);
    jobExecution.setStartedAt(LocalDateTime.now());
    jobExecution.setCreatedAt(LocalDateTime.now());
    jobExecution.setCreatedBy(jobExecutionDTO.getCreatedBy());
    
    jobExecutionRepository.save(jobExecution);
  }
}
