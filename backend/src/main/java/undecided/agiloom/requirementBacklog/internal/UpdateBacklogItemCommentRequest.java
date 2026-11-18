package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/** コメント編集のリクエストDTO。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateBacklogItemCommentRequest {
  private SnowflakeId actorEmployeeId;
  @NotBlank private String body;
}
