package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSprintCommand {
    @NonNull
    private UUID productId;

    @NonNull
    private Integer sprintNumber;

    @NonNull
    private String title;

    private String goal;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate endDate;

    @NonNull
    private UUID teamId;

    private String teamName;
}
