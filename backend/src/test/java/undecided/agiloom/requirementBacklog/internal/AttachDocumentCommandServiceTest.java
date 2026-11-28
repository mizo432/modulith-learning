package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;

import undecided.agiloom.requirementBacklog.spi.BacklogItemDocument;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFacade;
import undecided.generic.documentMgmt.spi.DocumentFileType;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("AttachDocumentCommandServiceのテスト")
class AttachDocumentCommandServiceTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(1000L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);
  private static final SnowflakeId STAKEHOLDER_ID = SnowflakeId.of(900L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(999L);
  private static final SnowflakeId SERIES_ID = SnowflakeId.of(5000L);
  private static final SnowflakeId LINK_ID = SnowflakeId.of(5001L);
  private static final SnowflakeId DOC_ID = SnowflakeId.of(6000L);
  private static final SnowflakeId NEW_DOC_ID = SnowflakeId.of(6001L);

  @Mock private BacklogItemRepository backlogItemRepository;
  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  @Mock private BacklogItemDocumentRepository backlogItemDocumentRepository;
  @Mock private DocumentFacade documentFacade;
  @Mock private SnowflakeIdGenerator snowflakeIdGenerator;

  private AttachDocumentCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new AttachDocumentCommandService(
            backlogItemRepository,
            scrumTeamSubsetRepository,
            backlogItemDocumentRepository,
            documentFacade,
            snowflakeIdGenerator, Clock.systemDefaultZone());
  }

  private BacklogItem backlogItem() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private ScrumTeamSubset subset() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .poEmployeeId(PO_ID)
        .poName("PO")
        .smEmployeeId(SM_ID)
        .smName("SM")
        .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
        .stakeholderEmployeeIds(List.of(STAKEHOLDER_ID))
        .updatedAt(LocalDateTime.now()).build();
  }

  private AttachDocumentCommand command(SnowflakeId uploaderId, long fileSize, SnowflakeId seriesId) {
    return AttachDocumentCommand.builder()
        .backlogItemId(ITEM_ID)
        .fileName("design.pdf")
        .contentType("application/pdf")
        .fileSize(fileSize)
        .content(new ByteArrayInputStream(new byte[] {1, 2, 3}))
        .uploaderEmployeeId(uploaderId)
        .uploaderName("アップローダー")
        .seriesId(seriesId)
        .build();
  }

  private Document document(SnowflakeId id, long fileSize) {
    LocalDateTime now = LocalDateTime.now();
    return Document.builder()
        .id(id)
        .fileName("design.pdf")
        .contentType("application/pdf")
        .fileSize(fileSize)
        .fileType(DocumentFileType.PDF)
        .version(1)
        .s3Key(id + "/v1/design.pdf")
        .deleted(false)
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  @Nested
  @DisplayName("異常系のテスト")
  class ErrorCases {

    @Test
    @DisplayName("存在しないバックログアイテムに添付したとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenBacklogItemNotFound() {
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.execute(command(MEMBER_ID, 1024L, null)))
          .isInstanceOf(IllegalArgumentException.class);
      verify(documentFacade, never())
          .upload(anyString(), anyString(), anyLong(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("チームメンバーでない従業員が添付したとき、権限例外がスローされる")
    void shouldThrowWhenUploaderIsNotTeamMember() {
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(backlogItem()));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(subset()));

      assertThatThrownBy(() -> service.execute(command(OUTSIDER_ID, 1024L, null)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(documentFacade, never())
          .upload(anyString(), anyString(), anyLong(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("ステークホルダーが添付したとき、権限例外がスローされる（添付はチームメンバーのみ）")
    void shouldThrowWhenUploaderIsStakeholder() {
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(backlogItem()));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(subset()));

      assertThatThrownBy(() -> service.execute(command(STAKEHOLDER_ID, 1024L, null)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
    }

    @Test
    @DisplayName("合計サイズが50MBを超えるとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenTotalSizeExceedsLimit() {
      long existingSize = 45L * 1024 * 1024;
      BacklogItemDocument linkage =
          BacklogItemDocument.builder()
              .id(LINK_ID)
              .backlogItemId(ITEM_ID)
              .documentId(DOC_ID)
              .seriesId(SERIES_ID)
              .createdAt(LocalDateTime.now()).build();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(backlogItem()));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(subset()));
      when(backlogItemDocumentRepository.findByBacklogItemId(ITEM_ID))
          .thenReturn(List.of(linkage));
      when(documentFacade.findById(DOC_ID)).thenReturn(Optional.of(document(DOC_ID, existingSize)));

      assertThatThrownBy(() -> service.execute(command(MEMBER_ID, 6L * 1024 * 1024, null)))
          .isInstanceOf(IllegalArgumentException.class);
      verify(documentFacade, never())
          .upload(anyString(), anyString(), anyLong(), any(), any(), any(), any());
    }
  }

  @Nested
  @DisplayName("正常系のテスト")
  class HappyCases {

    @Test
    @DisplayName("チームメンバーが添付したとき、帳票が保存され関連付けが作成されて帳票IDが返る")
    void shouldAttachAndCreateLinkage() {
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(backlogItem()));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(subset()));
      when(backlogItemDocumentRepository.findByBacklogItemId(ITEM_ID)).thenReturn(List.of());
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(SERIES_ID, LINK_ID);
      when(documentFacade.upload(
              anyString(), anyString(), anyLong(), any(), eq(MEMBER_ID), anyString(), eq(SERIES_ID)))
          .thenReturn(DOC_ID);

      SnowflakeId result = service.execute(command(MEMBER_ID, 1024L, null));

      assertThat(result).isEqualTo(DOC_ID);
      ArgumentCaptor<BacklogItemDocument> captor =
          ArgumentCaptor.forClass(BacklogItemDocument.class);
      verify(backlogItemDocumentRepository).save(captor.capture());
      assertThat(captor.getValue().getBacklogItemId()).isEqualTo(ITEM_ID);
      assertThat(captor.getValue().getDocumentId()).isEqualTo(DOC_ID);
      assertThat(captor.getValue().getSeriesId()).isEqualTo(SERIES_ID);
    }

    @Test
    @DisplayName("系列を指定して再アップロードしたとき、既存の関連付けの帳票IDが新しい帳票に更新される")
    void shouldUpdateLinkageOnReupload() {
      BacklogItemDocument linkage =
          BacklogItemDocument.builder()
              .id(LINK_ID)
              .backlogItemId(ITEM_ID)
              .documentId(DOC_ID)
              .seriesId(SERIES_ID)
              .createdAt(LocalDateTime.now()).build();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(backlogItem()));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(subset()));
      when(backlogItemDocumentRepository.findByBacklogItemId(ITEM_ID))
          .thenReturn(List.of(linkage));
      when(backlogItemDocumentRepository.findByBacklogItemIdAndSeriesId(ITEM_ID, SERIES_ID))
          .thenReturn(Optional.of(linkage));
      when(documentFacade.findById(DOC_ID)).thenReturn(Optional.of(document(DOC_ID, 1024L)));
      when(documentFacade.upload(
              anyString(), anyString(), anyLong(), any(), eq(MEMBER_ID), anyString(), eq(SERIES_ID)))
          .thenReturn(NEW_DOC_ID);

      SnowflakeId result = service.execute(command(MEMBER_ID, 2048L, SERIES_ID));

      assertThat(result).isEqualTo(NEW_DOC_ID);
      assertThat(linkage.getDocumentId()).isEqualTo(NEW_DOC_ID);
      verify(backlogItemDocumentRepository).save(linkage);
    }
  }
}
