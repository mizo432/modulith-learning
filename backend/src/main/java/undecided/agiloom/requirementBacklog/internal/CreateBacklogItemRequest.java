package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CreateBacklogItemRequest {
  @NotBlank private String title;
  private String description;
  private Integer priority;
  private UUID creatorEmployeeId;
  @NotBlank private String creatorName;
  private String sprintId;
}
