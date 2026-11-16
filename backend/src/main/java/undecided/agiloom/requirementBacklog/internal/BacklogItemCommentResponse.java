package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BacklogItemCommentResponse {
  private SnowflakeId id;
  private String backlogItemId;
  private UUID authorEmployeeId;
  private String authorName;
  private String body;
  private LocalDateTime createdAt;
}
