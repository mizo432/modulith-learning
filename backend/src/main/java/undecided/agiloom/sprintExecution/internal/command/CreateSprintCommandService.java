package undecided.agiloom.sprintExecution.internal.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.SprintSettingsRepository;
import undecided.agiloom.sprintExecution.internal.domain.SprintSettingsCalculator;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class CreateSprintCommandService {

    private final SprintRepository sprintRepository;
    private final SprintSettingsRepository sprintSettingsRepository;
    private final SprintSettingsCalculator sprintSettingsCalculator;
    private final Clock clock;

    public CreateSprintCommandService(SprintRepository sprintRepository,
                                      SprintSettingsRepository sprintSettingsRepository,
                                      SprintSettingsCalculator sprintSettingsCalculator,
                                      Clock clock) {
        this.sprintRepository = sprintRepository;
        this.sprintSettingsRepository = sprintSettingsRepository;
        this.sprintSettingsCalculator = sprintSettingsCalculator;
        this.clock = clock;
    }

    public SnowflakeId execute(@NonNull CreateSprintCommand command) {
        LocalDate endDate = command.getEndDate();
        LocalDateTime now = LocalDateTime.now(clock);
        
        // endDateが指定されていない場合、スプリント設定に基づいて自動計算する
        if (endDate == null) {
            SnowflakeId productId = command.getProductId();
            SprintSettings settings = sprintSettingsRepository.findByProductId(productId)
                .orElseGet(() -> SprintSettings.builder().productId(productId).createdAt(now).updatedAt(now).build());
            endDate = sprintSettingsCalculator.calculateEndDate(settings, command.getStartDate());
        }

        Sprint sprint = Sprint.builder()
                .productId(command.getProductId())
                .sprintNumber(command.getSprintNumber())
                .title(command.getTitle())
                .goal(command.getGoal())
                .startDate(command.getStartDate())
                .endDate(endDate)
                .teamId(command.getTeamId())
                .teamName(command.getTeamName())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Sprint saved = sprintRepository.save(sprint);
        return saved.getId();
    }
}
