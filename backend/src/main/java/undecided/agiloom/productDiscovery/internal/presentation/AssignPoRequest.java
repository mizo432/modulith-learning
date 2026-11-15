package undecided.agiloom.productDiscovery.internal.presentation;

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
public class AssignPoRequest {
  @NotBlank private UUID poEmployeeId;
  @NotBlank private String poName;
}
