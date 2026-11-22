package undecided.agiloom.requirementBacklog.internal;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Service
@Transactional
class SplitBacklogItemCommandService {
  SplitBacklogItemCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      SnowflakeIdGenerator snowflakeIdGenerator,
      ApplicationEventPublisher eventPublisher) {}

  void execute(SplitBacklogItemCommand command) {
    throw new UnsupportedOperationException("Not implemented");
  }
}