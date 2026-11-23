package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目41〜43: バージョン管理の統合テスト。
 *
 * <p>- 項目41: 同じファイル名で2回目のアップロードをしたとき、バージョン2として保存される
 * <p>- 項目42: バージョン一覧を取得したとき、すべてのバージョンが返される
 * <p>- 項目43: 特定のバージョンをダウンロードしたとき、そのバージョンのファイルが返される
 */
@Tag("medium")
@Testcontainers
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, DocumentVersionMediumTest.MinioTestConfig.class})
@Transactional
@DirtiesContext
@DisplayName("帳票バージョン管理の統合テスト")
class DocumentVersionMediumTest {

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

  @Autowired
  private UploadDocumentCommandService uploadDocumentCommandService;

  @Autowired
  private DocumentQueryService documentQueryService;

  @Autowired
  private DocumentDownloadService documentDownloadService;

  private static final String FILE_NAME = "version-test.pdf";
  private static final byte[] CONTENT_V1 = "バージョン1の内容".getBytes(StandardCharsets.UTF_8);
  private static final byte[] CONTENT_V2 = "バージョン2の内容（更新）".getBytes(StandardCharsets.UTF_8);

  @Nested
  @DisplayName("バージョン2への昇格")
  class VersionIncrementTest {

    @Test
    @DisplayName("項目41: 同じファイル名で2回目のアップロードをしたとき、バージョン2として保存される")
    void shouldSaveAsVersionTwoWhenSameFileNameUploadedTwice() {
      uploadDocumentCommandService.execute(
          UploadDocumentCommand.builder()
              .fileName(FILE_NAME)
              .contentType("application/pdf")
              .fileSize(CONTENT_V1.length)
              .content(new ByteArrayInputStream(CONTENT_V1))
              .build());

      SnowflakeId secondId =
          uploadDocumentCommandService.execute(
              UploadDocumentCommand.builder()
                  .fileName(FILE_NAME)
                  .contentType("application/pdf")
                  .fileSize(CONTENT_V2.length)
                  .content(new ByteArrayInputStream(CONTENT_V2))
                  .build());

      Document second = documentQueryService.findById(secondId).orElseThrow();
      assertThat(second.getVersion()).isEqualTo(2);
    }
  }

  @Nested
  @DisplayName("バージョン一覧取得")
  class VersionListTest {

    @Test
    @DisplayName("項目42: バージョン一覧を取得したとき、すべてのバージョンが返される")
    void shouldReturnAllVersionsWhenVersionListRequested() {
      uploadDocumentCommandService.execute(
          UploadDocumentCommand.builder()
              .fileName(FILE_NAME)
              .contentType("application/pdf")
              .fileSize(CONTENT_V1.length)
              .content(new ByteArrayInputStream(CONTENT_V1))
              .build());
      uploadDocumentCommandService.execute(
          UploadDocumentCommand.builder()
              .fileName(FILE_NAME)
              .contentType("application/pdf")
              .fileSize(CONTENT_V2.length)
              .content(new ByteArrayInputStream(CONTENT_V2))
              .build());

      List<Document> versions = documentQueryService.findVersionsByFileName(FILE_NAME);

      assertThat(versions).hasSize(2);
      assertThat(versions.stream().map(Document::getVersion)).containsExactlyInAnyOrder(1, 2);
    }
  }

  @Nested
  @DisplayName("特定バージョンのダウンロード")
  class VersionDownloadTest {

    @Test
    @DisplayName("項目43: 特定のバージョンをダウンロードしたとき、そのバージョンのファイルが返される")
    void shouldReturnSpecificVersionContentWhenVersionDownloaded() throws Exception {
      SnowflakeId firstId =
          uploadDocumentCommandService.execute(
              UploadDocumentCommand.builder()
                  .fileName(FILE_NAME)
                  .contentType("application/pdf")
                  .fileSize(CONTENT_V1.length)
                  .content(new ByteArrayInputStream(CONTENT_V1))
                  .build());
      uploadDocumentCommandService.execute(
          UploadDocumentCommand.builder()
              .fileName(FILE_NAME)
              .contentType("application/pdf")
              .fileSize(CONTENT_V2.length)
              .content(new ByteArrayInputStream(CONTENT_V2))
              .build());

      // バージョン1の帳票 ID で直接ダウンロード
      DocumentDownloadResult result = documentDownloadService.download(firstId);

      assertThat(result.content().readAllBytes()).isEqualTo(CONTENT_V1);
    }
  }
}
