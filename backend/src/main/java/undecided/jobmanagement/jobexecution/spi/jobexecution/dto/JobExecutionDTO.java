package undecided.jobmanagement.jobexecution.spi.jobexecution.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobExecutionDTO {

  private String id;

  @NotBlank(message = "executionIdは必須です")
  @Size(max = 100, message = "executionIdは100文字以内で指定してください")
  private String executionId;

  @NotBlank(message = "jobCodeは必須です")
  @Size(max = 100, message = "jobCodeは100文字以内で指定してください")
  @Pattern(regexp = "^[a-zA-Z0-9_\\-\\.]+$", message = "jobCodeは英数字、アンダースコア、ハイフン、ドットのみ使用できます")
  private String jobCode;

  @Size(max = 50, message = "stateは50文字以内で指定してください")
  private String state;

  private String startedAt;
  private String completedAt;

  @Size(max = 5000, message = "errorMessageは5000文字以内で指定してください")
  private String errorMessage;

  private Long processedCount;

  @Size(max = 500, message = "checkpointPositionは500文字以内で指定してください")
  private String checkpointPosition;

  @Size(max = 100, message = "createdByは100文字以内で指定してください")
  private String createdBy;
}
