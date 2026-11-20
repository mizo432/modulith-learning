package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムをフィーチャーに割り当てるコマンド。
 */
@Builder
record AssignBacklogItemToFeatureCommand(
    @NonNull SnowflakeId featureId,
    @NonNull SnowflakeId backlogItemId,
    @NonNull SnowflakeId scrumTeamId,
    @NonNull SnowflakeId actorEmployeeId) {
}
