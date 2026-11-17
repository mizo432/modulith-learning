package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSprintCommand {
    private SnowflakeId id;
    private String title;
    private String goal;
    private LocalDate endDate;
    private String notes;
}
