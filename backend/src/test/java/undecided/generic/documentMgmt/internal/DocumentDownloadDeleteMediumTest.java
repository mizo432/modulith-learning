package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
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
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目28〜30、32〜35: ダウンロードと論理削除の統合テスト。
 *
 * <p>ダウンロード:
 * <p>- 項目28: 有効な帳票IDでダウンロードしたとき、ファイルコンテンツが返される
 * <p>- 項目29: 存在しない帳票IDでダウンロードしたとき、エラーとなる
 * <p>- 項目30: 論理削除済みの帳票をダウンロードしたとき、エラーとなる
 *
 * <p>論理削除:
 * <p>- 項目32: アップロード者がファイルを削除したとき、論理削除フラグがセットされる
 * <p>- 項目33: アップロード者以外がファイルを削除したとき、エラーとなる
 * <p>- 項目34: 既に論理削除済みのファイルを再度削除したとき、エラーとなる
 * <p>- 項目35: 削除時に MinIO のオブジェクトは残ったままとなる（物理削除されない）
 */
@Tag("medium")
@Testcontainers
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, DocumentDownloadDeleteMediumTest.MinioTestConfig.class})
@Transactional
@DirtiesContext
@DisplayName("帳票ダウンロードと論理削除の統合テスト")
class DocumentDownloadDeleteMediumTest {

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
  private DeleteDocumentCommandService deleteDocumentCommandService;

  @Autowired
  private DocumentDownloadService documentDownloadService;

  @Autowired
  private DocumentRepository documentRepository;

  private static final byte[] CONTENT = "ダウンロードテスト用のファイル内容".getBytes(StandardCharsets.UTF_8);
  private static final SnowflakeId UPLOADER_ID = SnowflakeId.of(10001L);
  private static final SnowflakeId OTHER_EMPLOYEE_ID = SnowflakeId.of(99999L);

  private SnowflakeId uploadTestFile(String fileName) {
    return uploadDocumentCommandService.execute(
        UploadDocumentCommand.builder()
            .fileName(fileName)
            .contentType("application/pdf")
            .fileSize(CONTENT.length)
            .content(new ByteArrayInputStream(CONTENT))
            .uploaderEmployeeId(UPLOADER_ID)
            .uploaderName("テストユーザー")
            .build());
  }

  @Nested
  @DisplayName("ダウンロードテスト")
  class DownloadTest {

    @Test
    @DisplayName("項目28: 有効な帳票IDでダウンロードしたとき、ファイルコンテンツが返される")
    void shouldReturnFileContentWhenValidDocumentIdProvided() throws Exception {
      SnowflakeId id = uploadTestFile("download-test.pdf");

      DocumentDownloadResult result = documentDownloadService.download(id);

      assertThat(result).isNotNull();
      assertThat(result.content().readAllBytes()).isEqualTo(CONTENT);
      assertThat(result.contentType()).isEqualTo("application/pdf");
    }

    @Test
    @DisplayName("項目29: 存在しない帳票IDでダウンロードしたとき、エラーとなる")
    void shouldThrowExceptionWhenDocumentNotFound() {
      SnowflakeId nonExistentId = SnowflakeId.of(99999999999L);

      assertThatThrownBy(() -> documentDownloadService.download(nonExistentId))
          .isInstanceOf(DocumentNotFoundException.class);
    }

    @Test
    @DisplayName("項目30: 論理削除済みの帳票をダウンロードしたとき、エラーとなる")
    void shouldThrowExceptionWhenDocumentIsDeleted() {
      SnowflakeId id = uploadTestFile("deleted-download.pdf");
      deleteDocumentCommandService.execute(
          DeleteDocumentCommand.builder()
              .documentId(id)
              .actorEmployeeId(UPLOADER_ID)
              .build());

      assertThatThrownBy(() -> documentDownloadService.download(id))
          .isInstanceOf(DocumentNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("論理削除テスト")
  class SoftDeleteTest {

    @Test
    @DisplayName("項目32: アップロード者がファイルを削除したとき、論理削除フラグがセットされる")
    void shouldSetDeletedFlagWhenUploaderDeletesDocument() {
      SnowflakeId id = uploadTestFile("to-be-deleted.pdf");

      deleteDocumentCommandService.execute(
          DeleteDocumentCommand.builder()
              .documentId(id)
              .actorEmployeeId(UPLOADER_ID)
              .build());

      Document deleted = documentRepository.findById(id).orElseThrow();
      assertThat(deleted.isDeleted()).isTrue();
      assertThat(deleted.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("項目33: アップロード者以外がファイルを削除したとき、エラーとなる")
    void shouldThrowExceptionWhenNonUploaderDeletesDocument() {
      SnowflakeId id = uploadTestFile("non-uploader-delete.pdf");

      assertThatThrownBy(
              () ->
                  deleteDocumentCommandService.execute(
                      DeleteDocumentCommand.builder()
                          .documentId(id)
                          .actorEmployeeId(OTHER_EMPLOYEE_ID)
                          .build()))
          .isInstanceOf(DocumentPermissionDeniedException.class);
    }

    @Test
    @DisplayName("項目34: 既に論理削除済みのファイルを再度削除したとき、エラーとなる")
    void shouldThrowExceptionWhenAlreadyDeletedDocumentIsDeletedAgain() {
      SnowflakeId id = uploadTestFile("double-delete.pdf");
      deleteDocumentCommandService.execute(
          DeleteDocumentCommand.builder()
              .documentId(id)
              .actorEmployeeId(UPLOADER_ID)
              .build());

      assertThatThrownBy(
              () ->
                  deleteDocumentCommandService.execute(
                      DeleteDocumentCommand.builder()
                          .documentId(id)
                          .actorEmployeeId(UPLOADER_ID)
                          .build()))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("項目35: 削除時に MinIO のオブジェクトは残ったままとなる（物理削除されない）")
    void shouldKeepMinioObjectWhenDocumentSoftDeleted() {
      SnowflakeId id = uploadTestFile("keep-in-minio.pdf");
      Document uploaded = documentRepository.findById(id).orElseThrow();
      String s3Key = uploaded.getS3Key();

      deleteDocumentCommandService.execute(
          DeleteDocumentCommand.builder()
              .documentId(id)
              .actorEmployeeId(UPLOADER_ID)
              .build());

      // MinIO にオブジェクトが残っていることを確認
      HeadObjectResponse head =
          s3Client.headObject(b -> b.bucket(DocumentStorageTestHelper.BUCKET).key(s3Key));
      assertThat(head).isNotNull();
      assertThat(head.contentLength()).isPositive();
    }
  }
}
