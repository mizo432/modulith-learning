package undecided.generic.largeRecordBatch.internal.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.generic.largeRecordBatch.internal.LargeRecordRepository;
import undecided.generic.largeRecordBatch.internal.batch.operator.LargeRecordImportJobOperator;
import undecided.generic.largeRecordBatch.spi.LargeRecord;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("LargeRecordImportBatchConfigの統合テスト")
class LargeRecordImportBatchConfigTest {

  @Autowired private JobOperator jobOperator;

  @Autowired private Job largeRecordImportJob;

  @Autowired private LargeRecordImportJobOperator batchOperator;

  @Autowired private LargeRecordRepository largeRecordRepository;

  @BeforeEach
  void setUp() {
    largeRecordRepository.deleteAll();
  }

  @Nested
  @DisplayName("largeRecordImportJobの実行テスト")
  class LargeRecordImportJobTest {

    @Test
    @DisplayName("1000件以上のレコードを正常にインポートできること")
    void shouldImportOver1000RecordsSuccessfully() throws Exception {
      // Arrange
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobOperator.start(largeRecordImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      // 1050件の正常レコード + 6件のテスト用レコード（空コード・空名はスキップ、他は処理される）
      // 空コード(1件)と空名(1件)はスキップされるため、1054件が保存される
      assertThat(largeRecordRepository.count()).isGreaterThan(1000L);
    }

    @Test
    @DisplayName("チャンクサイズ200で大量データを処理できること")
    void shouldProcessLargeDataWithChunkSize200() throws Exception {
      // Arrange & Act
      JobExecution jobExecution = batchOperator.run();

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(largeRecordRepository.count()).isGreaterThan(1000L);
    }

    @Test
    @DisplayName("不正なレコードはスキップまたはフォールトトレラントに処理されること")
    void shouldHandleInvalidRecordsFaultTolerantly() throws Exception {
      // Arrange & Act
      JobExecution jobExecution = batchOperator.run();

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

      // 不正な金額・数量・ステータスのレコードが処理されていることを確認
      List<LargeRecord> records = new ArrayList<>();
      largeRecordRepository.findAll().forEach(records::add);

      // 無効金額レコード（REC_INVALID2）はamountがnullで保存される
      LargeRecord invalidAmountRecord =
          records.stream()
              .filter(r -> "REC_INVALID2".equals(r.getRecordCode()))
              .findFirst()
              .orElse(null);
      assertThat(invalidAmountRecord).isNotNull();
      assertThat(invalidAmountRecord.getAmount()).isNull();

      // 無効数量レコード（REC_INVALID3）はquantityがnullで保存される
      LargeRecord invalidQuantityRecord =
          records.stream()
              .filter(r -> "REC_INVALID3".equals(r.getRecordCode()))
              .findFirst()
              .orElse(null);
      assertThat(invalidQuantityRecord).isNotNull();
      assertThat(invalidQuantityRecord.getQuantity()).isNull();

      // 無効ステータスレコード（REC_INVALID4）はACTIVEにデフォルト
      LargeRecord invalidStatusRecord =
          records.stream()
              .filter(r -> "REC_INVALID4".equals(r.getRecordCode()))
              .findFirst()
              .orElse(null);
      assertThat(invalidStatusRecord).isNotNull();
      assertThat(invalidStatusRecord.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("空のrecordCodeまたはrecordNameのレコードはスキップされること")
    void shouldSkipRecordsWithEmptyCodeOrName() throws Exception {
      // Arrange & Act
      JobExecution jobExecution = batchOperator.run();

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

      List<LargeRecord> records = new ArrayList<>();
      largeRecordRepository.findAll().forEach(records::add);

      // 空コードのレコードは保存されない
      assertThat(records).noneMatch(r -> r.getRecordCode().isEmpty());
      // 空名のレコードは保存されない
      assertThat(records).noneMatch(r -> r.getRecordName().isEmpty());
    }

    @Test
    @DisplayName("ジョブを再実行した場合でも完了すること")
    void shouldExecuteSuccessfullyWhenRerun() throws Exception {
      // Arrange & Act
      JobExecution jobExecution1 = batchOperator.run();
      JobExecution jobExecution2 = batchOperator.run();

      // Assert
      assertThat(jobExecution1.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(jobExecution2.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("指定したCSVファイルパスでジョブを実行できること")
    void shouldRunWithSpecifiedInputFile() throws Exception {
      // Arrange & Act
      JobExecution jobExecution = batchOperator.run("classpath:data/large-records.csv");

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(largeRecordRepository.count()).isGreaterThan(1000L);
    }

    @Test
    @DisplayName("カテゴリとサブカテゴリが正常に設定されること")
    void shouldSetCategoryAndSubCategoryCorrectly() throws Exception {
      // Arrange & Act
      JobExecution jobExecution = batchOperator.run();

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

      List<LargeRecord> records = new ArrayList<>();
      largeRecordRepository.findAll().forEach(records::add);

      // カテゴリAのレコードが存在すること
      assertThat(records).anyMatch(r -> "カテゴリA".equals(r.getCategory()));
      // サブ1のレコードが存在すること
      assertThat(records).anyMatch(r -> "サブ1".equals(r.getSubCategory()));
    }

    @Test
    @DisplayName("ステータスの正規化が正常に機能すること")
    void shouldNormalizeStatusCorrectly() throws Exception {
      // Arrange & Act
      JobExecution jobExecution = batchOperator.run();

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

      List<LargeRecord> records = new ArrayList<>();
      largeRecordRepository.findAll().forEach(records::add);

      // すべてのレコードのステータスはACTIVEまたはINACTIVEのみ
      assertThat(records)
          .allMatch(r -> "ACTIVE".equals(r.getStatus()) || "INACTIVE".equals(r.getStatus()));
    }
  }
}
