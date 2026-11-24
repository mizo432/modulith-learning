package undecided.agiloom.sprintExecution.internal.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** スプリント容量のレスポンス。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintCapacityResponse {
  private String sprintId;
  private Integer businessDays;
  private Double velocityPerBusinessDay;
  private Long assignablePoints;
  private Long assignedPoints;
  private Boolean overAllocated;
}
