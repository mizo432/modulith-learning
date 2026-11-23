package undecided.agiloom.requirementBacklog.internal;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDocument;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentContent;
import undecided.generic.documentMgmt.spi.DocumentFacade;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional(readOnly = true)
class BacklogItemQueryService {

  private final BacklogItemRepository backlogItemRepository;
  private final BacklogItemCommentRepository backlogItemCommentRepository;
  private final BacklogItemDocumentRepository backlogItemDocumentRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final DocumentFacade documentFacade;

  BacklogItemQueryService(
      BacklogItemRepository backlogItemRepository,
      BacklogItemCommentRepository backlogItemCommentRepository,
      BacklogItemDocumentRepository backlogItemDocumentRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      DocumentFacade documentFacade) {
    this.backlogItemRepository = backlogItemRepository;
    this.backlogItemCommentRepository = backlogItemCommentRepository;
    this.backlogItemDocumentRepository = backlogItemDocumentRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.documentFacade = documentFacade;
  }

  Optional<BacklogItem> findById(@NonNull SnowflakeId id) {
    return backlogItemRepository.findById(id);
  }

  /**
   * 指定した親アイテムから分割された子アイテムを取得します。
   *
   * @param parentItemId 親アイテムID
   */
  List<BacklogItem> findByParentItemId(@NonNull SnowflakeId parentItemId) {
    return backlogItemRepository.findByParentItemId(parentItemId);
  }

  /**
   * チームのバックログアイテムを取得します。
   * Value降順（nullは末尾）、同値ならcreatedAt昇順、さらに同値ならid昇順でソートされます。
   *
   * @param scrumTeamId スクラムチームID
   * @param status 状態フィルタ（nullの場合は全状態）
   */
  List<BacklogItem> findByScrumTeamId(
      @NonNull SnowflakeId scrumTeamId, @Nullable BacklogItemStatus status) {
    List<BacklogItem> items;
    if (status == null) {
      items = backlogItemRepository.findByScrumTeamId(scrumTeamId);
    } else {
      items = backlogItemRepository.findByScrumTeamIdAndStatus(scrumTeamId, status);
    }
    items.sort(backlogItemComparator());
    return items;
  }

  /**
   * Value降順（nullは末尾）、同値ならcreatedAt昇順、さらに同値ならid昇順の比較子を返します。
   */
  private Comparator<BacklogItem> backlogItemComparator() {
    return Comparator.<BacklogItem, Integer>comparing(
            BacklogItem::getValue, Comparator.nullsLast(Comparator.reverseOrder()))
        .thenComparing(BacklogItem::getCreatedAt, Comparator.naturalOrder())
        .thenComparing(item -> item.getId().value(), Comparator.naturalOrder());
  }

  List<BacklogItemComment> findComments(@NonNull SnowflakeId backlogItemId) {
    List<BacklogItemComment> comments =
        backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(backlogItemId);
    // 子返信を持つ親コメントのID集合を計算する
    Set<SnowflakeId> parentsWithReplies = new HashSet<>();
    for (BacklogItemComment comment : comments) {
      if (comment.getParentComment() != null) {
        parentsWithReplies.add(comment.getParentComment().getId());
      }
    }
    // 各コメントに子返信有無を反映する
    for (BacklogItemComment comment : comments) {
      comment.setHasReplies(parentsWithReplies.contains(comment.getId()));
    }
    return comments;
  }

  /**
   * バックログアイテムの添付ファイル一覧を取得します（詳細表示用、権限チェックなし）。
   *
   * @param backlogItemId バックログアイテム ID
   * @return 添付ファイル一覧
   */
  List<BacklogItemAttachmentResponse> findAttachments(@NonNull SnowflakeId backlogItemId) {
    return backlogItemDocumentRepository.findByBacklogItemId(backlogItemId).stream()
        .map(
            linkage ->
                documentFacade
                    .findById(linkage.getDocumentId())
                    .map(
                        document ->
                            BacklogItemAttachmentResponse.from(document, linkage.getSeriesId()))
                    .orElse(null))
        .filter(Objects::nonNull)
        .toList();
  }

  /**
   * バックログアイテムの添付ファイル一覧を取得します。
   *
   * <p>閲覧できるのはチームメンバーとステークホルダーのみです。
   *
   * @param backlogItemId バックログアイテム ID
   * @param actorEmployeeId 操作者の従業員 ID
   * @return 添付ファイル一覧
   * @throws BacklogItemPermissionDeniedException メンバーでもステークホルダーでもない場合
   */
  List<BacklogItemAttachmentResponse> findAttachments(
      @NonNull SnowflakeId backlogItemId, @Nullable SnowflakeId actorEmployeeId) {
    BacklogItem backlogItem =
        backlogItemRepository
            .findById(backlogItemId)
            .orElseThrow(
                () -> new IllegalArgumentException("BacklogItem not found: " + backlogItemId));
    ScrumTeamSubset scrumTeamSubset =
        scrumTeamSubsetRepository
            .findById(backlogItem.getScrumTeamId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ScrumTeam not found: " + backlogItem.getScrumTeamId()));
    if (!scrumTeamSubset.isTeamMember(actorEmployeeId)
        && !scrumTeamSubset.isStakeholder(actorEmployeeId)) {
      throw new BacklogItemPermissionDeniedException(
          "添付ファイルを閲覧できるのはチームメンバーとステークホルダーのみです");
    }
    return findAttachments(backlogItemId);
  }

  /**
   * 添付ファイルをダウンロードします。
   *
   * <p>ダウンロードできるのはチームメンバーとステークホルダーのみです。
   *
   * @param backlogItemId バックログアイテム ID
   * @param documentId 帳票 ID
   * @param actorEmployeeId 操作者の従業員 ID
   * @return ダウンロード内容
   * @throws BacklogItemPermissionDeniedException メンバーでもステークホルダーでもない場合
   */
  DocumentContent downloadAttachment(
      @NonNull SnowflakeId backlogItemId,
      @NonNull SnowflakeId documentId,
      @Nullable SnowflakeId actorEmployeeId) {
    findAttachments(backlogItemId, actorEmployeeId);
    resolveLinkage(backlogItemId, documentId);
    return documentFacade.download(documentId);
  }

  /**
   * 添付ファイルのバージョン一覧を取得します（バージョン昇順）。
   *
   * <p>閲覧できるのはチームメンバーとステークホルダーのみです。指定した帳票が属する系列の
   * 全バージョンを返します。
   *
   * @param backlogItemId バックログアイテム ID
   * @param documentId 系列に属する帳票 ID（どのバージョンでもよい）
   * @param actorEmployeeId 操作者の従業員 ID
   * @return バージョン一覧
   * @throws BacklogItemPermissionDeniedException メンバーでもステークホルダーでもない場合
   */
  List<BacklogItemAttachmentResponse> findAttachmentVersions(
      @NonNull SnowflakeId backlogItemId,
      @NonNull SnowflakeId documentId,
      @Nullable SnowflakeId actorEmployeeId) {
    findAttachments(backlogItemId, actorEmployeeId);
    BacklogItemDocument linkage = resolveLinkage(backlogItemId, documentId);
    return documentFacade.findBySeriesId(linkage.getSeriesId()).stream()
        .sorted(Comparator.comparingInt(Document::getVersion))
        .map(document -> BacklogItemAttachmentResponse.from(document, linkage.getSeriesId()))
        .toList();
  }

  /**
   * 帳票 ID から添付リンケージを解決します。
   *
   * <p>リンケージは最新バージョンの帳票 ID を保持するため、旧バージョンの ID は帳票の系列 ID
   * 経由で解決します。
   */
  private BacklogItemDocument resolveLinkage(SnowflakeId backlogItemId, SnowflakeId documentId) {
    Optional<BacklogItemDocument> byDocument =
        backlogItemDocumentRepository.findByBacklogItemIdAndDocumentId(backlogItemId, documentId);
    if (byDocument.isPresent()) {
      return byDocument.get();
    }
    return documentFacade
        .findById(documentId)
        .map(Document::getSeriesId)
        .filter(Objects::nonNull)
        .flatMap(
            seriesId ->
                backlogItemDocumentRepository.findByBacklogItemIdAndSeriesId(
                    backlogItemId, seriesId))
        .orElseThrow(() -> new IllegalArgumentException("添付が存在しません: " + documentId));
  }
}
