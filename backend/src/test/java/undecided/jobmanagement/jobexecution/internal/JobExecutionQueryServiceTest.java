package undecided.jobmanagement.jobexecution.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionId;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.supporting.snowflake.SnowflakeId;

class JobExecutionQueryServiceTest {

  @Test
  void 実行一覧を取得したとき新しい順に実行が返る() {
    JobExecutionRepository repository = mock(JobExecutionRepository.class);
    JobExecution newer =
        execution(2L, "exec-2", ExecutionState.RUNNING, LocalDateTime.of(2026, 11, 26, 9, 0));
    JobExecution older =
        execution(1L, "exec-1", ExecutionState.SUCCESS, LocalDateTime.of(2026, 11, 26, 8, 0));
    when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(newer, older));
    JobExecutionQueryService service = new JobExecutionQueryService(repository);

    List<JobExecutionDTO> result = service.listExecutions();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getExecutionId()).isEqualTo("exec-2");
    assertThat(result.get(0).getState()).isEqualTo("RUNNING");
    assertThat(result.get(1).getExecutionId()).isEqualTo("exec-1");
  }

  private JobExecution execution(
      Long id, String executionId, ExecutionState state, LocalDateTime createdAt) {
    JobExecution e = new JobExecution();
    e.setId(SnowflakeId.of(id));
    e.setExecutionId(ExecutionId.of(executionId));
    e.setJobCode("sampleJob");
    e.setState(state);
    e.setCreatedAt(createdAt);
    e.setCreatedBy("100");
    return e;
  }
}
