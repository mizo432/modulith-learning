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
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.generic.addressReg.internal.ChouAzaRepository;
import undecided.generic.addressReg.internal.batch.launcher.ChouAzaImportJobLauncher;
import undecided.generic.addressReg.spi.ChouAza;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("ChouAzaImportBatchConfigの統合テスト")
class ChouAzaImportBatchConfigTest {

  @Autowired private JobLauncher jobLauncher;

  @Autowired private Job chouAzaImportJob;

  @Autowired private ChouAzaImportJobLauncher batchLauncher;

  @Autowired private ChouAzaRepository chouAzaRepository;

  @Nested
  @DisplayName("chouAzaImportJobの実行テスト")
  class ChouAzaImportJobTest {

    @Test
    @DisplayName("デジタル庁の町字マスターCSVを正常にインポートできること")
    void shouldImportAllChouAzaSuccessfully() throws Exception {
      // Arrange
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();

      // Act
      JobExecution jobExecution = jobLauncher.run(chouAzaImportJob, jobParameters);

      // Assert
      assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(chouAzaRepository.count()).isEqualTo(16L);

      // 個別データの検証 (札幌市中央区 旭ケ丘１丁目)
      ChouAza asahigaoka1 = chouAzaRepository.findByLgCodeAndMachiazaCode("011011", "0001001");
      assertThat(asahigaoka1).isNotNull();
      assertThat(asahigaoka1.getId()).isEqualTo(110110001001L);
      assertThat(asahigaoka1.getCityId()).isEqualTo(11011L);
      assertThat(asahigaoka1.getOazaChoName()).isEqualTo("旭ケ丘");
      assertThat(asahigaoka1.getOazaChoKana()).isEqualTo("アサヒガオカ");
      assertThat(asahigaoka1.getOazaChoRoma()).isEqualTo("Asahigaoka");
      assertThat(asahigaoka1.getChomeName()).isEqualTo("１丁目");
      assertThat(asahigaoka1.getChomeKana()).isEqualTo("１チョウメ");
      assertThat(asahigaoka1.getChomeNumber()).isEqualTo("1");
      assertThat(asahigaoka1.getRsdtAddrFlg()).isTrue();
      assertThat(asahigaoka1.getEffectiveDate()).isEqualTo(LocalDate.of(1947, 4, 17));
      assertThat(asahigaoka1.getAbolitionData()).isEqualTo(LocalDate.of(9999, 12, 31));

      // 個別データの検証 (千代田区 丸の内１丁目)
      ChouAza marunouchi1 = chouAzaRepository.findByLgCodeAndMachiazaCode("131016", "0001001");
      assertThat(marunouchi1).isNotNull();
      assertThat(marunouchi1.getCityId()).isEqualTo(131016L);
      assertThat(marunouchi1.getOazaChoName()).isEqualTo("丸の内");
      assertThat(marunouchi1.getOazaChoKana()).isEqualTo("マルノウチ");
      assertThat(marunouchi1.getOazaChoRoma()).isEqualTo("Marunouchi");

      // 個別データの検証 (那覇市 泉崎１丁目)
      ChouAza izumizaki1 = chouAzaRepository.findByLgCodeAndMachiazaCode("472018", "0001001");
      assertThat(izumizaki1).isNotNull();
      assertThat(izumizaki1.getCityId()).isEqualTo(472018L);
      assertThat(izumizaki1.getOazaChoName()).isEqualTo("泉崎");
      assertThat(izumizaki1.getEffectiveDate()).isEqualTo(LocalDate.of(1972, 5, 15));

      // 市区町村IDでの検索検証
      List<ChouAza> sapporoChuoList = chouAzaRepository.findByCityId(11011L);
      assertThat(sapporoChuoList).hasSize(4);

      // 法定コードでの検索検証
      List<ChouAza> chiyodaList = chouAzaRepository.findByLgCode("131016");
      assertThat(chiyodaList).hasSize(5);
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
      assertThat(chouAzaRepository.count()).isEqualTo(16L);
    }
  }
}
