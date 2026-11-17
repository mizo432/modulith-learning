package undecided.agiloom.productDiscovery.internal.presentation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScrumTeamRequest {
  @NotBlank private String name;
  @NotBlank private String businessDomain;
  private SnowflakeId poEmployeeId;
  private String poName;
  private SnowflakeId smEmployeeId;
  private String smName;
}
