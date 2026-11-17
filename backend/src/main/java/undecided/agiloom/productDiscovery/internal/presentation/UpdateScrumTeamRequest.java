package undecided.agiloom.productDiscovery.internal.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScrumTeamRequest {
  private String name;
  private String businessDomain;
  private SnowflakeId poEmployeeId;
  private String poName;
  private SnowflakeId smEmployeeId;
  private String smName;
}
