package undecided.jobmanagement.jobexecution.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.supporting.snowflake.SnowflakeId;

/** ジョブ実行レコードに対応する実バッチを非同期に実行し、状態を更新するコンポーネント。 */
@Component
@RequiredArgsConstructor
public class JobExecutionRunner {

  private final JobExecutionRepository jobExecutionRepository;
  private final JobOperator jobOperator;

  @Async
  public void execute(SnowflakeId executionRecordId, Job job, JobParameters parameters) {
    // TODO: #748 で実装する
  }
}
