package undecided.generic.bankReg.internal.batch.config;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import undecided.generic.bankReg.internal.BankMasterImport;
import undecided.generic.bankReg.internal.BankMasterImportRepository;
import undecided.generic.bankReg.internal.BankRepository;
import undecided.generic.bankReg.internal.batch.dto.BankCsvDto;
import undecided.generic.bankReg.internal.batch.processor.BankItemProcessor;
import undecided.generic.bankReg.internal.batch.reader.BankCsvItemReader;
import undecided.generic.bankReg.internal.batch.writer.BankItemWriter;
import undecided.generic.bankReg.spi.bank.Bank;

/**
 * 金融機関マスターデータ自動インポート用 Spring Batch 設定クラス。
 *
 * <p>ジョブは以下のステップで構成されます：
 *
 * <ol>
 *   <li>downloadStep — Master Export ZIP のダウンロード
 *   <li>importStep — banks.csv の読み込み・変換・永続化
 *   <li>cleanupStep — 古いデータセットの金融機関レコードを削除
 *   <li>recordImportStep — 取り込み完了イベントを記録
 * </ol>
 */
@Configuration
public class BankImportBatchConfig {

  public static final String JOB_NAME = "bankImportJob";
  public static final String DOWNLOAD_STEP = "bankDownloadStep";
  public static final String IMPORT_STEP = "bankImportStep";
  public static final String CLEANUP_STEP = "bankCleanupStep";
  public static final String RECORD_IMPORT_STEP = "bankRecordImportStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public BankCsvItemReader bankCsvItemReader(
      @Value("#{jobParameters['zipFile']}") String zipFilePath) {
    BankCsvItemReader reader = new BankCsvItemReader();
    if (zipFilePath != null && !zipFilePath.isBlank()) {
      reader.setZipFile(Path.of(zipFilePath));
    }
    return reader;
  }

  @Bean
  public Step downloadStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder(DOWNLOAD_STEP, jobRepository)
        .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
        .build();
  }

  @Bean
  public Step importStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      BankCsvItemReader reader,
      BankItemProcessor processor,
      BankItemWriter writer) {
    return new StepBuilder(IMPORT_STEP, jobRepository)
        .<BankCsvDto, Bank>chunk(CHUNK_SIZE)
        .transactionManager(transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .faultTolerant()
        .skip(Exception.class)
        .skipLimit(10)
        .retry(Exception.class)
        .retryLimit(3)
        .build();
  }

  @Bean
  @StepScope
  public Tasklet cleanupTasklet(
      BankRepository bankRepository, @Value("#{jobParameters['datasetId']}") String datasetId) {
    return (contribution, chunkContext) -> {
      if (datasetId != null && !datasetId.isBlank()) {
        int deleted = bankRepository.deleteByDatasetIdNot(datasetId);
        contribution.incrementWriteCount(deleted);
      }
      return RepeatStatus.FINISHED;
    };
  }

  @Bean
  public Step cleanupStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      Tasklet cleanupTasklet) {
    return new StepBuilder(CLEANUP_STEP, jobRepository)
        .tasklet(cleanupTasklet, transactionManager)
        .build();
  }

  @Bean
  @StepScope
  public Tasklet recordImportTasklet(
      BankMasterImportRepository bankMasterImportRepository,
      @Value("#{jobParameters['datasetId']}") String datasetId,
      @Value("#{jobParameters['publishedAt']}") String publishedAtStr,
      @Value("#{jobParameters['zipSha256']}") String zipSha256,
      @Value("#{jobParameters['bankRowCount']}") Long bankRowCountLong) {
    return (contribution, chunkContext) -> {
      if (datasetId != null && !datasetId.isBlank()) {
        BankMasterImport importRecord = new BankMasterImport();
        importRecord.setDatasetId(datasetId);
        importRecord.setPublishedAt(
            publishedAtStr != null && !publishedAtStr.isBlank()
                ? OffsetDateTime.parse(publishedAtStr)
                : null);
        importRecord.setOccurredAt(OffsetDateTime.now());
        importRecord.setZipSha256(zipSha256 != null ? zipSha256 : "");
        importRecord.setBankRowCount(bankRowCountLong != null ? bankRowCountLong.intValue() : 0);
        bankMasterImportRepository.save(importRecord);
        contribution.incrementWriteCount(1);
      }
      return RepeatStatus.FINISHED;
    };
  }

  @Bean
  public Step recordImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      Tasklet recordImportTasklet) {
    return new StepBuilder(RECORD_IMPORT_STEP, jobRepository)
        .tasklet(recordImportTasklet, transactionManager)
        .build();
  }

  @Bean
  public Job bankImportJob(
      JobRepository jobRepository,
      Step downloadStep,
      Step importStep,
      Step cleanupStep,
      Step recordImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(downloadStep)
        .next(importStep)
        .next(cleanupStep)
        .next(recordImportStep)
        .build();
  }
}
