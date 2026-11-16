package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.supporting.snowflake.SnowflakeId;

interface BacklogItemCommentRepository extends CrudRepository<BacklogItemComment, SnowflakeId> {

  List<BacklogItemComment> findByBacklogItemIdOrderByCreatedAtAsc(SnowflakeId backlogItemId);
}
