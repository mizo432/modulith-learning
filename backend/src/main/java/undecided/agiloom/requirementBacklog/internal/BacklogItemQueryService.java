package undecided.agiloom.requirementBacklog.internal;

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
   *
   * @param scrumTeamId スクラムチームID
   * @param status 状態フィルタ（nullの場合は全状態）
   */
  List<BacklogItem> findByScrumTeamId(
      @NonNull SnowflakeId scrumTeamId, @Nullable BacklogItemStatus status) {
    if (status == null) {
      return backlogItemRepository.findByScrumTeamId(scrumTeamId);
    }
    return backlogItemRepository.findByScrumTeamIdAndStatus(scrumTeamId, status);
  }

  List<BacklogItemComment> findComments(@NonNull SnowflakeId backlogItemId) {
    return backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(backlogItemId);
  }
}
