package undecided.agiloom.productDiscovery.internal.command;

import java.util.UUID;
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
public class UpdateScrumTeamCommand {
  @NonNull private SnowflakeId id;
  private String name;
  private String businessDomain;
  private UUID poEmployeeId;
  private String poName;
  private UUID smEmployeeId;
  private String smName;
}
