package undecided.generic.calendarReg.internal.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.generic.calendarReg.internal.HolidayRepository;
import undecided.generic.calendarReg.internal.batch.launcher.HolidayImportJobLauncher;
import undecided.generic.calendarReg.spi.Holiday;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("HolidayImportBatchConfigの統合テスト")
class HolidayImportBatchConfigTest {

  @Autowired private JobLauncher jobLauncher;

  @Autowired private Job holidayImportJob;

  @Autowired private HolidayImportJobLauncher batchLauncher;

  @Autowired private HolidayRepository holidayRepository;

  @Nested
  @DisplayName("holidayImportJobの実行テスト")
  class HolidayImportJobTest {

    @Test
    @DisplayName("デジタル庁の祝日CSVを正常にインポートできること")
    void shouldImportHolidaysSuccessfully() throws Exception {
      // Arrange
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobLauncher.run(holidayImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(holidayRepository.count()).isGreaterThanOrEqualTo(50L);

      // 個別データの検証
      Optional<Holiday> ganjitsu2024 =
          holidayRepository.findByHolidayDate(LocalDate.of(2024, 1, 1));
      assertThat(ganjitsu2024).isPresent();
      assertThat(ganjitsu2024.get().getId()).isEqualTo(20240101L);
      assertThat(ganjitsu2024.get().getHolidayName()).isEqualTo("元日");

      Optional<Holiday> seijin2024 = holidayRepository.findByHolidayDate(LocalDate.of(2024, 1, 8));
      assertThat(seijin2024).isPresent();
      assertThat(seijin2024.get().getId()).isEqualTo(20240108L);
      assertThat(seijin2024.get().getHolidayName()).isEqualTo("成人の日");

      Optional<Holiday> furikae2024 =
          holidayRepository.findByHolidayDate(LocalDate.of(2024, 2, 12));
      assertThat(furikae2024).isPresent();
      assertThat(furikae2024.get().getId()).isEqualTo(20240212L);
      assertThat(furikae2024.get().getHolidayName()).isEqualTo("休日");

      Optional<Holiday> kokumin2026 =
          holidayRepository.findByHolidayDate(LocalDate.of(2026, 9, 22));
      assertThat(kokumin2026).isPresent();
      assertThat(kokumin2026.get().getId()).isEqualTo(20260922L);
      assertThat(kokumin2026.get().getHolidayName()).isEqualTo("国民の休日");
    }

    @Test
    @DisplayName("ジョブを再実行した場合でも冪等に完了し件数が維持されること")
    void shouldExecuteIdempotentlyWhenRerun() throws Exception {
      // Arrange & Act
      JobExecution jobExecution1 = batchLauncher.run();
      long countAfterFirstRun = holidayRepository.count();

      JobExecution jobExecution2 = batchLauncher.run();
      long countAfterSecondRun = holidayRepository.count();

      // Assert
      assertThat(jobExecution1.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(jobExecution2.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(countAfterSecondRun).isEqualTo(countAfterFirstRun);
    }
  }
}
