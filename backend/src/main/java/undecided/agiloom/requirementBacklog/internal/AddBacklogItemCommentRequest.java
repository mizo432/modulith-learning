package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AddBacklogItemCommentRequest {
  private SnowflakeId authorEmployeeId;
  @NotBlank private String authorName;
  @NotBlank private String body;
}
