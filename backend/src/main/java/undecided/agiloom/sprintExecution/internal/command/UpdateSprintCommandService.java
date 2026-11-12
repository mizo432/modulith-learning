package undecided.agiloom.sprintExecution.internal.command;

import com.github.mizo432.shared.common.exception.BusinessException;
import com.github.mizo432.shared.common.message.ResultMessage;
import com.github.mizo432.shared.common.message.ResultMessages;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.presentation.SprintResponseAssembler;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class UpdateSprintCommandService implements UpdateSprintCommandServiceInterface {

  private final SprintRepository sprintRepository;
  private final SprintResponseAssembler sprintResponseAssembler;

  public UpdateSprintCommandService(
      SprintRepository sprintRepository, SprintResponseAssembler sprintResponseAssembler) {
    this.sprintRepository = sprintRepository;
    this.sprintResponseAssembler = sprintResponseAssembler;
  }

  public SprintDTO execute(@NonNull UpdateSprintCommand command) {
    Sprint sprint =
        sprintRepository
            .findByIdAndDeletedAtIsNull(command.getId())
            .orElseThrow(() -> new BusinessException(
                ResultMessages.error().add(ResultMessage.fromText("Sprint not found: " + command.getId()))));

    // nullが渡された場合は既存の値を使用し、ドメイン層の検証を通過させる
    String title = command.getTitle() != null ? command.getTitle() : sprint.getTitle();
    String goal = command.getGoal() != null ? command.getGoal() : sprint.getGoal();

    sprint.update(title, goal, command.getEndDate(), command.getNotes());

    return sprintResponseAssembler.toDTO(sprint);
  }

  public SprintDTO changeStatus(@NonNull SnowflakeId id, @NonNull SprintStatus status) {
    Sprint sprint =
        sprintRepository
            .findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new BusinessException(
                ResultMessages.error().add(ResultMessage.fromText("Sprint not found: " + id))));

    switch (status) {
      case ACTIVE -> sprint.start();
      case COMPLETED -> sprint.complete();
      case CANCELLED -> sprint.cancel();
      default -> throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("Invalid status: " + status)));
    }

    return sprintResponseAssembler.toDTO(sprint);
  }
}
