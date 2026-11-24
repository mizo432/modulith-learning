package undecided.agiloom.requirementBacklog.internal;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional(readOnly = true)
class BacklogItemQueryService {

  private final BacklogItemRepository backlogItemRepository;
  private final BacklogItemCommentRepository backlogItemCommentRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final SprintFacade sprintFacade;

  BacklogItemQueryService(
      BacklogItemRepository backlogItemRepository,
      BacklogItemCommentRepository backlogItemCommentRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      SprintFacade sprintFacade) {
    this.backlogItemRepository = backlogItemRepository;
    this.backlogItemCommentRepository = backlogItemCommentRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.sprintFacade = sprintFacade;
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
   * アイテムに割り当て可能なスプリント（未来4スプリント）の一覧を返します（Issue #525）。
   * 現在の割り当て先が窓の外にある場合は、そのスプリントも含めて返します。
   */
  List<AssignableSprintResponse> findAssignableSprints(
      @NonNull SnowflakeId backlogItemId, @NonNull SnowflakeId actorEmployeeId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
