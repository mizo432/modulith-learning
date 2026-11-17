package undecided.agiloom.requirementBacklog.internal;

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
class UpdateBacklogItemCommand {
  @NonNull private SnowflakeId backlogItemId;
  private String title;
  private String description;
  private Integer priority;
  private SnowflakeId sprintId;
  private Integer value;
}
