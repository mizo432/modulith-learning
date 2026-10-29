package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSprintRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private Integer sprintNumber;

    @NotBlank
    private String title;

    private String goal;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private UUID teamId;

    private String teamName;
}
