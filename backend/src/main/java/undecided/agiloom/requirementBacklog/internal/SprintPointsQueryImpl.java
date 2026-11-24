package undecided.agiloom.requirementBacklog.internal;

import org.springframework.stereotype.Service;
import undecided.agiloom.requirementBacklog.spi.SprintPointsQuery;
import undecided.supporting.snowflake.SnowflakeId;

/** SprintPointsQuery の実装。 */
@Service
class SprintPointsQueryImpl implements SprintPointsQuery {

  private final BacklogItemRepository backlogItemRepository;

  SprintPointsQueryImpl(BacklogItemRepository backlogItemRepository) {
    this.backlogItemRepository = backlogItemRepository;
  }

  @Override
  public long sumAssignedStoryPoints(SnowflakeId sprintId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public long sumCompletedStoryPoints(SnowflakeId sprintId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
