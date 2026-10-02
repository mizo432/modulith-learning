package undecided.generic.addressReg.internal.batch.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
import undecided.generic.addressReg.internal.CityRepository;
import undecided.generic.addressReg.internal.batch.launcher.CityImportJobLauncher;
import undecided.generic.addressReg.spi.City;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("CityImportBatchConfigの統合テスト")
class CityImportBatchConfigTest {

  @Autowired private JobOperator jobOperator;

  @Autowired private Job cityImportJob;

  @Autowired private CityImportJobLauncher batchLauncher;

  @Autowired private CityRepository cityRepository;

  @Nested
  @DisplayName("cityImportJobの実行テスト")
  class CityImportJobTest {

    @Test
    @DisplayName("デジタル庁の市区町村マスターCSVを正常にインポートできること")
    void shouldImportAllCitiesSuccessfully() throws Exception {
      // Arrange
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobOperator.start(cityImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(cityRepository.count()).isEqualTo(63L);

      // 個別データの検証 (政令指定都市・区)
      City sapporoChuo = cityRepository.findByLgCode("011011");
      assertThat(sapporoChuo).isNotNull();
      assertThat(sapporoChuo.getPrefectureId()).isEqualTo(1L);
      assertThat(sapporoChuo.getCityName()).isEqualTo("札幌市");
      assertThat(sapporoChuo.getWardName()).isEqualTo("中央区");
      assertThat(sapporoChuo.getWardKana()).isEqualTo("チュウオウク");
      assertThat(sapporoChuo.getWardRoma()).isEqualTo("Chuo-ku");
      assertThat(sapporoChuo.getEffectiveDate()).isEqualTo(LocalDate.of(1947, 4, 17));
      assertThat(sapporoChuo.getAbolitionData()).isEqualTo(LocalDate.of(9999, 12, 31));

      // 個別データの検証 (郡部・町村)
      City minamifurano = cityRepository.findByLgCode("014605");
      assertThat(minamifurano).isNotNull();
      assertThat(minamifurano.getPrefectureId()).isEqualTo(1L);
      assertThat(minamifurano.getCountryName()).isEqualTo("空知郡");
      assertThat(minamifurano.getCountryKana()).isEqualTo("ソラチグン");
      assertThat(minamifurano.getCountryRoma()).isEqualTo("Sorachi-gun");
      assertThat(minamifurano.getCityName()).isEqualTo("南富良野町");
      assertThat(minamifurano.getWardName()).isNull();

      // 個別データの検証 (特別区: 東京都千代田区)
      City chiyoda = cityRepository.findByLgCode("131016");
      assertThat(chiyoda).isNotNull();
      assertThat(chiyoda.getPrefectureId()).isEqualTo(13L);
      assertThat(chiyoda.getCityName()).isEqualTo("千代田区");
      assertThat(chiyoda.getCityKana()).isEqualTo("チヨダク");
      assertThat(chiyoda.getCityRoma()).isEqualTo("Chiyoda-ku");

      // 都道府県IDでの検索検証
      List<City> hokkaidoCities = cityRepository.findByPrefectureId(1L);
      assertThat(hokkaidoCities).isNotEmpty();
    }

    @Test
    @DisplayName("ジョブを再実行した場合でも冪等に完了し件数が維持されること")
    void shouldExecuteIdempotentlyWhenRerun() throws Exception {
      // Arrange & Act
      JobExecution jobExecution1 = batchLauncher.run();
      JobExecution jobExecution2 = batchLauncher.run();

      // Assert
      assertThat(jobExecution1.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(jobExecution2.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(cityRepository.count()).isEqualTo(63L);
    }
  }
}
