package undecided.jobmanagement.jobexecution.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;

/** ジョブ実行の参照系サービス（UC-JB-013 の最小範囲）。 */
@Service
@RequiredArgsConstructor
public class JobExecutionQueryService {

  private final JobExecutionRepository jobExecutionRepository;

  /**
   * ジョブ実行を作成日時の新しい順に返します。
   *
   * @return ジョブ実行の一覧
   */
  public List<JobExecutionDTO> listExecutions() {
    return jobExecutionRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toDto).toList();
  }

  private JobExecutionDTO toDto(JobExecution execution) {
    JobExecutionDTO dto = new JobExecutionDTO();
    dto.setId(String.valueOf(execution.getId().value()));
    dto.setExecutionId(execution.getExecutionId().getValue());
    dto.setJobCode(execution.getJobCode());
    dto.setState(execution.getState().name());
    dto.setStartedAt(execution.getStartedAt() != null ? execution.getStartedAt().toString() : null);
    dto.setCompletedAt(
        execution.getCompletedAt() != null ? execution.getCompletedAt().toString() : null);
    dto.setErrorMessage(execution.getErrorMessage());
    dto.setProcessedCount(execution.getProcessedCount());
    dto.setCheckpointPosition(execution.getCheckpointPosition());
    dto.setCreatedBy(execution.getCreatedBy());
    return dto;
  }
}
