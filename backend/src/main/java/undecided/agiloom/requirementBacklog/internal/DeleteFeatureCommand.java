package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャー削除コマンド。
 */
@Builder
record DeleteFeatureCommand(
    @NonNull SnowflakeId featureId,
    @NonNull SnowflakeId scrumTeamId,
    @NonNull SnowflakeId actorEmployeeId) {
}
