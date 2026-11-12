package undecided.jobmanagement.jobdefinition.internal.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.ExecutionConstraint;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobCode;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobVersion;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.ParameterSpec;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.command.RegisterJobDefinitionCommandService;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RegisterJobDefinitionCommandServiceImpl implements RegisterJobDefinitionCommandService {

  private final JobDefinitionRepository jobDefinitionRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  @Override
  public SnowflakeId register(JobDefinitionDTO jobDefinitionDTO) {
    SnowflakeId id = SnowflakeId.of(snowflakeIdGenerator.nextId());
    JobDefinition jobDefinition = JobDefinition.create(
        id,
        JobCode.of(jobDefinitionDTO.getJobCode()),
        JobVersion.of(jobDefinitionDTO.getVersion()),
        jobDefinitionDTO.getParameterSpec() != null
            ? ParameterSpec.of(jobDefinitionDTO.getParameterSpec())
            : null,
        ExecutionConstraint.create(
            jobDefinitionDTO.getMaxConcurrentExecutions(),
            jobDefinitionDTO.getTimeoutSeconds()),
        LocalDateTime.now(),
        jobDefinitionDTO.getCreatedBy());

    jobDefinitionRepository.save(jobDefinition);
    return id;
  }
}
