package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FeatureResponse {
  private SnowflakeId id;
  private String name;
  private String description;
  private FeatureStatus status;
  private String statusLabel;
  private String scrumTeamId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<SnowflakeId> backlogItemIds;
  private Integer completedStoryPoints;
  private Integer totalStoryPoints;
  private Double progressPercentage;
}
