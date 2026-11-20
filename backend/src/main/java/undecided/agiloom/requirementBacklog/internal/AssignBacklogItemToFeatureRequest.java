package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AssignBacklogItemToFeatureRequest {
  @NotNull private String actorEmployeeId;
  @NotNull private String scrumTeamId;
}
