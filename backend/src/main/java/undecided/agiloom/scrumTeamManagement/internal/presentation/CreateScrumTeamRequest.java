package undecided.agiloom.scrumTeamManagement.internal.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotNull private UUID productId;

  @NotBlank private String teamName;

  @NotBlank private String businessDomain;

  private String description;

  private UUID poEmployeeId;
}
