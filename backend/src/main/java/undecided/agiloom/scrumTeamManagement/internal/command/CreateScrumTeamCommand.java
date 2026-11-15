package undecided.agiloom.scrumTeamManagement.internal.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScrumTeamCommand {

  @NonNull @NotNull private UUID productId;

  @NonNull @NotBlank private String teamName;

  @NonNull @NotBlank private String businessDomain;

  private String description;

  private UUID poEmployeeId;
}
