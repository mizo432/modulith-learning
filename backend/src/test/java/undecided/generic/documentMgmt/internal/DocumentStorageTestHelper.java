package undecided.generic.documentMgmt.internal;

import java.net.URI;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/** 帳票ストレージ統合テスト用のヘルパーユーティリティ。 */
class DocumentStorageTestHelper {

  static final String ACCESS_KEY = "minioadmin";
  static final String SECRET_KEY = "minioadmin";
  static final String BUCKET = "modulith-documents-test";
  static final int MINIO_PORT = 9000;

  /**
   * テスト用 S3Client を生成する。
   *
   * @param host MinIO のホスト名
   * @param port MinIO のポート番号
   * @return S3Client
   */
  static S3Client createS3Client(String host, int port) {
    return S3Client.builder()
        .endpointOverride(URI.create("http://" + host + ":" + port))
        .region(Region.US_EAST_1)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
        .forcePathStyle(true)
        .build();
  }

  private DocumentStorageTestHelper() {}
}
