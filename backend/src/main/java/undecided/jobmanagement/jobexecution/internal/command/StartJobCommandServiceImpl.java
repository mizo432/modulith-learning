package undecided.jobmanagement.jobexecution.internal.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionId;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommandService;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class StartJobCommandServiceImpl implements StartJobCommandService {

  private final JobExecutionRepository jobExecutionRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  @Override
  public SnowflakeId startJob(JobExecutionDTO jobExecutionDTO) {
    SnowflakeId id = SnowflakeId.of(snowflakeIdGenerator.nextId());
    LocalDateTime now = LocalDateTime.now();
    JobExecution jobExecution = JobExecution.create(
        id,
        ExecutionId.of(jobExecutionDTO.getExecutionId()),
        jobExecutionDTO.getJobCode(),
        ExecutionState.PENDING,
        now,
        jobExecutionDTO.getCreatedBy());

    jobExecutionRepository.save(jobExecution);
    return id;
  }
}
