package undecided.agiloom.requirementBacklog.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムのコメントを編集するコマンド。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateBacklogItemCommentCommand {
  @NonNull private SnowflakeId commentId;
  @NonNull private String body;
  private SnowflakeId actorEmployeeId;
}
