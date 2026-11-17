package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSprintCommand {
    @NonNull
    private SnowflakeId productId;

    @NonNull
    private Integer sprintNumber;

    @NonNull
    private String title;

    private String goal;

    @NonNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NonNull
    private SnowflakeId teamId;

    private String teamName;
}
