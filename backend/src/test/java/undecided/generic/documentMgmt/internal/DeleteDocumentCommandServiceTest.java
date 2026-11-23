package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * DeleteDocumentCommandService の単体テスト。
 *
 * <p>Spring Boot に依存せず、Mockito でコラボレーターをスタブ化して振る舞いを検証する。
 */
@Tag("small")
@DisplayName("DeleteDocumentCommandServiceの単体テスト")
class DeleteDocumentCommandServiceTest {

  private final DocumentRepository documentRepository = mock(DocumentRepository.class);

  private final DeleteDocumentCommandService sut =
      new DeleteDocumentCommandService(documentRepository);

  private static final SnowflakeId DOCUMENT_ID = SnowflakeId.of(1L);
  private static final SnowflakeId UPLOADER_ID = SnowflakeId.of(10L);
  private static final SnowflakeId OTHER_EMPLOYEE_ID = SnowflakeId.of(99L);

  private Document buildDocument(boolean deleted) {
    return Document.builder()
        .id(DOCUMENT_ID)
        .fileName("test.pdf")
        .contentType("application/pdf")
        .fileSize(1024L)
        .fileType(DocumentFileType.PDF)
        .version(1)
        .s3Key("1/v1/test.pdf")
        .uploaderEmployeeId(UPLOADER_ID)
        .uploaderName("テストユーザー")
        .deleted(deleted)
        .deletedAt(deleted ? LocalDateTime.now() : null)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("正常な論理削除")
  class ValidDeleteTest {

    @Test
    @DisplayName("アップロード者が削除したとき、論理削除フラグがセットされる")
    void shouldSetDeletedFlagWhenUploaderDeletesDocument() {
      Document document = buildDocument(false);
      when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.of(document));

      sut.execute(
          DeleteDocumentCommand.builder()
              .documentId(DOCUMENT_ID)
              .actorEmployeeId(UPLOADER_ID)
              .build());

      assertThat(document.isDeleted()).isTrue();
      assertThat(document.getDeletedAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("エラーケース")
  class ErrorTest {

    @Test
    @DisplayName("帳票が存在しないとき、IllegalArgumentException をスローする")
    void shouldThrowExceptionWhenDocumentNotFound() {
      when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.empty());

      assertThatThrownBy(
              () ->
                  sut.execute(
                      DeleteDocumentCommand.builder()
                          .documentId(DOCUMENT_ID)
                          .actorEmployeeId(UPLOADER_ID)
                          .build()))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("アップロード者以外が削除しようとしたとき、DocumentPermissionDeniedException をスローする")
    void shouldThrowExceptionWhenNonUploaderDeletesDocument() {
      Document document = buildDocument(false);
      when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.of(document));

      assertThatThrownBy(
              () ->
                  sut.execute(
                      DeleteDocumentCommand.builder()
                          .documentId(DOCUMENT_ID)
                          .actorEmployeeId(OTHER_EMPLOYEE_ID)
                          .build()))
          .isInstanceOf(DocumentPermissionDeniedException.class);
    }

    @Test
    @DisplayName("既に論理削除済みの帳票を削除しようとしたとき、IllegalStateException をスローする")
    void shouldThrowExceptionWhenAlreadyDeletedDocumentIsDeleted() {
      Document document = buildDocument(true);
      when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.of(document));

      assertThatThrownBy(
              () ->
                  sut.execute(
                      DeleteDocumentCommand.builder()
                          .documentId(DOCUMENT_ID)
                          .actorEmployeeId(UPLOADER_ID)
                          .build()))
          .isInstanceOf(IllegalStateException.class);
    }
  }
}
