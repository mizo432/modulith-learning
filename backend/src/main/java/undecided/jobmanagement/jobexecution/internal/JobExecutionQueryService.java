package undecided.jobmanagement.jobexecution.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;

/** ジョブ実行の参照系サービス（UC-JB-013 の最小範囲）。 */
@Service
@RequiredArgsConstructor
public class JobExecutionQueryService {

  private final JobExecutionRepository jobExecutionRepository;

  public List<JobExecutionDTO> listExecutions() {
    return List.of();
  }
}
