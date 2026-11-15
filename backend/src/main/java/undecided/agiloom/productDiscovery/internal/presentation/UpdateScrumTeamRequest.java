package undecided.agiloom.productDiscovery.internal.presentation;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScrumTeamRequest {
  private String name;
  private String businessDomain;
  private UUID poEmployeeId;
  private String poName;
  private UUID smEmployeeId;
  private String smName;
}
