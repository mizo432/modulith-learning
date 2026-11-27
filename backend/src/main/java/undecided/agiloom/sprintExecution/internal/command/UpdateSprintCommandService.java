package undecided.agiloom.sprintExecution.internal.command;
import java.time.Clock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintCancelled;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class UpdateSprintCommandService {

    private final SprintRepository sprintRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public UpdateSprintCommandService(
            SprintRepository sprintRepository, ApplicationEventPublisher eventPublisher, Clock clock) {
        this.sprintRepository = sprintRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    public void execute(@NonNull UpdateSprintCommand command) {
        Sprint sprint = sprintRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + command.getId()));

        sprint.update(
                command.getTitle() != null ? command.getTitle() : sprint.getTitle(),
                command.getGoal() != null ? command.getGoal() : sprint.getGoal(),
                command.getEndDate(),
                command.getNotes()
        );
    }

    public void changeStatus(@NonNull SnowflakeId id, @NonNull SprintStatus status) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + id));

        switch (status) {
            case ACTIVE -> sprint.start();
            case COMPLETED -> sprint.complete();
            case CANCELLED -> {
                sprint.cancel();
                publishSprintCancelled(sprint);
            }
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    /**
     * スプリントを論理削除します。
     * スプリント0は削除できないため、例外をスローします。
     *
     * @param id 削除するスプリントのID
     */
    public void delete(@NonNull SnowflakeId id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + id));

        // スプリント0の削除は禁止
        sprint.delete();
        sprintRepository.save(sprint);
        publishSprintCancelled(sprint);
    }

    private void publishSprintCancelled(Sprint sprint) {
        eventPublisher.publishEvent(
                new SprintCancelled(
                        sprint.getId(), sprint.getTeamId(), sprint.getSprintNumber(), LocalDateTime.now(clock)));
    }
}
