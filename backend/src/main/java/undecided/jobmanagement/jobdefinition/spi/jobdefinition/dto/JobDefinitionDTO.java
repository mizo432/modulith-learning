package undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDefinitionDTO {

  private String id;
  private String jobCode;
  private String version;
  private String parameterSpec;
  private Integer maxConcurrentExecutions;
  private Integer timeoutSeconds;
  private Boolean isActive;
  private String createdAt;
  private String updatedAt;
  private String createdBy;
  private String updatedBy;
}
