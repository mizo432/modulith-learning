package undecided.generic.documentMgmt.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.generic.documentMgmt.spi.DocumentVersion;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@link DocumentStorageService} の MinIO（S3互換）実装。
 *
 * <p>リトライは {@link S3Client} に設定したリトライ戦略が行う（{@link DocumentStorageConfig}）。リトライが尽きた場合は SDK
 * の例外をそのまま呼び出し元へ伝え、フォールバックしない。
 */
class DocumentStorageServiceImpl implements DocumentStorageService {

  private static final Logger log = LoggerFactory.getLogger(DocumentStorageServiceImpl.class);

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
    String key = documentId + "/v" + version.value() + "/" + fileName;
    PutObjectRequest request =
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .contentLength(contentLength)
            .build();
    try {
      // リトライ時に本文を再送できるよう、メモリ上のバイト列として渡す
      s3Client.putObject(request, RequestBody.fromBytes(readAll(content)));
    } catch (RuntimeException ex) {
      log.warn("ストレージへのアップロードに失敗しました (bucket={}, key={})", bucketName, key, ex);
      throw ex;
    }
    log.info(
        "ストレージへアップロードしました (bucket={}, key={}, size={})", bucketName, key, contentLength);
    return key;
  }

  @Override
  public InputStream download(String key) {
    try {
      InputStream content = s3Client.getObject(b -> b.bucket(bucketName).key(key));
      log.info("ストレージからダウンロードしました (bucket={}, key={})", bucketName, key);
      return content;
    } catch (RuntimeException ex) {
      log.warn("ストレージからのダウンロードに失敗しました (bucket={}, key={})", bucketName, key, ex);
      throw ex;
    }
  }

  @Override
  public void delete(String key) {
    try {
      s3Client.deleteObject(b -> b.bucket(bucketName).key(key));
      log.info("ストレージから削除しました (bucket={}, key={})", bucketName, key);
    } catch (RuntimeException ex) {
      log.warn("ストレージからの削除に失敗しました (bucket={}, key={})", bucketName, key, ex);
      throw ex;
    }
  }

  private static byte[] readAll(InputStream content) {
    try {
      return content.readAllBytes();
    } catch (IOException e) {
      throw new UncheckedIOException("アップロードするファイルの読み込みに失敗しました", e);
    }
  }
}
