package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateFeatureRequest {
  private String name;
  private String description;
  private FeatureStatus status;
  @NotNull private String scrumTeamId;
  @NotNull private String actorEmployeeId;
}
