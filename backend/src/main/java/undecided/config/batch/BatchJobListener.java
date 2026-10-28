package undecided.config.batch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import com.github.mizo432.shared.logger.LogIdBasedLogger;

/**
 * バッチジョブの開始・終了時にログ出力を行う共通リスナークラス。
 *
 * <p>すべてのバッチジョブにこのリスナーを適用することで、ジョブの開始・終了タイミングを 一貫してログに記録します。
 */
public class BatchJobListener implements JobExecutionListener {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final LogIdBasedLogger log = LogIdBasedLogger.getLogger(BatchJobListener.class);

  @Override
  public void beforeJob(JobExecution jobExecution) {
    String jobName = jobExecution.getJobInstance().getJobName();
    String startTime = LocalDateTime.now().format(FORMATTER);
    log.info("i.batch.1001", jobName, startTime);
    log.info("i.batch.1002", jobExecution.getJobParameters());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    String jobName = jobExecution.getJobInstance().getJobName();
    String endTime = LocalDateTime.now().format(FORMATTER);
    String status = jobExecution.getStatus().name();

    log.info("i.batch.1003", jobName, endTime, status);
    log.info("i.batch.1004", jobExecution.getExitStatus().getExitCode());

    // ステップごとの実行情報をログ出力
    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
      log.info(
          "i.batch.1005",
          stepExecution.getStepName(),
          stepExecution.getStatus(),
          stepExecution.getReadCount(),
          stepExecution.getWriteCount(),
          stepExecution.getSkipCount());
    }

    // エラーがある場合はログ出力
    if (!jobExecution.getAllFailureExceptions().isEmpty()) {
      log.warn(
          "w.batch.2001", jobName, jobExecution.getAllFailureExceptions().size());
      jobExecution
          .getAllFailureExceptions()
          .forEach(e -> log.warn("w.batch.2002", e.getMessage()));
    }
  }
}
