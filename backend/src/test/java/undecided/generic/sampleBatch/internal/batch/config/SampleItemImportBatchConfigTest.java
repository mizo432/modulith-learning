package undecided.generic.sampleBatch.internal.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
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
import undecided.generic.sampleBatch.internal.SampleItemRepository;
import undecided.generic.sampleBatch.internal.batch.operator.SampleItemImportJobOperator;
import undecided.generic.sampleBatch.spi.SampleItem;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("SampleItemImportBatchConfigの統合テスト")
class SampleItemImportBatchConfigTest {

  @Autowired private JobOperator jobOperator;

  @Autowired private Job sampleItemImportJob;

  @Autowired private SampleItemImportJobOperator batchOperator;

  @Autowired private SampleItemRepository sampleItemRepository;

  @Nested
  @DisplayName("sampleItemImportJobの実行テスト")
  class SampleItemImportJobTest {

    @Test
    @DisplayName("サンプルCSVの10件を正常にインポートできること")
    void shouldImportAll10SampleItemsSuccessfully() throws Exception {
      // Arrange
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobOperator.start(sampleItemImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(sampleItemRepository.count()).isEqualTo(10L);

      // 個別データの検証
      List<SampleItem> items = new ArrayList<>();
      sampleItemRepository.findAll().forEach(items::add);
      assertThat(items).hasSize(10);

      SampleItem firstItem = items.stream()
          .filter(i -> "ITEM001".equals(i.getItemCode()))
          .findFirst()
          .orElse(null);
      assertThat(firstItem).isNotNull();
      assertThat(firstItem.getItemName()).isEqualTo("サンプル商品A");
      assertThat(firstItem.getCategory()).isEqualTo("食品");
      assertThat(firstItem.getPrice()).isEqualTo(1000L);

      SampleItem lastItem = items.stream()
          .filter(i -> "ITEM010".equals(i.getItemCode()))
          .findFirst()
          .orElse(null);
      assertThat(lastItem).isNotNull();
      assertThat(lastItem.getItemName()).isEqualTo("サンプル商品J");
      assertThat(lastItem.getCategory()).isEqualTo("食品");
      assertThat(lastItem.getPrice()).isEqualTo(1800L);
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
      JobExecution jobExecution =
          batchOperator.run("classpath:data/sample-items.csv");

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(sampleItemRepository.count()).isGreaterThan(0L);
    }
  }
}
