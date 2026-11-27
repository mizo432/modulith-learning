package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import undecided.generic.documentMgmt.spi.DocumentVersion;
import undecided.supporting.snowflake.SnowflakeId;

/** テストリスト項目6〜8: ドキュメントストレージのログ出力テスト（#755） */
@Tag("small")
@DisplayName("DocumentStorageServiceImplの単体テスト")
class DocumentStorageServiceImplTest {

  private static final String BUCKET = "modulith-documents";

  private S3Client s3Client;
  private DocumentStorageServiceImpl service;
  private Logger logger;
  private ListAppender<ILoggingEvent> appender;

  @BeforeEach
  void setUp() {
    s3Client = mock(S3Client.class);
    service = new DocumentStorageServiceImpl(s3Client, BUCKET);
    logger = (Logger) LoggerFactory.getLogger(DocumentStorageServiceImpl.class);
    appender = new ListAppender<>();
    appender.start();
    logger.addAppender(appender);
    logger.setLevel(Level.INFO);
  }

  @AfterEach
  void tearDown() {
    logger.detachAppender(appender);
    logger.setLevel(null);
  }

  @Test
  @DisplayName("S3へアップロードしたとき、バケット・キー・サイズを含むINFOログが記録される")
  void shouldLogInfoOnUpload() {
    byte[] content = "hello".getBytes(StandardCharsets.UTF_8);

    String key =
        service.upload(
            SnowflakeId.of(123L),
            DocumentVersion.initial(),
            "report.pdf",
            "application/pdf",
            new ByteArrayInputStream(content),
            content.length);

    assertThat(appender.list)
        .anySatisfy(
            event -> {
              assertThat(event.getLevel()).isEqualTo(Level.INFO);
              assertThat(event.getFormattedMessage())
                  .contains(BUCKET)
                  .contains(key)
                  .contains(String.valueOf(content.length));
            });
  }

  @Test
  @DisplayName("S3からダウンロード・削除したとき、バケットとキーを含むINFOログが記録される")
  void shouldLogInfoOnDownloadAndDelete() {
    when(s3Client.getObject(ArgumentMatchers.<Consumer<GetObjectRequest.Builder>>any()))
        .thenAnswer(
            invocation ->
                new ResponseInputStream<>(
                    GetObjectResponse.builder().build(),
                    AbortableInputStream.create(new ByteArrayInputStream(new byte[0]))));

    service.download("123/v1/report.pdf");
    service.delete("123/v1/report.pdf");

    assertThat(appender.list)
        .filteredOn(event -> event.getLevel() == Level.INFO)
        .hasSize(2)
        .allSatisfy(
            event ->
                assertThat(event.getFormattedMessage())
                    .contains(BUCKET)
                    .contains("123/v1/report.pdf"));
  }

  @Test
  @DisplayName("S3操作が失敗したとき、WARNログが記録され、例外はそのまま伝播する")
  void shouldLogWarnAndRethrowOnFailure() {
    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenThrow(new RuntimeException("S3 unavailable"));
    byte[] content = "hello".getBytes(StandardCharsets.UTF_8);

    assertThatThrownBy(
            () ->
                service.upload(
                    SnowflakeId.of(123L),
                    DocumentVersion.initial(),
                    "report.pdf",
                    "application/pdf",
                    new ByteArrayInputStream(content),
                    content.length))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("S3 unavailable");
    assertThat(appender.list)
        .anySatisfy(event -> assertThat(event.getLevel()).isEqualTo(Level.WARN));
  }
}
