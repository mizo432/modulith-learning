package undecided.jobmanagement.jobdefinition.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;

/** ジョブ定義の参照系サービス（UC-JB-004）。 */
@Service
@RequiredArgsConstructor
public class JobDefinitionQueryService {

  private final JobDefinitionRepository jobDefinitionRepository;

  /**
   * 登録済みのジョブ定義をすべて返します。
   *
   * @return ジョブ定義の一覧
   */
  public List<JobDefinitionDTO> listDefinitions() {
    return jobDefinitionRepository.findAll().stream().map(this::toDto).toList();
  }

  private JobDefinitionDTO toDto(JobDefinition definition) {
    JobDefinitionDTO dto = new JobDefinitionDTO();
    dto.setId(String.valueOf(definition.getId().value()));
    dto.setJobCode(definition.getJobCode().getValue());
    dto.setVersion(definition.getVersion() != null ? definition.getVersion().getValue() : null);
    dto.setParameterSpec(
        definition.getParameterSpec() != null ? definition.getParameterSpec().getValue() : null);
    if (definition.getExecutionConstraint() != null) {
      dto.setMaxConcurrentExecutions(definition.getExecutionConstraint().getMaxConcurrentExecutions());
      dto.setTimeoutSeconds(definition.getExecutionConstraint().getTimeoutSeconds());
    }
    dto.setIsActive(definition.getIsActive());
    dto.setCreatedAt(definition.getCreatedAt() != null ? definition.getCreatedAt().toString() : null);
    dto.setUpdatedAt(definition.getUpdatedAt() != null ? definition.getUpdatedAt().toString() : null);
    dto.setCreatedBy(definition.getCreatedBy());
    dto.setUpdatedBy(definition.getUpdatedBy());
    return dto;
  }
}
