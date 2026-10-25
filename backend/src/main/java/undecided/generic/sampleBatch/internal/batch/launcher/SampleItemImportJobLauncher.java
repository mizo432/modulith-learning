package undecided.generic.sampleBatch.internal.batch.launcher;

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
 * サンプルアイテムインポートバッチジョブの実行およびスケジューリングを制御するランチャークラス。
 *
 * <p>バッチ基本構成サンプルとして、JobLauncherの標準パターンを示します。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleItemImportJobLauncher {

  private final JobOperator jobOperator;
  private final Job sampleItemImportJob;

  /**
   * 指定された入力ファイルを使用してジョブを実行します。
   *
   * @param inputFile CSVファイルパス (nullまたは空文字の場合はデフォルトファイルを使用)
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run(String inputFile) throws Exception {
    JobParametersBuilder builder =
        new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis());
    if (inputFile != null && !inputFile.isBlank()) {
      builder.addString("inputFile", inputFile);
    }
    JobParameters parameters = builder.toJobParameters();
    log.info("Starting sample item import job with parameters: {}", parameters);
    return jobOperator.start(sampleItemImportJob, parameters);
  }

  /**
   * デフォルト設定でジョブを実行します。
   *
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run() throws Exception {
    return run(null);
  }

  /** スケジュールに基づいて定期的にインポートジョブを起動します。 */
  @Scheduled(cron = "${batch.sample-item-import.cron:0 0 3 * * ?}")
  public void scheduledRun() {
    try {
      log.info("Triggering scheduled sample item import job");
      run();
    } catch (Exception e) {
      log.error("Failed to run scheduled sample item import job", e);
    }
  }
}
