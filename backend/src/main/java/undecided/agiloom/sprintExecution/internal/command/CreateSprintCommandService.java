package undecided.agiloom.sprintExecution.internal.command;

import com.github.mizo432.shared.common.exception.BusinessException;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.presentation.SprintResponseAssembler;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Service
@Transactional
public class CreateSprintCommandService implements CreateSprintCommandServiceInterface {

  private final SprintRepository sprintRepository;
  private final SprintResponseAssembler sprintResponseAssembler;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  public CreateSprintCommandService(
      SprintRepository sprintRepository,
      SprintResponseAssembler sprintResponseAssembler,
      SnowflakeIdGenerator snowflakeIdGenerator) {
    this.sprintRepository = sprintRepository;
    this.sprintResponseAssembler = sprintResponseAssembler;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  public SprintDTO execute(@NonNull CreateSprintCommand command) {
    if (!command.getEndDate().isAfter(command.getStartDate())) {
      throw new BusinessException("endDate must be after startDate");
    }
    Sprint sprint =
        Sprint.of(
            SnowflakeId.of(snowflakeIdGenerator.nextId()),
            command.getProductId(),
            command.getSprintNumber(),
            command.getTitle(),
            command.getGoal(),
            command.getStartDate(),
            command.getEndDate(),
            command.getTeamId(),
            command.getTeamName(),
            null,
            null,
            null,
            null);

    Sprint saved = sprintRepository.save(sprint);
    return sprintResponseAssembler.toDTO(saved);
  }
}
