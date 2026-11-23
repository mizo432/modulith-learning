package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    throw new UnsupportedOperationException("not implemented");
  }
}
