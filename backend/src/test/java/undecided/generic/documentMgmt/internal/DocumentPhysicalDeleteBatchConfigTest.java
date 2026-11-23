package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目44〜47: 物理削除バッチの統合テスト。
 *
 * <p>- 項目44: 論理削除から1年経過した帳票がバッチ対象として読み込まれる
 * <p>- 項目45: 論理削除から1年未満の帳票はバッチ対象とならない
 * <p>- 項目46: バッチ実行後、MinIO からオブジェクトが物理削除される
 * <p>- 項目47: バッチ実行後、DB からレコードが削除される
 */
@Tag("medium")
@Testcontainers
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, DocumentPhysicalDeleteBatchConfigTest.MinioTestConfig.class})
@DirtiesContext
@DisplayName("帳票物理削除バッチの統合テスト")
class DocumentPhysicalDeleteBatchConfigTest {

  private static final int MINIO_PORT = 9000;

  @Container
  private static final GenericContainer<?> MINIO =
      new GenericContainer<>("cgr.dev/chainguard/minio:latest")
          .withEnv("MINIO_ROOT_USER", DocumentStorageTestHelper.ACCESS_KEY)
          .withEnv("MINIO_ROOT_PASSWORD", DocumentStorageTestHelper.SECRET_KEY)
          .withCommand("server", "/data")
          .withExposedPorts(MINIO_PORT)
          .waitingFor(Wait.forHttp("/minio/health/live").forPort(MINIO_PORT));

  private static S3Client s3Client;

  @BeforeAll
  static void setUpMinio() {
    s3Client =
        DocumentStorageTestHelper.createS3Client(
            MINIO.getHost(), MINIO.getMappedPort(MINIO_PORT));
    s3Client.createBucket(b -> b.bucket(DocumentStorageTestHelper.BUCKET));
  }

  @AfterAll
  static void tearDownMinio() {
    if (s3Client != null) {
      s3Client.close();
    }
  }

  /** MinIO コンテナを Spring コンテキストに注入するテスト設定。 */
  @TestConfiguration
  static class MinioTestConfig {

    @Bean
    @Primary
    S3Client documentStorageS3Client() {
      return DocumentStorageTestHelper.createS3Client(
          MINIO.getHost(), MINIO.getMappedPort(MINIO_PORT));
    }

    @Bean
    @Primary
    DocumentStorageService documentStorageService(S3Client documentStorageS3Client) {
      return new DocumentStorageServiceImpl(
          documentStorageS3Client, DocumentStorageTestHelper.BUCKET);
    }
  }

  @Autowired private JobOperator jobOperator;

  @Autowired private Job documentPhysicalDeleteJob;

  @Autowired private DocumentRepository documentRepository;

  private static final byte[] CONTENT = "物理削除テスト用のファイル内容".getBytes(StandardCharsets.UTF_8);
  private static final AtomicLong ID_SEQUENCE = new AtomicLong(9_000_000L);

  @BeforeEach
  void cleanUp() {
    documentRepository.deleteAll();
    s3Client
        .listObjectsV2(b -> b.bucket(DocumentStorageTestHelper.BUCKET))
        .contents()
        .forEach(
            object ->
                s3Client.deleteObject(
                    b -> b.bucket(DocumentStorageTestHelper.BUCKET).key(object.key())));
  }

  private Document saveDocument(String fileName, LocalDateTime deletedAt) {
    long idValue = ID_SEQUENCE.incrementAndGet();
    String s3Key = idValue + "/v1/" + fileName;
    s3Client.putObject(
        b -> b.bucket(DocumentStorageTestHelper.BUCKET).key(s3Key), RequestBody.fromBytes(CONTENT));
    LocalDateTime now = LocalDateTime.now();
    Document saved =
        documentRepository.save(
            Document.builder()
                .id(SnowflakeId.of(idValue))
                .fileName(fileName)
                .contentType("text/plain")
                .fileSize(CONTENT.length)
                .fileType(DocumentFileType.TEXT)
                .version(1)
                .s3Key(s3Key)
                .uploaderEmployeeId(SnowflakeId.of(10001L))
                .uploaderName("山田太郎")
                .createdAt(now)
                .updatedAt(now)
                .build());
    if (deletedAt != null) {
      saved.softDelete(deletedAt);
      saved = documentRepository.save(saved);
    }
    return saved;
  }

  private JobExecution runJob() throws Exception {
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .addLong("nano", System.nanoTime())
            .toJobParameters();
    return jobOperator.start(documentPhysicalDeleteJob, jobParameters);
  }

  private boolean objectExists(String s3Key) {
    try {
      s3Client.headObject(b -> b.bucket(DocumentStorageTestHelper.BUCKET).key(s3Key));
      return true;
    } catch (NoSuchKeyException e) {
      return false;
    }
  }

  @Nested
  @DisplayName("物理削除バッチの実行テスト")
  class PhysicalDeleteJobTest {

    @Test
    @DisplayName("項目44・47: 論理削除から1年経過した帳票が読み込まれ、DBからレコードが削除される")
    void shouldReadAndDeleteExpiredDocument() throws Exception {
      Document expired = saveDocument("expired.txt", LocalDateTime.now().minusYears(2));

      JobExecution execution = runJob();

      assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(documentRepository.findById(expired.getId())).isEmpty();
    }

    @Test
    @DisplayName("項目45: 論理削除から1年未満の帳票はバッチ対象とならない")
    void shouldNotDeleteRecentDocument() throws Exception {
      Document expired = saveDocument("expired2.txt", LocalDateTime.now().minusYears(2));
      Document recent = saveDocument("recent.txt", LocalDateTime.now().minusMonths(6));

      JobExecution execution = runJob();

      assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(documentRepository.findById(expired.getId())).isEmpty();
      assertThat(documentRepository.findById(recent.getId())).isPresent();
      assertThat(objectExists(recent.getS3Key())).isTrue();
    }

    @Test
    @DisplayName("項目46: バッチ実行後、MinIOからオブジェクトが物理削除される")
    void shouldDeleteObjectFromMinio() throws Exception {
      Document expired = saveDocument("expired3.txt", LocalDateTime.now().minusYears(2));
      assertThat(objectExists(expired.getS3Key())).isTrue();

      JobExecution execution = runJob();

      assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
      assertThat(objectExists(expired.getS3Key())).isFalse();
    }
  }
}
