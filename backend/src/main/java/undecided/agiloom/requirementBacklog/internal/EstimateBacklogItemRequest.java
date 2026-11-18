package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムの見積もり値設定リクエスト。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class EstimateBacklogItemRequest {
  @NotNull private Integer storyPoint;
  @NotNull private SnowflakeId actorEmployeeId;
}
