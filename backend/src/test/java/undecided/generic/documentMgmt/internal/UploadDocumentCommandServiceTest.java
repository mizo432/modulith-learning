package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.generic.documentMgmt.spi.DocumentStorageService;
import undecided.generic.documentMgmt.spi.DocumentVersion;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * UploadDocumentCommandService の単体テスト。
 *
 * <p>Spring Boot に依存せず、Mockito でコラボレーターをスタブ化して振る舞いを検証する。
 */
@Tag("small")
@DisplayName("UploadDocumentCommandServiceの単体テスト")
class UploadDocumentCommandServiceTest {

  private final DocumentFileValidator documentFileValidator = new DocumentFileValidator();
  private final DocumentStorageService documentStorageService = mock(DocumentStorageService.class);
  private final DocumentRepository documentRepository = mock(DocumentRepository.class);
  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);

  private final UploadDocumentCommandService sut =
      new UploadDocumentCommandService(
          documentFileValidator, documentStorageService, documentRepository, snowflakeIdGenerator);

  private static final byte[] CONTENT = "テスト内容".getBytes(StandardCharsets.UTF_8);
  private static final SnowflakeId DOCUMENT_ID = SnowflakeId.of(100L);
  private static final String S3_KEY = "100/v1/test.pdf";

  @Nested
  @DisplayName("有効なファイルのアップロード")
  class ValidUploadTest {

    @Test
    @DisplayName("有効なファイルをアップロードしたとき、ストレージに保存してリポジトリに登録し ID を返す")
    void shouldSaveToStorageAndReturnIdWhenValidFile() {
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(DOCUMENT_ID);
      when(documentStorageService.upload(
              eq(DOCUMENT_ID), any(DocumentVersion.class), eq("test.pdf"),
              eq("application/pdf"), any(InputStream.class), anyLong()))
          .thenReturn(S3_KEY);
      when(documentRepository.findByFileName("test.pdf")).thenReturn(Collections.emptyList());
      when(documentRepository.save(any(Document.class)))
          .thenAnswer(inv -> inv.getArgument(0));

      SnowflakeId result =
          sut.execute(
              UploadDocumentCommand.builder()
                  .fileName("test.pdf")
                  .contentType("application/pdf")
                  .fileSize(CONTENT.length)
                  .content(new ByteArrayInputStream(CONTENT))
                  .build());

      assertThat(result).isEqualTo(DOCUMENT_ID);
      verify(documentStorageService)
          .upload(eq(DOCUMENT_ID), any(DocumentVersion.class), eq("test.pdf"),
              eq("application/pdf"), any(InputStream.class), anyLong());
      verify(documentRepository).save(any(Document.class));
    }

    @Test
    @DisplayName("同じファイル名のアップロードが既に存在するとき、バージョン 2 でストレージに保存する")
    void shouldUploadAsVersionTwoWhenSameFileNameExists() {
      SnowflakeId secondId = SnowflakeId.of(200L);
      Document existingDoc = mock(Document.class);
      when(existingDoc.getVersion()).thenReturn(1);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(secondId);
      when(documentStorageService.upload(
              eq(secondId), any(DocumentVersion.class), eq("dup.pdf"),
              eq("application/pdf"), any(InputStream.class), anyLong()))
          .thenReturn("200/v2/dup.pdf");
      when(documentRepository.findByFileName("dup.pdf")).thenReturn(List.of(existingDoc));
      when(documentRepository.save(any(Document.class)))
          .thenAnswer(inv -> inv.getArgument(0));

      sut.execute(
          UploadDocumentCommand.builder()
              .fileName("dup.pdf")
              .contentType("application/pdf")
              .fileSize(CONTENT.length)
              .content(new ByteArrayInputStream(CONTENT))
              .build());

      verify(documentStorageService)
          .upload(eq(secondId), eq(new DocumentVersion(2)), eq("dup.pdf"),
              eq("application/pdf"), any(InputStream.class), anyLong());
    }
  }

  @Nested
  @DisplayName("ファイル検証エラー")
  class ValidationErrorTest {

    @Test
    @DisplayName("ファイルサイズ制限を超えたとき、ストレージに保存せず InvalidDocumentFileException をスローする")
    void shouldThrowExceptionAndNotSaveWhenFileSizeExceedsLimit() {
      long oversized = DocumentFileValidator.MAX_FILE_SIZE_BYTES + 1;

      assertThatThrownBy(
              () ->
                  sut.execute(
                      UploadDocumentCommand.builder()
                          .fileName("big.pdf")
                          .contentType("application/pdf")
                          .fileSize(oversized)
                          .content(new ByteArrayInputStream(CONTENT))
                          .build()))
          .isInstanceOf(InvalidDocumentFileException.class);

      verify(documentStorageService, never()).upload(any(), any(), any(), any(), any(), anyLong());
      verify(documentRepository, never()).save(any());
    }

    @Test
    @DisplayName("許可されていない拡張子のとき、ストレージに保存せず InvalidDocumentFileException をスローする")
    void shouldThrowExceptionAndNotSaveWhenDisallowedExtension() {
      assertThatThrownBy(
              () ->
                  sut.execute(
                      UploadDocumentCommand.builder()
                          .fileName("bad.exe")
                          .contentType("application/x-msdownload")
                          .fileSize(CONTENT.length)
                          .content(new ByteArrayInputStream(CONTENT))
                          .build()))
          .isInstanceOf(InvalidDocumentFileException.class);

      verify(documentStorageService, never()).upload(any(), any(), any(), any(), any(), anyLong());
      verify(documentRepository, never()).save(any());
    }
  }
}
