package undecided.agiloom.productDiscovery.internal.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScrumTeamCommand {
  @NonNull private SnowflakeId productId;
  @NonNull private String name;
  @NonNull private String businessDomain;
  private SnowflakeId poEmployeeId;
  private String poName;
  private SnowflakeId smEmployeeId;
  private String smName;
}
