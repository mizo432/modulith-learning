package undecided.shared.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListener;
import undecided.shared.logger.LogIdBasedLogger;

/**
 * バッチステップの開始・終了時にログ出力を行う共通リスナークラス。
 *
 * <p>すべてのバッチステップにこのリスナーを適用することで、ステップの開始・終了タイミングを 一貫してログに記録します。
 */
public class BatchStepListener implements StepExecutionListener {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final LogIdBasedLogger log = LogIdBasedLogger.getLogger(BatchStepListener.class);

  @Override
  public void beforeStep(StepExecution stepExecution) {
    String stepName = stepExecution.getStepName();
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String startTime = LocalDateTime.now().format(FORMATTER);
    log.info("i.batch.1011", jobName, stepName, startTime);
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    String stepName = stepExecution.getStepName();
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String endTime = LocalDateTime.now().format(FORMATTER);
    String status = stepExecution.getStatus().name();

    log.info(
        "i.batch.1012",
        jobName,
        stepName,
        endTime,
        status,
        stepExecution.getReadCount(),
        stepExecution.getWriteCount(),
        stepExecution.getSkipCount());

    // エラーがある場合はログ出力
    if (!stepExecution.getFailureExceptions().isEmpty()) {
      log.warn("w.batch.2011", jobName, stepName, stepExecution.getFailureExceptions().size());
      stepExecution
          .getFailureExceptions()
          .forEach(e -> log.warn("w.batch.2012", e.getMessage()));
    }
    return null;
  }
}
