package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムのスプリント割り当てを解除するコマンド（Issue #525）。 */
@Getter
@Builder
class UnassignBacklogItemSprintCommand {
  @NonNull private final SnowflakeId backlogItemId;
  @NonNull private final SnowflakeId actorEmployeeId;
}
