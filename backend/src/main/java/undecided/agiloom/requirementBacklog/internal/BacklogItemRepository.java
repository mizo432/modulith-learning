package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

interface BacklogItemRepository
    extends CrudRepository<BacklogItem, SnowflakeId>, PagingAndSortingRepository<BacklogItem, SnowflakeId> {

  List<BacklogItem> findByScrumTeamId(SnowflakeId scrumTeamId);

  List<BacklogItem> findByScrumTeamIdAndStatus(SnowflakeId scrumTeamId, BacklogItemStatus status);

  List<BacklogItem> findByParentItemId(SnowflakeId parentItemId);
}
