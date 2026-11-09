package undecided.agiloom.sprintExecution.internal.command;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

@Getter
@Builder
public class UpdateSprintCommand {
    @NonNull
    private final SnowflakeId id;
    @Nullable
    private final String title;
    @Nullable
    private final String goal;
    @Nullable
    private final LocalDate endDate;
    @Nullable
    private final String notes;
}
