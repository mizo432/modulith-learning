package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.QualityCategory;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BacklogItemResponse {
  private SnowflakeId id;
  private String title;
  private String description;
  private BacklogItemStatus status;
  private String statusLabel;
  private Integer priority;
  private UUID creatorEmployeeId;
  private String creatorName;
  private String sprintId;
  private String scrumTeamId;
  private Integer value;
  private QualityCategory qualityCategory;
  private String qualityCategoryLabel;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
