package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import undecided.generic.documentMgmt.spi.DocumentVersion;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * DocumentStorageServiceImpl の MinIO ラウンドトリップテスト。
 *
 * <p>MinIO コンテナに対して、アップロード → ダウンロード → 物理削除を一続きで確認する。保存先のキーが {@code
 * {documentId}/v{version}/{fileName}} であることも確認する。
 */
@Tag("medium")
@Testcontainers
@DisplayName("DocumentStorageServiceImplのMinIO統合テスト")
class DocumentStorageServiceImplMediumTest {

  private static final String ACCESS_KEY = "minioadmin";
  private static final String SECRET_KEY = "minioadmin";
  private static final String BUCKET = "modulith-documents-test";
  private static final int MINIO_PORT = 9000;

  @Container
  private static final GenericContainer<?> MINIO =
      new GenericContainer<>("cgr.dev/chainguard/minio:latest")
          .withEnv("MINIO_ROOT_USER", ACCESS_KEY)
          .withEnv("MINIO_ROOT_PASSWORD", SECRET_KEY)
          .withCommand("server", "/data")
          .withExposedPorts(MINIO_PORT)
          .waitingFor(Wait.forHttp("/minio/health/live").forPort(MINIO_PORT));

  private static S3Client s3Client;

  @BeforeAll
  static void setUp() {
    s3Client =
        S3Client.builder()
            .endpointOverride(
                URI.create(
                    "http://" + MINIO.getHost() + ":" + MINIO.getMappedPort(MINIO_PORT)))
            .region(Region.US_EAST_1)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
            .forcePathStyle(true)
            .build();
    s3Client.createBucket(b -> b.bucket(BUCKET));
  }

  @AfterAll
  static void tearDown() {
    if (s3Client != null) {
      s3Client.close();
    }
  }

  @Test
  @DisplayName("アップロードしたファイルをダウンロードでき、物理削除後は存在しないこと")
  void shouldUploadDownloadAndDelete() throws Exception {
    DocumentStorageServiceImpl sut = new DocumentStorageServiceImpl(s3Client, BUCKET);
    byte[] content = "帳票管理のラウンドトリップ".getBytes(StandardCharsets.UTF_8);

    String key =
        sut.upload(
            SnowflakeId.of(1234567890123L),
            new DocumentVersion(1),
            "sample.txt",
            "text/plain",
            new ByteArrayInputStream(content),
            content.length);

    assertThat(key).isEqualTo("1234567890123/v1/sample.txt");
    HeadObjectResponse head = s3Client.headObject(b -> b.bucket(BUCKET).key(key));
    assertThat(head.contentLength()).isEqualTo(content.length);
    assertThat(head.contentType()).isEqualTo("text/plain");

    try (InputStream downloaded = sut.download(key)) {
      assertThat(downloaded.readAllBytes()).isEqualTo(content);
    }

    sut.delete(key);

    assertThatThrownBy(() -> s3Client.headObject(b -> b.bucket(BUCKET).key(key)))
        .isInstanceOf(NoSuchKeyException.class);
  }
}
