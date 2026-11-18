package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BacklogItemCommentResponse {
  private SnowflakeId id;
  private String backlogItemId;
  private SnowflakeId authorEmployeeId;
  private String authorName;
  private String body;
  private LocalDateTime createdAt;
  /** 親コメントのID（返信の場合）。ルートコメントの場合はnull。 */
  @Nullable private SnowflakeId parentId;
  /** 子返信があるかどうか。 */
  private boolean hasReplies;
}
