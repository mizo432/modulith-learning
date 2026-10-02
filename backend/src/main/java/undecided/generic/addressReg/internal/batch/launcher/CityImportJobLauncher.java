package undecided.generic.addressReg.internal.batch.launcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 市区町村インポートバッチジョブの実行およびスケジューリングを制御するランチャークラス。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CityImportJobLauncher {

  private final JobOperator jobOperator;
  private final Job cityImportJob;

  /**
   * 指定された入力ファイルを使用してジョブを実行します。
   *
   * @param inputFile CSV ファイルパス (null または空文字の場合はデフォルトファイルを使用)
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
    log.info("Starting city import job with parameters: {}", parameters);
    return jobOperator.start(cityImportJob, parameters);
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
  @Scheduled(cron = "${batch.city-import.cron:0 15 3 * * ?}")
  public void scheduledRun() {
    try {
      log.info("Triggering scheduled city import job");
      run();
    } catch (Exception e) {
      log.error("Failed to run scheduled city import job", e);
    }
  }
}
