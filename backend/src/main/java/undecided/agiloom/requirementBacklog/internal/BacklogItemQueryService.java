package undecided.agiloom.requirementBacklog.internal;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional(readOnly = true)
class BacklogItemQueryService {

  private final BacklogItemRepository backlogItemRepository;
  private final BacklogItemCommentRepository backlogItemCommentRepository;

  BacklogItemQueryService(
      BacklogItemRepository backlogItemRepository,
      BacklogItemCommentRepository backlogItemCommentRepository) {
    this.backlogItemRepository = backlogItemRepository;
    this.backlogItemCommentRepository = backlogItemCommentRepository;
  }

  Optional<BacklogItem> findById(@NonNull SnowflakeId id) {
    return backlogItemRepository.findById(id);
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
    return backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(backlogItemId);
  }
}
