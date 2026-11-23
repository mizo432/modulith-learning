package undecided.agiloom.requirementBacklog.internal;

import undecided.agiloom.requirementBacklog.spi.BacklogItemDocument;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFacade;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("DetachDocumentCommandServiceのテスト")
class DetachDocumentCommandServiceTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(1000L);
  private static final SnowflakeId DOC_ID = SnowflakeId.of(6000L);
  private static final SnowflakeId SERIES_ID = SnowflakeId.of(5000L);
  private static final SnowflakeId LINK_ID = SnowflakeId.of(5001L);
  private static final SnowflakeId UPLOADER_ID = SnowflakeId.of(300L);
  private static final SnowflakeId OTHER_ID = SnowflakeId.of(301L);

  @Mock private BacklogItemDocumentRepository backlogItemDocumentRepository;
  @Mock private DocumentFacade documentFacade;

  private DetachDocumentCommandService service;

  @BeforeEach
  void setUp() {
    service = new DetachDocumentCommandService(backlogItemDocumentRepository, documentFacade);
  }

  private BacklogItemDocument linkage() {
    return BacklogItemDocument.builder()
        .id(LINK_ID)
        .backlogItemId(ITEM_ID)
        .documentId(DOC_ID)
        .seriesId(SERIES_ID)
        .build();
  }

  private Document document() {
    LocalDateTime now = LocalDateTime.now();
    return Document.builder()
        .id(DOC_ID)
        .fileName("design.pdf")
        .contentType("application/pdf")
        .fileSize(1024L)
        .fileType(DocumentFileType.PDF)
        .version(1)
        .s3Key(DOC_ID + "/v1/design.pdf")
        .uploaderEmployeeId(UPLOADER_ID)
        .deleted(false)
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  private DetachDocumentCommand command(SnowflakeId actorId) {
    return DetachDocumentCommand.builder()
        .backlogItemId(ITEM_ID)
        .documentId(DOC_ID)
        .actorEmployeeId(actorId)
        .build();
  }

  @Nested
  @DisplayName("異常系のテスト")
  class ErrorCases {

    @Test
    @DisplayName("関連付けが存在しないとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenLinkageNotFound() {
      when(backlogItemDocumentRepository.findByBacklogItemIdAndDocumentId(ITEM_ID, DOC_ID))
          .thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.execute(command(UPLOADER_ID)))
          .isInstanceOf(IllegalArgumentException.class);
      verify(documentFacade, never()).delete(any(), any());
    }

    @Test
    @DisplayName("アップロード者以外が削除したとき、権限例外がスローされ関連付けは残る")
    void shouldThrowWhenActorIsNotUploader() {
      BacklogItemDocument linkage = linkage();
      when(backlogItemDocumentRepository.findByBacklogItemIdAndDocumentId(ITEM_ID, DOC_ID))
          .thenReturn(Optional.of(linkage));
      when(documentFacade.findById(DOC_ID)).thenReturn(Optional.of(document()));

      assertThatThrownBy(() -> service.execute(command(OTHER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(documentFacade, never()).delete(any(), any());
      verify(backlogItemDocumentRepository, never()).delete(any());
    }
  }

  @Nested
  @DisplayName("正常系のテスト")
  class HappyCases {

    @Test
    @DisplayName("アップロード者が削除したとき、帳票が論理削除され関連付けが削除される")
    void shouldDeleteDocumentAndLinkage() {
      BacklogItemDocument linkage = linkage();
      when(backlogItemDocumentRepository.findByBacklogItemIdAndDocumentId(ITEM_ID, DOC_ID))
          .thenReturn(Optional.of(linkage));
      when(documentFacade.findById(DOC_ID)).thenReturn(Optional.of(document()));

      service.execute(command(UPLOADER_ID));

      verify(documentFacade).delete(DOC_ID, UPLOADER_ID);
      verify(backlogItemDocumentRepository).delete(linkage);
    }
  }
}
