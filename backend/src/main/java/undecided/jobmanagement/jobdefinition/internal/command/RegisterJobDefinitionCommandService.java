package undecided.jobmanagement.jobdefinition.internal.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.ExecutionConstraint;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobCode;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobVersion;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.ParameterSpec;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.command.RegisterJobDefinitionCommand;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RegisterJobDefinitionCommandService implements RegisterJobDefinitionCommand {

  private final JobDefinitionRepository jobDefinitionRepository;

  @Override
  public void register(JobDefinitionDTO jobDefinitionDTO) {
    JobDefinition jobDefinition = new JobDefinition();
    jobDefinition.setId(SnowflakeId.nextId());
    jobDefinition.setJobCode(JobCode.of(jobDefinitionDTO.getJobCode()));
    jobDefinition.setVersion(JobVersion.of(jobDefinitionDTO.getVersion()));
    jobDefinition.setParameterSpec(ParameterSpec.of(jobDefinitionDTO.getParameterSpec()));
    jobDefinition.setExecutionConstraint(
        ExecutionConstraint.of(
            jobDefinitionDTO.getMaxConcurrentExecutions(),
            jobDefinitionDTO.getTimeoutSeconds()));
    jobDefinition.setIsActive(true);
    jobDefinition.setCreatedAt(LocalDateTime.now());
    jobDefinition.setCreatedBy(jobDefinitionDTO.getCreatedBy());
    
    jobDefinitionRepository.save(jobDefinition);
  }
}
