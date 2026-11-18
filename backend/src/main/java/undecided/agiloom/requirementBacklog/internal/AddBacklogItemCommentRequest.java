package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotBlank;
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
class AddBacklogItemCommentRequest {
  private SnowflakeId authorEmployeeId;
  @NotBlank private String authorName;
  @NotBlank private String body;
  /** 親コメントのID（返信の場合）。ルートコメントの場合はnull。 */
  @Nullable private SnowflakeId parentCommentId;
}
