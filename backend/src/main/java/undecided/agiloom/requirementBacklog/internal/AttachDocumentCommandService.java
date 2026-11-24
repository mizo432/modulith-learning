package undecided.agiloom.requirementBacklog.internal;

import undecided.agiloom.requirementBacklog.spi.BacklogItemDocument;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentFacade;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * バックログアイテムへファイルを添付するコマンドサービス。
 *
 * <p>帳票管理モジュールのファサード経由でファイルを保存し、自領域に関連付けを作成します。
 * アップロードできるのはチームメンバーのみで、アイテムあたりの合計サイズ上限（50MB）を守ります。
 */
@Service
@Transactional
class AttachDocumentCommandService {

  /** バックログアイテムあたりの添付ファイル合計サイズ上限（バイト）。 */
  static final long MAX_TOTAL_SIZE_PER_ITEM = 50L * 1024 * 1024;

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final BacklogItemDocumentRepository backlogItemDocumentRepository;
  private final DocumentFacade documentFacade;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  AttachDocumentCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      BacklogItemDocumentRepository backlogItemDocumentRepository,
      DocumentFacade documentFacade,
      SnowflakeIdGenerator snowflakeIdGenerator) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.backlogItemDocumentRepository = backlogItemDocumentRepository;
    this.documentFacade = documentFacade;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  /**
   * ファイルを添付します。
   *
   * @param command 添付コマンド
   * @return 保存された帳票の ID
   */
  @NonNull
  SnowflakeId execute(@NonNull AttachDocumentCommand command) {
    BacklogItem backlogItem =
        backlogItemRepository
            .findById(command.getBacklogItemId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "BacklogItem not found: " + command.getBacklogItemId()));
    ScrumTeamSubset scrumTeamSubset =
        scrumTeamSubsetRepository
            .findById(backlogItem.getScrumTeamId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ScrumTeam not found: " + backlogItem.getScrumTeamId()));
    if (!scrumTeamSubset.isTeamMember(command.getUploaderEmployeeId())) {
      throw new BacklogItemPermissionDeniedException("ファイルを添付できるのはチームメンバーのみです");
    }

    // 合計サイズ判定（再アップロード時は当該系列の旧サイズを新しいサイズで置き換える）
    List<BacklogItemDocument> linkages =
        backlogItemDocumentRepository.findByBacklogItemId(command.getBacklogItemId());
    long totalSize = 0L;
    long replacedSize = 0L;
    for (BacklogItemDocument linkage : linkages) {
      long size = documentFacade.findById(linkage.getDocumentId()).map(Document::getFileSize).orElse(0L);
      totalSize += size;
      if (command.getSeriesId() != null && linkage.getSeriesId().equals(command.getSeriesId())) {
        replacedSize = size;
      }
    }
    if (totalSize - replacedSize + command.getFileSize() > MAX_TOTAL_SIZE_PER_ITEM) {
      throw new IllegalArgumentException("添付ファイルの合計サイズが上限（50MB）を超えています");
    }

    SnowflakeId seriesId =
        command.getSeriesId() != null ? command.getSeriesId() : snowflakeIdGenerator.nextIdValue();
    SnowflakeId documentId =
        documentFacade.upload(
            command.getFileName(),
            command.getContentType(),
            command.getFileSize(),
            command.getContent(),
            command.getUploaderEmployeeId(),
            command.getUploaderName(),
            seriesId);

    if (command.getSeriesId() != null) {
      BacklogItemDocument linkage =
          backlogItemDocumentRepository
              .findByBacklogItemIdAndSeriesId(command.getBacklogItemId(), seriesId)
              .orElseThrow(
                  () -> new IllegalArgumentException("添付が存在しません: seriesId=" + seriesId));
      linkage.setDocumentId(documentId);
      backlogItemDocumentRepository.save(linkage);
    } else {
      backlogItemDocumentRepository.save(
          BacklogItemDocument.builder()
              .id(snowflakeIdGenerator.nextIdValue())
              .backlogItemId(command.getBacklogItemId())
              .documentId(documentId)
              .seriesId(seriesId)
              .build());
    }
    return documentId;
  }
}
