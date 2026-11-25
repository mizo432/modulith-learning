package undecided.jobmanagement.jobexecution.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionId;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.supporting.snowflake.SnowflakeId;

class JobExecutionRunnerTest {

  private JobExecutionRepository repository;
  private JobOperator jobOperator;
  private JobExecutionRunner runner;
  private JobExecution record;
  private Job job;
  private SnowflakeId recordId;

  @BeforeEach
  void setUp() {
    repository = mock(JobExecutionRepository.class);
    jobOperator = mock(JobOperator.class);
    runner = new JobExecutionRunner(repository, jobOperator);
    recordId = SnowflakeId.of(1L);
    record = new JobExecution();
    record.setId(recordId);
    record.setExecutionId(ExecutionId.of("exec-1"));
    record.setJobCode("sampleJob");
    record.setState(ExecutionState.PENDING);
    when(repository.findById(recordId)).thenReturn(Optional.of(record));
    job = mock(Job.class);
  }

  @Test
  void バッチが正常終了したときSUCCESSになる() throws Exception {
    org.springframework.batch.core.job.JobExecution batchExecution =
        mock(org.springframework.batch.core.job.JobExecution.class);
    when(batchExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
    when(jobOperator.start(any(Job.class), any(JobParameters.class))).thenReturn(batchExecution);

    runner.execute(recordId, job, new JobParametersBuilder().toJobParameters());

    assertThat(record.getState()).isEqualTo(ExecutionState.SUCCESS);
    assertThat(record.getStartedAt()).isNotNull();
    assertThat(record.getCompletedAt()).isNotNull();
  }

  @Test
  void バッチが異常終了したときFAILEDになりエラー情報が記録される() throws Exception {
    org.springframework.batch.core.job.JobExecution batchExecution =
        mock(org.springframework.batch.core.job.JobExecution.class);
    when(batchExecution.getStatus()).thenReturn(BatchStatus.FAILED);
    when(batchExecution.getAllFailureExceptions()).thenReturn(List.of(new RuntimeException("boom")));
    when(jobOperator.start(any(Job.class), any(JobParameters.class))).thenReturn(batchExecution);

    runner.execute(recordId, job, new JobParametersBuilder().toJobParameters());

    assertThat(record.getState()).isEqualTo(ExecutionState.FAILED);
    assertThat(record.getErrorMessage()).contains("boom");
    assertThat(record.getCompletedAt()).isNotNull();
  }
}
