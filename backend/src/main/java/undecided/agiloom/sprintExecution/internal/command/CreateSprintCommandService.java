package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.Sprint;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.presentation.SprintResponseAssembler;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.exception.BusinessException;

@Service
@Transactional
public class CreateSprintCommandService implements CreateSprintCommandServiceInterface {

    private final SprintRepository sprintRepository;
    private final SprintResponseAssembler sprintResponseAssembler;

    public CreateSprintCommandService(SprintRepository sprintRepository,
                                      SprintResponseAssembler sprintResponseAssembler) {
        this.sprintRepository = sprintRepository;
        this.sprintResponseAssembler = sprintResponseAssembler;
    }

    public SprintDTO execute(@NonNull CreateSprintCommand command) {
        Objects.requireNonNull(command, "commandはnullであってはいけません");
        if (!command.getEndDate().isAfter(command.getStartDate())) {
            throw new BusinessException("endDate must be after startDate");
        }
        Sprint sprint = Sprint.builder()
                .productId(command.getProductId())
                .sprintNumber(command.getSprintNumber())
                .title(command.getTitle())
                .goal(command.getGoal())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .teamId(command.getTeamId())
                .teamName(command.getTeamName())
                .build();

        Sprint saved = sprintRepository.save(sprint);
        return sprintResponseAssembler.toDTO(saved);
    }
}
