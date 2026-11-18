package undecided.agiloom.requirementBacklog.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムのコメントを削除するコマンド。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DeleteBacklogItemCommentCommand {
  @NonNull private SnowflakeId backlogItemId;
  @NonNull private SnowflakeId commentId;
  private SnowflakeId actorEmployeeId;
}
