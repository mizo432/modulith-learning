package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャー作成コマンド。
 */
@Builder
record CreateFeatureCommand(
    @NonNull SnowflakeId scrumTeamId,
    @NonNull String name,
    String description,
    @NonNull SnowflakeId actorEmployeeId) {
}
