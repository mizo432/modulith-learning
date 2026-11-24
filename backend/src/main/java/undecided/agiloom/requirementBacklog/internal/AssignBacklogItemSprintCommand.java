package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムをスプリントに割り当てるコマンド（Issue #525）。 */
@Getter
@Builder
class AssignBacklogItemSprintCommand {
  @NonNull private final SnowflakeId backlogItemId;
  @NonNull private final SnowflakeId sprintId;
  @NonNull private final SnowflakeId actorEmployeeId;
}
