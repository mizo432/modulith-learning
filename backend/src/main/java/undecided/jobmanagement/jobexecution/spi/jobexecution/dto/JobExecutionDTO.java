package undecided.jobmanagement.jobexecution.spi.jobexecution.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobExecutionDTO {

  private String id;
  private String executionId;
  private String jobCode;
  private String state;
  private String startedAt;
  private String completedAt;
  private String errorMessage;
  private Long processedCount;
  private String checkpointPosition;
  private String createdBy;
}
