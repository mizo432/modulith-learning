package undecided.generic.documentMgmt.internal.batch.operator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 帳票物理削除バッチジョブの実行およびスケジューリングを制御するオペレータークラス。
 *
 * <p>論理削除から1年が経過した帳票を、ストレージと DB の両方から物理削除する。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentPhysicalDeleteJobOperator {

  private final JobOperator jobOperator;
  private final Job documentPhysicalDeleteJob;

  /**
   * 物理削除ジョブを実行します。
   *
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run() throws Exception {
    JobParameters parameters =
        new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis()).toJobParameters();
    log.info("Starting document physical delete job");
    return jobOperator.start(documentPhysicalDeleteJob, parameters);
  }

  /** スケジュールに基づいて定期的に物理削除ジョブを起動します（デフォルトは毎日3時）。 */
  @Scheduled(cron = "${batch.document-physical-delete.cron:0 0 3 * * ?}")
  public void scheduledRun() {
    try {
      log.info("Triggering scheduled document physical delete job");
      JobExecution execution = run();
      log.info("Scheduled document physical delete job completed with status: {}", execution.getStatus());
    } catch (Exception e) {
      log.error("Failed to run scheduled document physical delete job", e);
    }
  }
}
