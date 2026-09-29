package undecided.generic.addressReg.internal.batch.launcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 町字インポートバッチジョブの実行およびスケジューリングを制御するランチャークラス。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChouAzaImportJobLauncher {

  private final JobLauncher jobLauncher;
  private final Job chouAzaImportJob;

  /**
   * 指定された入力ファイルを使用してジョブを実行します。
   *
   * @param inputFile CSV ファイルパス (null または空文字の場合はデフォルトファイルを使用)
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public @NonNull JobExecution run(@Nullable String inputFile) throws Exception {
    JobParametersBuilder builder =
        new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis());
    if (inputFile != null && !inputFile.isBlank()) {
      builder.addString("inputFile", inputFile);
    }
    JobParameters parameters = builder.toJobParameters();
    log.info("Starting chou aza import job with parameters: {}", parameters);
    return jobLauncher.run(chouAzaImportJob, parameters);
  }

  /**
   * デフォルト設定でジョブを実行します。
   *
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public @NonNull JobExecution run() throws Exception {
    return run(null);
  }

  /** スケジュールに基づいて定期的にインポートジョブを起動します。 */
  @Scheduled(cron = "${batch.chou-aza-import.cron:0 30 3 * * ?}")
  public void scheduledRun() {
    try {
      log.info("Triggering scheduled chou aza import job");
      run();
    } catch (Exception e) {
      log.error("Failed to run scheduled chou aza import job", e);
    }
  }
}
