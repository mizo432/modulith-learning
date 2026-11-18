package undecided.agiloom.requirementBacklog.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムの見積もり値設定コマンド。
 *
 * <p>見積もり値（StoryPoint）の設定と、ESTIMATED 状態での再評価に使用します。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class EstimateBacklogItemCommand {
  @NonNull private SnowflakeId backlogItemId;
  @Nullable private Integer storyPoint;
  @NonNull private SnowflakeId actorEmployeeId;
}
