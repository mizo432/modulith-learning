package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class CreateSprintCommandService {

    private final SprintRepository sprintRepository;

    public CreateSprintCommandService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    public SnowflakeId execute(@NonNull CreateSprintCommand command) {
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
        return saved.getId();
    }
}
