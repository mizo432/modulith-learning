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
class CreateFeatureRequest {
  @NotBlank private String name;
  private String description;
  @NotBlank private String scrumTeamId;
  @NotBlank private String actorEmployeeId;
}
