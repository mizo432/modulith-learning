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
class AddBacklogItemCommentRequest {
  private UUID authorEmployeeId;
  @NotBlank private String authorName;
  @NotBlank private String body;
}
