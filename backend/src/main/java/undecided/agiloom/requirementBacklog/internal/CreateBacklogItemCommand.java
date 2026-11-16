package undecided.agiloom.requirementBacklog.internal;

import java.util.UUID;
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
class CreateBacklogItemCommand {
  @NonNull private SnowflakeId scrumTeamId;
  @NonNull private String title;
  private String description;
  private Integer priority;
  private UUID creatorEmployeeId;
  @NonNull private String creatorName;
  private SnowflakeId sprintId;
}
