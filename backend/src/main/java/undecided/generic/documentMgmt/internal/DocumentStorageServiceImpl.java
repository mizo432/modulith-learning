package undecided.generic.documentMgmt.internal;

import java.io.InputStream;
import software.amazon.awssdk.services.s3.S3Client;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.generic.documentMgmt.spi.DocumentVersion;
import undecided.supporting.snowflake.SnowflakeId;

/** {@link DocumentStorageService} の MinIO（S3互換）実装。 */
class DocumentStorageServiceImpl implements DocumentStorageService {

  private final S3Client s3Client;
  private final String bucketName;

  DocumentStorageServiceImpl(S3Client s3Client, String bucketName) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }

  @Override
  public String upload(
      SnowflakeId documentId,
      DocumentVersion version,
      String fileName,
      String contentType,
      InputStream content,
      long contentLength) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public InputStream download(String key) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void delete(String key) {
    throw new UnsupportedOperationException("not implemented");
  }
}
