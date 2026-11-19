package undecided.agiloom.requirementBacklog.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AddBacklogItemCommentCommand {
  @NonNull private SnowflakeId backlogItemId;
  private SnowflakeId authorEmployeeId;
  @NonNull private String authorName;
  private String authorEmail;
  @NonNull private String body;
  /** 親コメントのID（返信の場合）。ルートコメントの場合はnull。 */
  @Nullable private SnowflakeId parentCommentId;
}
