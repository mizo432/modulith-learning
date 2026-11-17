package undecided.agiloom.requirementBacklog.internal;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AddBacklogItemCommentCommand {
  @NonNull private SnowflakeId backlogItemId;
  private UUID authorEmployeeId;
  @NonNull private String authorName;
  @NonNull private String body;
}
