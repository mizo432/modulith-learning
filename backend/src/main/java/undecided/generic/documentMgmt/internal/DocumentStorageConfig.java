package undecided.generic.documentMgmt.internal;

import java.net.URI;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.retry.AwsRetryStrategy;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.retries.api.BackoffStrategy;
import software.amazon.awssdk.retries.api.RetryStrategy;
import software.amazon.awssdk.services.s3.S3Client;
import undecided.generic.documentMgmt.spi.DocumentStorageService;

/** 帳票ストレージ（MinIO / S3互換）の S3 クライアントとストレージ操作の設定。 */
@Configuration
@EnableConfigurationProperties(DocumentStorageProperties.class)
class DocumentStorageConfig {

  /** リトライ回数（初回の試行を含まない）。 */
  static final int MAX_RETRIES = 3;

  /** 指数バックオフの初回の待ち時間。 */
  static final Duration INITIAL_BACKOFF = Duration.ofSeconds(1);

  /** 各回の待ち時間の上限。 */
  static final Duration MAX_BACKOFF = Duration.ofSeconds(10);

  /** リトライを含めた1操作全体の上限。 */
  static final Duration API_CALL_TIMEOUT = Duration.ofSeconds(30);

  @Bean
  S3Client documentS3Client(DocumentStorageProperties properties) {
    return S3Client.builder()
        .endpointOverride(URI.create(properties.endpoint()))
        .region(Region.of(properties.region()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(properties.accessKey(), properties.secretKey())))
        .forcePathStyle(true)
        .overrideConfiguration(
            ClientOverrideConfiguration.builder()
                .retryStrategy(retryStrategy())
                .apiCallTimeout(API_CALL_TIMEOUT)
                .build())
        .build();
  }

  @Bean
  DocumentStorageService documentStorageServiceImpl(
      S3Client documentS3Client, DocumentStorageProperties properties) {
    return new DocumentStorageServiceImpl(documentS3Client, properties.bucket());
  }

  /**
   * ジッター付き指数バックオフのリトライ戦略。
   *
   * <p>SDK の maxAttempts は初回の試行を含むため、リトライ回数に 1 を足す。スロットリング時の待ちも同じ上限にそろえ、
   * リトライ回数がクォータで減らないよう circuit breaker を無効にする。
   */
  static RetryStrategy retryStrategy() {
    BackoffStrategy backoff = BackoffStrategy.exponentialDelay(INITIAL_BACKOFF, MAX_BACKOFF);
    return AwsRetryStrategy.standardRetryStrategy().toBuilder()
        .circuitBreakerEnabled(false)
        .maxAttempts(MAX_RETRIES + 1)
        .backoffStrategy(backoff)
        .throttlingBackoffStrategy(backoff)
        .build();
  }
}
