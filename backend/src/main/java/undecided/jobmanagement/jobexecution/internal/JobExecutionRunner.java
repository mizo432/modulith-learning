package undecided.jobmanagement.jobexecution.internal;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.supporting.snowflake.SnowflakeId;

/** ジョブ実行レコードに対応する実バッチを非同期に実行し、状態を更新するコンポーネント。 */
@Component
@RequiredArgsConstructor
public class JobExecutionRunner {

  private static final int MAX_ERROR_MESSAGE_LENGTH = 2000;

  private final JobExecutionRepository jobExecutionRepository;
  private final JobOperator jobOperator;

  /**
   * 実行レコードを実行中へ進め、実バッチを実行して結果で状態を更新します。
   *
   * @param executionRecordId ジョブ実行レコードのID
   * @param job 実行する実バッチの Job
   * @param parameters 実行パラメータ
   */
  @Async
  public void execute(SnowflakeId executionRecordId, Job job, JobParameters parameters) {
    JobExecution execution =
        jobExecutionRepository
            .findById(executionRecordId)
            .orElseThrow(
                () -> new IllegalStateException("ジョブ実行レコードが見つかりません: " + executionRecordId));
    execution.setState(ExecutionState.RUNNING);
    execution.setStartedAt(LocalDateTime.now());
    jobExecutionRepository.save(execution);
    try {
      JobParameters withTimestamp =
          new JobParametersBuilder(parameters)
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();
      org.springframework.batch.core.job.JobExecution batchExecution =
          jobOperator.start(job, withTimestamp);
      execution.setCompletedAt(LocalDateTime.now());
      if (batchExecution.getStatus() == BatchStatus.COMPLETED) {
        execution.setState(ExecutionState.SUCCESS);
      } else {
        execution.setState(ExecutionState.FAILED);
        execution.setErrorMessage(describeFailure(batchExecution));
      }
    } catch (Exception e) {
      execution.setCompletedAt(LocalDateTime.now());
      execution.setState(ExecutionState.FAILED);
      execution.setErrorMessage(truncate(String.valueOf(e.getMessage())));
    }
    jobExecutionRepository.save(execution);
  }

  private String describeFailure(org.springframework.batch.core.job.JobExecution batchExecution) {
    if (!batchExecution.getAllFailureExceptions().isEmpty()) {
      return truncate(String.valueOf(batchExecution.getAllFailureExceptions().get(0).getMessage()));
    }
    return "バッチが異常終了しました: " + batchExecution.getStatus();
  }

  private String truncate(String message) {
    return message.length() > MAX_ERROR_MESSAGE_LENGTH
        ? message.substring(0, MAX_ERROR_MESSAGE_LENGTH)
        : message;
  }
}
