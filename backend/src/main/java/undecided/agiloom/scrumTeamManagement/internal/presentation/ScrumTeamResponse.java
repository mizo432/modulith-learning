package undecided.agiloom.scrumTeamManagement.internal.presentation;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeamStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrumTeamResponse {
  private SnowflakeId id;
  private UUID productId;
  private String teamName;
  private String businessDomain;
  private String description;
  private UUID poEmployeeId;
  private ScrumTeamStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
