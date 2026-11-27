package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;

import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSplit;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatusChanged;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Service
@Transactional
class SplitBacklogItemCommandService {
  private static final int MIN_SPLIT_STORY_POINT = 13;
  private static final int MAX_CHILDREN = 20;

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final ApplicationEventPublisher eventPublisher;
  private final Clock clock;

  SplitBacklogItemCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      SnowflakeIdGenerator snowflakeIdGenerator,
      ApplicationEventPublisher eventPublisher,
      Clock clock) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
    this.eventPublisher = eventPublisher;
    this.clock = clock;
  }

  void execute(SplitBacklogItemCommand command) {
    BacklogItem parent = backlogItemRepository.findById(command.backlogItemId())
        .orElseThrow(() -> new IllegalArgumentException("BacklogItem not found: " + command.backlogItemId()));
    ScrumTeamSubset team = scrumTeamSubsetRepository.findById(parent.getScrumTeamId())
        .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + parent.getScrumTeamId()));
    if (!team.canChangeStatusBy(command.actorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException(
          "Only the Scrum Master or Product Owner of the team can split a backlog item");
    }
    if (parent.getStoryPoint() == null || parent.getStoryPoint() < MIN_SPLIT_STORY_POINT) {
      throw new IllegalArgumentException("BacklogItem story point must be at least " + MIN_SPLIT_STORY_POINT);
    }
    if (command.children() == null || command.children().isEmpty() || command.children().size() > MAX_CHILDREN) {
      throw new IllegalArgumentException("The number of children must be between 1 and " + MAX_CHILDREN);
    }

    List<BacklogItem> children = new ArrayList<>();
    for (SplitBacklogItemCommand.Child child : command.children()) {
      if (child == null) {
        throw new IllegalArgumentException("Child must not be null");
      }
      String title = child.title() != null ? child.title() : parent.getTitle();
      checkNonEmpty(title, () -> new IllegalArgumentException("BacklogItem title must not be empty"));
      BacklogItem item = BacklogItem.builder()
          .id(snowflakeIdGenerator.nextIdValue())
          .title(title)
          .description(parent.getDescription())
          .creatorEmployeeId(parent.getCreatorEmployeeId())
          .creatorName(parent.getCreatorName())
          .scrumTeamId(parent.getScrumTeamId())
          .parentItemId(parent.getId())
          .build();
      item.setStoryPoint(child.storyPoint());
      children.add(item);
    }

    BacklogItemStatus fromStatus = parent.getStatus();
    parent.completeForSplit();
    backlogItemRepository.save(parent);
    backlogItemRepository.saveAll(children);

    LocalDateTime now = LocalDateTime.now(clock);
    eventPublisher.publishEvent(new BacklogItemStatusChanged(
        parent.getId(), parent.getScrumTeamId(), fromStatus, BacklogItemStatus.DONE,
        command.actorEmployeeId(), now));
    eventPublisher.publishEvent(new BacklogItemSplit(
        parent.getId(), parent.getScrumTeamId(), children.stream().map(BacklogItem::getId).toList(),
        command.actorEmployeeId(), now));
  }
}