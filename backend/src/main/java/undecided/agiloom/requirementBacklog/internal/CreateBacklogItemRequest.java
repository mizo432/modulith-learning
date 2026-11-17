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
class CreateBacklogItemRequest {
  @NotBlank private String title;
  private String description;
  private Integer priority;
  private SnowflakeId creatorEmployeeId;
  @NotBlank private String creatorName;
  private String sprintId;
}
