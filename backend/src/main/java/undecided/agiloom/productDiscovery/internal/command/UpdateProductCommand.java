package undecided.agiloom.productDiscovery.internal.command;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCommand {
  private SnowflakeId id;
  private String name;
  private String description;
  private UUID managerId;
}
