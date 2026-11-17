package undecided.agiloom.productDiscovery.internal.presentation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrumTeamResponse {
  private SnowflakeId id;
  private String name;
  private String businessDomain;
  private SnowflakeId poEmployeeId;
  private String poName;
  private SnowflakeId smEmployeeId;
  private String smName;
  private String productId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
