package undecided.agiloom.scrumTeamManagement.internal.presentation;

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

  private String teamName;

  private String businessDomain;

  private String description;
}
