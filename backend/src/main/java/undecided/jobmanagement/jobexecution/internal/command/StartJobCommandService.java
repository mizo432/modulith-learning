package undecided.jobmanagement.jobexecution.internal.command;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobexecution.internal.BatchJobResolver;
import undecided.jobmanagement.jobexecution.internal.JobExecutionRunner;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommand;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.jobmanagement.permissionmanagement.internal.AdminEmployeeSubsetRepository;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@RequiredArgsConstructor
@Service
public class StartJobCommandService implements StartJobCommand {

  private final JobExecutionRepository jobExecutionRepository;
  private final JobDefinitionRepository jobDefinitionRepository;
  private final AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;
  private final BatchJobResolver batchJobResolver;
  private final JobExecutionRunner jobExecutionRunner;
  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void startJob(JobExecutionDTO jobExecutionDTO) {
    throw new UnsupportedOperationException("not implemented");
  }
}
