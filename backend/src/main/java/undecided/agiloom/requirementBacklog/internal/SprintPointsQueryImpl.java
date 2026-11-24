package undecided.agiloom.requirementBacklog.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.SprintPointsQuery;
import undecided.supporting.snowflake.SnowflakeId;

/** SprintPointsQuery の実装。 */
@Service
@Transactional(readOnly = true)
class SprintPointsQueryImpl implements SprintPointsQuery {

  private final BacklogItemRepository backlogItemRepository;

  SprintPointsQueryImpl(BacklogItemRepository backlogItemRepository) {
    this.backlogItemRepository = backlogItemRepository;
  }

  @Override
  public long sumAssignedStoryPoints(SnowflakeId sprintId) {
    return backlogItemRepository.findBySprintId(sprintId).stream()
        .mapToLong(SprintPointsQueryImpl::storyPointValue)
        .sum();
  }

  @Override
  public long sumCompletedStoryPoints(SnowflakeId sprintId) {
    return backlogItemRepository.findBySprintId(sprintId).stream()
        .filter(item -> item.getStatus() == BacklogItemStatus.DONE)
        .mapToLong(SprintPointsQueryImpl::storyPointValue)
        .sum();
  }

  private static long storyPointValue(BacklogItem item) {
    Integer storyPoint = item.getStoryPoint();
    return storyPoint == null ? 0L : storyPoint.longValue();
  }
}
