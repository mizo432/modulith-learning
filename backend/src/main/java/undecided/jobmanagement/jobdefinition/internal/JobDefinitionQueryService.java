package undecided.jobmanagement.jobdefinition.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;

/** ジョブ定義の参照系サービス（UC-JB-004）。 */
@Service
@RequiredArgsConstructor
public class JobDefinitionQueryService {

  private final JobDefinitionRepository jobDefinitionRepository;

  public List<JobDefinitionDTO> listDefinitions() {
    return List.of();
  }
}
