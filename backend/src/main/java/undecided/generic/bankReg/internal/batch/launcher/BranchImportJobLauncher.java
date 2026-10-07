package undecided.generic.bankReg.internal.batch.launcher;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import undecided.generic.bankReg.internal.BranchMasterImport;
import undecided.generic.bankReg.internal.BranchMasterImportRepository;
import undecided.generic.bankReg.internal.BranchRepository;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpMasterExportClient;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpProperties;
import undecided.generic.bankReg.internal.batch.client.MasterExportDataset;
import undecided.generic.bankReg.internal.batch.client.MasterExportDownloadUrl;

/**
 * 金融機関支店インポートバッチジョブの実行およびスケジューリングを制御するランチャークラス。
 *
 * <p>BankcodeJP Master Export API から ZIP をダウンロードし、branches.csv を解析して支店データを更新します。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BranchImportJobLauncher {

  private final JobOperator jobOperator;
  private final Job branchImportJob;
  private final BranchRepository branchRepository;
  private final BranchMasterImportRepository branchMasterImportRepository;
  private final BankcodeJpMasterExportClient bankcodeJpClient;
  private final BankcodeJpProperties bankcodeJpProperties;

  /**
   * 指定された CSV ファイルを使用して支店インポートジョブを実行します。
   *
   * @param csvFile branches.csv のファイルパス
   * @param datasetId Master Export のデータセットID
   * @return ジョブ実行結果
   * @throws Exception ジョブ起動例外
   */
  public JobExecution run(@NonNull Path csvFile, @NonNull String datasetId) throws Exception {
    JobParametersBuilder builder =
        new JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .addString("inputFile", csvFile.toString())
            .addString("datasetId", datasetId);
    JobParameters parameters = builder.toJobParameters();
    log.info("Starting branch import job with parameters: {}", parameters);
    return jobOperator.start(branchImportJob, parameters);
  }

  /**
   * BankcodeJP Master Export API からデータを取得し、支店インポートジョブを実行します。
   *
   * <p>API キーが未設定の場合はスキップされます。
   *
   * @return ジョブ実行結果、スキップされた場合は null
   * @throws Exception ジョブ実行例外
   */
  public @Nullable JobExecution runFromApi() throws Exception {
    if (!bankcodeJpProperties.hasApiKey()) {
      log.info("BankcodeJP API key is not configured; skipping branch import from API");
      return null;
    }

    // Fetch latest dataset info
    MasterExportDataset dataset = bankcodeJpClient.fetchLatest();
    log.info("Fetched latest Master Export dataset: {}", dataset.datasetId());

    // Check if already imported
    Optional<BranchMasterImport> lastImport =
        branchMasterImportRepository.findTopByOrderByOccurredAtDesc();
    if (lastImport.isPresent() && lastImport.get().getDatasetId().equals(dataset.datasetId())) {
      log.info("Dataset {} already imported; skipping", dataset.datasetId());
      return null;
    }

    // Issue download URL
    MasterExportDownloadUrl downloadUrl = bankcodeJpClient.issueDownloadUrl(dataset.datasetId());
    log.info("Issued download URL: {}", downloadUrl);

    // Download ZIP to temp file
    Path tempZip = Path.of(System.getProperty("java.io.tmpdir"), "bankcode-jp-master-export.zip");
    bankcodeJpClient.download(downloadUrl, tempZip);
    log.info("Downloaded Master Export ZIP to {}", tempZip);

    // Extract branches.csv from ZIP
    Path tempCsv = extractBranchesCsv(tempZip);
    log.info("Extracted branches.csv to {}", tempCsv);

    try {
      // Delete outdated branches
      int deleted = branchRepository.deleteByDatasetIdNot(dataset.datasetId());
      log.info("Deleted {} outdated branch records", deleted);

      // Run import job
      JobExecution execution = run(tempCsv, dataset.datasetId());

      // Record import event
      BranchMasterImport importEvent = new BranchMasterImport();
      importEvent.setDatasetId(dataset.datasetId());
      importEvent.setPublishedAt(dataset.publishedAt());
      importEvent.setOccurredAt(OffsetDateTime.now());
      importEvent.setZipSha256(dataset.zipSha256());
      importEvent.setBranchRowCount(
          dataset.bankRowCount()); // API returns bank_row_count; actual branch count may differ
      branchMasterImportRepository.save(importEvent);
      log.info("Recorded branch import event for dataset {}", dataset.datasetId());

      return execution;
    } finally {
      // Cleanup temp files
      try {
        java.nio.file.Files.deleteIfExists(tempCsv);
        java.nio.file.Files.deleteIfExists(tempZip);
      } catch (Exception e) {
        log.warn("Failed to cleanup temp files", e);
      }
    }
  }

  private Path extractBranchesCsv(Path zipFile) throws Exception {
    Path output = Path.of(System.getProperty("java.io.tmpdir"), "branches.csv");
    try (ZipInputStream zis = new ZipInputStream(java.nio.file.Files.newInputStream(zipFile))) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (entry.getName().endsWith("branches.csv")) {
          java.nio.file.Files.copy(zis, output, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
          break;
        }
        zis.closeEntry();
      }
    }
    if (!java.nio.file.Files.exists(output)) {
      throw new RuntimeException("branches.csv not found in Master Export ZIP");
    }
    return output;
  }

  /** スケジュールに基づいて定期的に支店インポートジョブを起動します。 */
  @Scheduled(cron = "${batch.branch-import.cron:0 0 4 * * ?}")
  public void scheduledRun() {
    try {
      log.info("Triggering scheduled branch import job");
      runFromApi();
    } catch (Exception e) {
      log.error("Failed to run scheduled branch import job", e);
    }
  }
}
