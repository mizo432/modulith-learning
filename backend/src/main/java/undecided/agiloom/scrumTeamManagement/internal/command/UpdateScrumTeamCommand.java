package undecided.agiloom.scrumTeamManagement.internal.command;

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

  private String teamName;

  private String businessDomain;

  private String description;
}
