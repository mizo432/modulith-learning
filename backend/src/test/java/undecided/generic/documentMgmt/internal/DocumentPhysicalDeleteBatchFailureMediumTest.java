package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目48: ストレージ削除が失敗したとき、DB レコードが削除されないことの統合テスト。
 *
 * <p>物理削除に必ず失敗する DocumentStorageService を差し込み、ジョブが失敗し、
 * チャンクのロールバックにより DB レコードが残ることを確認する。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import({
  TestcontainersConfiguration.class,
  DocumentPhysicalDeleteBatchFailureMediumTest.FailingStorageConfig.class
})
@DirtiesContext
@DisplayName("帳票物理削除バッチの失敗時統合テスト")
class DocumentPhysicalDeleteBatchFailureMediumTest {

  /** 物理削除が必ず失敗するストレージサービスを差し込むテスト設定。 */
  @TestConfiguration
  static class FailingStorageConfig {

    @Bean
    @Primary
    DocumentStorageService failingDocumentStorageService() {
      DocumentStorageService mock = Mockito.mock(DocumentStorageService.class);
      Mockito.doThrow(new IllegalStateException("ストレージ削除に失敗しました"))
          .when(mock)
          .delete(Mockito.anyString());
      return mock;
    }
  }

  @Autowired private JobOperator jobOperator;

  @Autowired private Job documentPhysicalDeleteJob;

  @Autowired private DocumentRepository documentRepository;

  @BeforeEach
  void cleanUp() {
    documentRepository.deleteAll();
  }

  @Test
  @DisplayName("項目48: MinIOからの削除が失敗したとき、DB削除も行われない")
  void shouldNotDeleteRecordWhenStorageDeleteFails() throws Exception {
    LocalDateTime now = LocalDateTime.now();
    Document document =
        documentRepository.save(
            Document.builder()
                .id(SnowflakeId.of(9_500_001L))
                .fileName("failure.txt")
                .contentType("text/plain")
                .fileSize(3L)
                .fileType(DocumentFileType.TEXT)
                .version(1)
                .s3Key("9500001/v1/failure.txt")
                .uploaderEmployeeId(SnowflakeId.of(10001L))
                .uploaderName("山田太郎")
                .createdAt(now)
                .updatedAt(now)
                .build());
    document.softDelete(now.minusYears(2));
    documentRepository.save(document);

    JobParameters jobParameters =
        new JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .addLong("nano", System.nanoTime())
            .toJobParameters();
    JobExecution execution = jobOperator.start(documentPhysicalDeleteJob, jobParameters);

    assertThat(execution.getStatus()).isEqualTo(BatchStatus.FAILED);
    assertThat(documentRepository.findById(document.getId())).isPresent();
    // 失敗原因がストレージ削除の例外であること（未実装スタブの失敗と区別するため）
    assertThat(hasStorageFailure(execution)).isTrue();
  }

  private boolean hasStorageFailure(JobExecution execution) {
    return execution.getStepExecutions().stream()
        .flatMap(step -> step.getFailureExceptions().stream())
        .anyMatch(
            ex -> {
              Throwable t = ex;
              while (t != null) {
                if (t instanceof IllegalStateException
                    && "ストレージ削除に失敗しました".equals(t.getMessage())) {
                  return true;
                }
                t = t.getCause();
              }
              return false;
            });
  }
}
