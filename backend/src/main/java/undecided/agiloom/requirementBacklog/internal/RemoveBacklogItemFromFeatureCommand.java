package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムをフィーチャーから外すコマンド。
 */
@Builder
record RemoveBacklogItemFromFeatureCommand(
    @NonNull SnowflakeId featureId,
    @NonNull SnowflakeId backlogItemId,
    @NonNull SnowflakeId scrumTeamId,
    @NonNull SnowflakeId actorEmployeeId) {
}
