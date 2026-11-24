package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AssignBacklogItemSprintRequest {
  @NotNull private String sprintId;
  @NotNull private SnowflakeId actorEmployeeId;
}
