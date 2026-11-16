package undecided.agiloom.requirementBacklog.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateBacklogItemRequest {
  private String title;
  private String description;
  private Integer priority;
  private String sprintId;
}
