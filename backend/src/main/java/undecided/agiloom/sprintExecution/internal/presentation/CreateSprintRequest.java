package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class CreateSprintRequest {
    @NotNull
    private final UUID productId;

    @NotNull
    @Min(1)
    private final Integer sprintNumber;

    @NotBlank
    @Size(max = 100)
    private final String title;

    @Size(max = 1000)
    private final String goal;

    @NotNull
    private final LocalDate startDate;

    @NotNull
    private final LocalDate endDate;

    @NotNull
    private final UUID teamId;

    @Size(max = 100)
    private final String teamName;
}
