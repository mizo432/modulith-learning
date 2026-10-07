package undecided.generic.bankReg.internal.batch.launcher;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import undecided.generic.bankReg.internal.BankMasterImportRepository;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpMasterExportClient;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpProperties;
import undecided.generic.bankReg.internal.batch.client.MasterExportDataset;
import undecided.generic.bankReg.internal.batch.client.MasterExportDownloadUrl;

/**
 * 金融機関インポートバッチジョブの実行およびスケジューリングを制御するランチャークラス。
 *
 * <p>API キーが設定されている場合、BankcodeJP Master Export API から最新のデータセットを 自動的にダウンロードしてインポートします。API
 * キーが未設定の場合は、ローカル CSV ファイルから インポートします。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankImportJobLauncher {

  private final JobOperator jobOperator;
  private final Job bankImportJob;
  private final BankcodeJpMasterExportClient bankcodeJpClient;
  private final BankcodeJpProperties bankcodeJpProperties;
  private final BankMasterImportRepository bankMasterImportRepository;

  @Value("${batch.bank-import.zip-dir:#{systemProperties['java.io.tmpdir']}}")
  private String zipDir;

  /**
   * 指定された入力ファイルおよびデータセット情報を使用してジョブを実行します。
   *
   * @param zipFile ZIP ファイルパス
   * @param datasetId データセットID
   * @param publishedAt 公開日時 (ISO-8601 文字列)
   * @param zipSha256 ZIP の SHA-256
   * @param bankRowCount 金融機関件数
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run(
      String zipFile,
      @Nullable String datasetId,
      @Nullable String publishedAt,
      @Nullable String zipSha256,
      @Nullable Integer bankRowCount)
      throws Exception {
    JobParametersBuilder builder =
        new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis());
    if (zipFile != null && !zipFile.isBlank()) {
      builder.addString("zipFile", zipFile);
    }
    if (datasetId != null && !datasetId.isBlank()) {
      builder.addString("datasetId", datasetId);
    }
    if (publishedAt != null && !publishedAt.isBlank()) {
      builder.addString("publishedAt", publishedAt);
    }
    if (zipSha256 != null && !zipSha256.isBlank()) {
      builder.addString("zipSha256", zipSha256);
    }
    if (bankRowCount != null) {
      builder.addLong("bankRowCount", bankRowCount.longValue());
    }
    JobParameters parameters = builder.toJobParameters();
    log.info("Starting bank import job with parameters: {}", parameters);
    return jobOperator.start(bankImportJob, parameters);
  }

  /**
   * BankcodeJP Master Export API から最新のデータセットを取得し、インポートジョブを実行します。
   *
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution runFromApi() throws Exception {
    // 最新データセット情報を取得
    MasterExportDataset dataset = bankcodeJpClient.fetchLatest();
    log.info("Fetched latest dataset: {}", dataset.datasetId());

    // 既にインポート済みかチェック
    if (bankMasterImportRepository.findById(dataset.datasetId()).isPresent()) {
      log.info("Dataset {} already imported, skipping", dataset.datasetId());
      return null;
    }

    // ダウンロード用URLを発行
    MasterExportDownloadUrl downloadUrl = bankcodeJpClient.issueDownloadUrl(dataset.datasetId());
    log.info("Issued download URL: {}", downloadUrl);

    // ZIP をダウンロード
    Path targetPath = Path.of(zipDir, "bankcode-jp-" + dataset.datasetId() + ".zip");
    bankcodeJpClient.download(downloadUrl, targetPath);
    log.info("Downloaded ZIP to: {}", targetPath);

    // インポートジョブを実行
    return run(
        targetPath.toString(),
        dataset.datasetId(),
        dataset.publishedAt() != null
            ? dataset.publishedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            : null,
        dataset.zipSha256(),
        dataset.bankRowCount());
  }

  /**
   * デフォルト設定でジョブを実行します。 API キーが設定されている場合は API から取得、否则ローカル CSV からインポートします。
   *
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run() throws Exception {
    if (bankcodeJpProperties.hasApiKey()) {
      return runFromApi();
    } else {
      log.info("No API key configured, using local CSV file");
      return run(null, null, null, null, null);
    }
  }

  /** スケジュールに基づいて定期的にインポートジョブを起動します。 */
  @Scheduled(cron = "${batch.bank-import.cron:0 0 4 * * ?}")
  public void scheduledRun() {
    try {
      log.info("Triggering scheduled bank import job");
      JobExecution execution = run();
      if (execution != null) {
        log.info("Scheduled bank import job completed with status: {}", execution.getStatus());
      } else {
        log.info("Scheduled bank import job was skipped (already up to date)");
      }
    } catch (Exception e) {
      log.error("Failed to run scheduled bank import job", e);
    }
  }
}
