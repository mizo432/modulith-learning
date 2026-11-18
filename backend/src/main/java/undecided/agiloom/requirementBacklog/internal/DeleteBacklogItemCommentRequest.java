package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/** コメント削除リクエスト。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DeleteBacklogItemCommentRequest {
  @NotNull private SnowflakeId actorEmployeeId;
}
