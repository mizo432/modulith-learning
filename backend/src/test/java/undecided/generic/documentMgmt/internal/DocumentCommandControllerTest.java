package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

/**
 * DocumentCommandController の単体テスト。
 *
 * <p>Spring コンテキストに依存せず、コラボレーターをモック化してコントローラーの振る舞いを検証する。
 */
@Tag("small")
@DisplayName("DocumentCommandControllerの単体テスト")
class DocumentCommandControllerTest {

  private final UploadDocumentCommandService uploadDocumentCommandService =
      mock(UploadDocumentCommandService.class);
  private final DeleteDocumentCommandService deleteDocumentCommandService =
      mock(DeleteDocumentCommandService.class);
  private final DocumentQueryService documentQueryService = mock(DocumentQueryService.class);

  private final DocumentCommandController sut =
      new DocumentCommandController(
          uploadDocumentCommandService, deleteDocumentCommandService, documentQueryService);

  private static final SnowflakeId DOCUMENT_ID = SnowflakeId.of(1L);
  private static final SnowflakeId UPLOADER_ID = SnowflakeId.of(10L);

  private Document buildDocument() {
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
        .deleted(false)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("アップロードエンドポイント")
  class UploadEndpointTest {

    @Test
    @DisplayName("有効なファイルをアップロードしたとき、コマンドサービスを呼び出してレスポンスを返す")
    void shouldCallCommandServiceAndReturnResponseWhenValidFileUploaded() {
      when(uploadDocumentCommandService.execute(any(UploadDocumentCommand.class)))
          .thenReturn(DOCUMENT_ID);
      when(documentQueryService.findById(DOCUMENT_ID))
          .thenReturn(Optional.of(buildDocument()));

      MockMultipartFile file =
          new MockMultipartFile(
              "file",
              "test.pdf",
              "application/pdf",
              "テスト内容".getBytes(StandardCharsets.UTF_8));

      sut.uploadDocument(file, String.valueOf(UPLOADER_ID.value()), "テストユーザー");

      verify(uploadDocumentCommandService).execute(any(UploadDocumentCommand.class));
    }

    @Test
    @DisplayName("ファイル検証エラーのとき、InvalidDocumentFileException がスローされる")
    void shouldThrowExceptionWhenFileValidationFails() {
      when(uploadDocumentCommandService.execute(any(UploadDocumentCommand.class)))
          .thenThrow(new InvalidDocumentFileException("ファイル検証エラー"));

      MockMultipartFile file =
          new MockMultipartFile(
              "file",
              "bad.exe",
              "application/x-msdownload",
              "悪意のある内容".getBytes(StandardCharsets.UTF_8));

      assertThatThrownBy(
              () -> sut.uploadDocument(file, String.valueOf(UPLOADER_ID.value()), "テストユーザー"))
          .isInstanceOf(InvalidDocumentFileException.class);
    }
  }

  @Nested
  @DisplayName("削除エンドポイント")
  class DeleteEndpointTest {

    @Test
    @DisplayName("有効な帳票IDで削除したとき、コマンドサービスを呼び出す")
    void shouldCallCommandServiceWhenValidDocumentIdProvided() {
      sut.deleteDocument(
          String.valueOf(DOCUMENT_ID.value()), String.valueOf(UPLOADER_ID.value()));

      verify(deleteDocumentCommandService).execute(any(DeleteDocumentCommand.class));
    }

    @Test
    @DisplayName("権限のない従業員が削除しようとしたとき、DocumentPermissionDeniedException がスローされる")
    void shouldThrowExceptionWhenUnauthorizedEmployeeDeletesDocument() {
      SnowflakeId otherId = SnowflakeId.of(999L);
      // .execute() がスローした場合にコントローラーが Exception を伝播させることを確認
      doThrow(new DocumentPermissionDeniedException("権限がありません"))
          .when(deleteDocumentCommandService).execute(any(DeleteDocumentCommand.class));

      assertThatThrownBy(
              () ->
                  sut.deleteDocument(
                      String.valueOf(DOCUMENT_ID.value()), String.valueOf(otherId.value())))
          .isInstanceOf(DocumentPermissionDeniedException.class);
    }
  }
}
