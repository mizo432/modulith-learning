package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ChangeBacklogItemStatusRequest {
  @NotNull private BacklogItemStatus status;
  @NotNull private UUID actorEmployeeId;
}
