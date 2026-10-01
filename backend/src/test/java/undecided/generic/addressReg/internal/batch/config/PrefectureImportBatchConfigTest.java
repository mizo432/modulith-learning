package undecided.generic.addressReg.internal.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import undecided.generic.addressReg.internal.PrefectureRepository;
import undecided.generic.addressReg.internal.batch.launcher.PrefectureImportJobLauncher;
import undecided.generic.addressReg.spi.Prefecture;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("PrefectureImportBatchConfigの統合テスト")
class PrefectureImportBatchConfigTest {

  @Autowired private JobLauncher jobLauncher;

  @Autowired private Job prefectureImportJob;

  @Autowired private PrefectureImportJobLauncher batchLauncher;

  @Autowired private PrefectureRepository prefectureRepository;

  @Nested
  @DisplayName("prefectureImportJobの実行テスト")
  class PrefectureImportJobTest {

    @Test
    @DisplayName("デジタル庁の47都道府県マスターCSVを正常にインポートできること")
    void shouldImportAll47PrefecturesSuccessfully() throws Exception {
      // Arrange
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobLauncher.run(prefectureImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(prefectureRepository.count()).isEqualTo(47L);

      // 個別データの検証
      Prefecture hokkaido = prefectureRepository.findByPrefectureCode("01");
      assertThat(hokkaido).isNotNull();
      assertThat(hokkaido.getPrefName()).isEqualTo("北海道");
      assertThat(hokkaido.getPrefKana()).isEqualTo("ホッカイドウ");
      assertThat(hokkaido.getPrefRoma()).isEqualTo("Hokkaido");
      assertThat(hokkaido.getLgCode()).isEqualTo("010006");
      assertThat(hokkaido.getEffectiveDate()).isEqualTo(LocalDate.of(1947, 4, 17));
      assertThat(hokkaido.getAbolitionData()).isEqualTo(LocalDate.of(9999, 12, 31));

      Prefecture tokyo = prefectureRepository.findByPrefectureCode("13");
      assertThat(tokyo).isNotNull();
      assertThat(tokyo.getPrefName()).isEqualTo("東京都");
      assertThat(tokyo.getPrefKana()).isEqualTo("トウキョウト");
      assertThat(tokyo.getPrefRoma()).isEqualTo("Tokyo");
      assertThat(tokyo.getLgCode()).isEqualTo("130001");

      Prefecture okinawa = prefectureRepository.findByPrefectureCode("47");
      assertThat(okinawa).isNotNull();
      assertThat(okinawa.getPrefName()).isEqualTo("沖縄県");
      assertThat(okinawa.getPrefKana()).isEqualTo("オキナワケン");
      assertThat(okinawa.getPrefRoma()).isEqualTo("Okinawa");
      assertThat(okinawa.getEffectiveDate()).isEqualTo(LocalDate.of(1972, 5, 15));
    }

    @Test
    @DisplayName("ジョブを再実行した場合でも冪等に完了し47件が維持されること")
    void shouldExecuteIdempotentlyWhenRerun() throws Exception {
      // Arrange & Act
      JobExecution jobExecution1 = batchLauncher.run();
      JobExecution jobExecution2 = batchLauncher.run();

      // Assert
      assertThat(jobExecution1.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(jobExecution2.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(prefectureRepository.count()).isEqualTo(47L);
    }
  }
}
