package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャー更新コマンド。
 */
@Builder
record UpdateFeatureCommand(
    @NonNull SnowflakeId featureId,
    @NonNull SnowflakeId scrumTeamId,
    String name,
    String description,
    FeatureStatus status,
    @NonNull SnowflakeId actorEmployeeId) {
}
