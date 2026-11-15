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
public class CreateScrumTeamRequest {
  @NotBlank private String name;
  @NotBlank private String businessDomain;
  private UUID poEmployeeId;
  private String poName;
}
