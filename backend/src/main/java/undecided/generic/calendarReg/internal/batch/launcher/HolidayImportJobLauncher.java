package undecided.generic.calendarReg.internal.batch.launcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 祝日インポートバッチジョブの実行およびスケジューリングを制御するランチャークラス。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayImportJobLauncher {

  private final JobLauncher jobLauncher;
  private final Job holidayImportJob;

  /**
   * 指定された入力ファイルおよび文字コードを使用してジョブを実行します。
   *
   * @param inputFile CSV ファイルパス (null または空文字の場合はデフォルトファイルを使用)
   * @param charset 文字コード名 (null または空文字の場合は UTF-8 を使用)
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run(String inputFile, String charset) throws Exception {
    JobParametersBuilder builder =
        new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis());
    if (inputFile != null && !inputFile.isBlank()) {
      builder.addString("inputFile", inputFile);
    }
    if (charset != null && !charset.isBlank()) {
      builder.addString("charset", charset);
    }
    JobParameters parameters = builder.toJobParameters();
    log.info("Starting holiday import job with parameters: {}", parameters);
    return jobLauncher.run(holidayImportJob, parameters);
  }

  /**
   * 指定された入力ファイルを使用してジョブを実行します。
   *
   * @param inputFile CSV ファイルパス (null または空文字の場合はデフォルトファイルを使用)
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run(String inputFile) throws Exception {
    return run(inputFile, null);
  }

  /**
   * デフォルト設定でジョブを実行します。
   *
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run() throws Exception {
    return run(null, null);
  }

  /** スケジュールに基づいて定期的にインポートジョブを起動します。 */
  @Scheduled(cron = "${batch.holiday-import.cron:0 0 4 * * ?}")
  public void scheduledRun() {
    try {
      log.info("Triggering scheduled holiday import job");
      run();
    } catch (Exception e) {
      log.error("Failed to run scheduled holiday import job", e);
    }
  }
}
