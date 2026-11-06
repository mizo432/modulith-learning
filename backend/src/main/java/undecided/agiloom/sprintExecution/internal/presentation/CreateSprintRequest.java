package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Min(1)
    private Integer sprintNumber;

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String goal;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private UUID teamId;

    @Size(max = 100)
    private String teamName;

    // endDateがstartDateより後であることを検証するメソッド
    // @AssertTrueはyavi環境で利用できないため、コマンド層で検証する
    public boolean isEndDateAfterStartDate() {
        return endDate == null || startDate == null || endDate.isAfter(startDate);
    }
}
