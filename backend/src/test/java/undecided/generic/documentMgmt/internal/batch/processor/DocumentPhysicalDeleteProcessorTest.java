package undecided.generic.documentMgmt.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.supporting.snowflake.SnowflakeId;

/** 段階4 small: 物理削除プロセッサーの単体テスト。 */
@DisplayName("DocumentPhysicalDeleteProcessorの単体テスト")
class DocumentPhysicalDeleteProcessorTest {

  private final DocumentStorageService documentStorageService = mock(DocumentStorageService.class);

  private final DocumentPhysicalDeleteProcessor processor =
      new DocumentPhysicalDeleteProcessor(documentStorageService);

  private Document document() {
    LocalDateTime now = LocalDateTime.now();
    return Document.builder()
        .id(SnowflakeId.of(7001L))
        .fileName("report.pdf")
        .contentType("application/pdf")
        .fileSize(3L)
        .fileType(DocumentFileType.PDF)
        .version(1)
        .s3Key("7001/v1/report.pdf")
        .uploaderEmployeeId(SnowflakeId.of(10001L))
        .uploaderName("山田太郎")
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  @Test
  @DisplayName("帳票を処理したとき、ストレージからその帳票のS3キーで物理削除される")
  void shouldDeleteObjectFromStorage() {
    processor.process(document());

    verify(documentStorageService).delete("7001/v1/report.pdf");
  }

  @Test
  @DisplayName("帳票を処理したとき、同じ帳票が返される")
  void shouldReturnSameDocument() {
    Document document = document();

    assertThat(processor.process(document)).isSameAs(document);
  }

  @Test
  @DisplayName("ストレージの物理削除が失敗したとき、例外が伝播する")
  void shouldPropagateStorageFailure() {
    doThrow(new IllegalStateException("storage error"))
        .when(documentStorageService)
        .delete(Mockito.anyString());

    assertThatThrownBy(() -> processor.process(document()))
        .isInstanceOf(IllegalStateException.class);
  }
}
