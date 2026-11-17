package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class UpdateSprintCommandService {

    private final SprintRepository sprintRepository;

    public UpdateSprintCommandService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
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
            case CANCELLED -> sprint.cancel();
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
    }
}
