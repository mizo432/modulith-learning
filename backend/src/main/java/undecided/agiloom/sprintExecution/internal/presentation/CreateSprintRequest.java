package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSprintRequest {
    @NotNull
    private String productId;

    @NotNull
    private Integer sprintNumber;

    @NotBlank
    private String title;

    private String goal;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private String teamId;

    private String teamName;
}
