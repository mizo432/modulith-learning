package undecided.agiloom.requirementBacklog.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ChangeBacklogItemStatusCommand {
  @NonNull private SnowflakeId backlogItemId;
  @NonNull private BacklogItemStatus targetStatus;
  @NonNull private SnowflakeId actorEmployeeId;
}
