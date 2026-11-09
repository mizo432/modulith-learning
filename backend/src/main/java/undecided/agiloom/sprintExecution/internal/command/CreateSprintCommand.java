package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

@Getter
@Builder
public class CreateSprintCommand {
    @NonNull
    private final UUID productId;

    @NonNull
    private final Integer sprintNumber;

    @NonNull
    private final String title;

    private final String goal;

    @NonNull
    private final LocalDate startDate;

    @NonNull
    private final LocalDate endDate;

    @NonNull
    private final UUID teamId;

    private final String teamName;
}
