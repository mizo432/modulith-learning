package undecided.generic.bankReg.internal.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.generic.bankReg.internal.BankMasterImportRepository;
import undecided.generic.bankReg.internal.BankRepository;
import undecided.generic.bankReg.spi.bank.Bank;
import undecided.generic.bankReg.spi.bank.BankCode;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("BankImportBatchConfigの統合テスト")
class BankImportBatchConfigTest {

  @Autowired private JobOperator jobOperator;

  @Autowired private Job bankImportJob;

  @Autowired private BankRepository bankRepository;

  @Autowired private BankMasterImportRepository bankMasterImportRepository;

  @Nested
  @DisplayName("bankImportJobの実行テスト")
  class BankImportJobTest {

    @Test
    @DisplayName("ZIPファイル内のbanks.csvを正常にインポートできること")
    void shouldImportBanksFromZipSuccessfully() throws Exception {
      // Arrange
      String zipPath = new ClassPathResource("data/bank-import/test-banks.zip").getFile().getPath();
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .addString("zipFile", zipPath)
              .addString("datasetId", "test-dataset-001")
              .addString("zipSha256", "abc123")
              .addLong("bankRowCount", 10L)
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobOperator.start(bankImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(bankRepository.count()).isEqualTo(10L);

      // 個別データの検証
      Optional<Bank> mizuho = bankRepository.findById(BankCode.of("0001"));
      assertThat(mizuho).isPresent();
      assertThat(mizuho.get().getBankName()).isEqualTo("みずほ銀行");
      assertThat(mizuho.get().getDatasetId()).isEqualTo("test-dataset-001");

      Optional<Bank> smbc = bankRepository.findById(BankCode.of("0002"));
      assertThat(smbc).isPresent();
      assertThat(smbc.get().getBankName()).isEqualTo("三井住友銀行");

      Optional<Bank> mufg = bankRepository.findById(BankCode.of("0003"));
      assertThat(mufg).isPresent();
      assertThat(mufg.get().getBankName()).isEqualTo("三菱UFJ銀行");
    }

    @Test
    @DisplayName("取り込み完了イベントが記録されること")
    void shouldRecordImportEvent() throws Exception {
      // Arrange
      String zipPath = new ClassPathResource("data/bank-import/test-banks.zip").getFile().getPath();
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .addString("zipFile", zipPath)
              .addString("datasetId", "test-dataset-002")
              .addString("publishedAt", "2026-10-01T00:00:00+09:00")
              .addString("zipSha256", "def456")
              .addLong("bankRowCount", 10L)
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobOperator.start(bankImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(bankMasterImportRepository.findById("test-dataset-002")).isPresent();
      var importRecord = bankMasterImportRepository.findById("test-dataset-002").get();
      assertThat(importRecord.getZipSha256()).isEqualTo("def456");
      assertThat(importRecord.getBankRowCount()).isEqualTo(10);
      assertThat(importRecord.getOccurredAt()).isNotNull();
    }

    @Test
    @DisplayName("古いデータセットのレコードが削除されること")
    void shouldDeleteOldDatasetRecords() throws Exception {
      // Arrange: 古いデータセットのレコードを挿入
      Bank oldBank = new Bank();
      oldBank.setBankCode(BankCode.of("9999"));
      oldBank.setBankName("古い銀行");
      oldBank.setDatasetId("old-dataset");
      bankRepository.save(oldBank);
      assertThat(bankRepository.findById(BankCode.of("9999"))).isPresent();

      String zipPath = new ClassPathResource("data/bank-import/test-banks.zip").getFile().getPath();
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .addString("zipFile", zipPath)
              .addString("datasetId", "new-dataset-001")
              .addString("zipSha256", "ghi789")
              .addLong("bankRowCount", 10L)
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobOperator.start(bankImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(bankRepository.findById(BankCode.of("9999"))).isEmpty();
      assertThat(bankRepository.count()).isEqualTo(10L);
    }
  }
}
