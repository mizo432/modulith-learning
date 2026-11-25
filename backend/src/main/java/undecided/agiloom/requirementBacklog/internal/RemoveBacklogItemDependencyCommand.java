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
class RemoveBacklogItemDependencyCommand {
  @NonNull private SnowflakeId backlogItemId;
  @NonNull private SnowflakeId dependencyItemId;
  @NonNull private SnowflakeId actorEmployeeId;
}
