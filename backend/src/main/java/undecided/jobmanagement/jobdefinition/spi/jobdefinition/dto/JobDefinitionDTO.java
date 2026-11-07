package undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDefinitionDTO {

  private String id;

  @NotBlank(message = "jobCodeは必須です")
  @Size(max = 100, message = "jobCodeは100文字以内で指定してください")
  @Pattern(regexp = "^[a-zA-Z0-9_\\-\\.]+$", message = "jobCodeは英数字、アンダースコア、ハイフン、ドットのみ使用できます")
  private String jobCode;

  @Size(max = 50, message = "versionは50文字以内で指定してください")
  private String version;

  @Size(max = 5000, message = "parameterSpecは5000文字以内で指定してください")
  private String parameterSpec;

  @NotNull(message = "maxConcurrentExecutionsは必須です")
  @Min(value = 1, message = "maxConcurrentExecutionsは1以上である必要があります")
  private Integer maxConcurrentExecutions;

  @NotNull(message = "timeoutSecondsは必須です")
  @Min(value = 1, message = "timeoutSecondsは1以上である必要があります")
  private Integer timeoutSeconds;

  private Boolean isActive;
  private String createdAt;
  private String updatedAt;

  @Size(max = 100, message = "createdByは100文字以内で指定してください")
  private String createdBy;

  @Size(max = 100, message = "updatedByは100文字以内で指定してください")
  private String updatedBy;
}
