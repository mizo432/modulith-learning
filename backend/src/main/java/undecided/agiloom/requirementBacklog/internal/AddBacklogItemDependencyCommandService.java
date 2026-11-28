package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependency;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependencyDefined;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * バックログアイテムに依存関係を追加するコマンドサービス（Issue #541）。
 *
 * <p>権限は PO・SM のみ（ScrumTeamSubset の canChangeStatusBy）。依存は直接の関係のみで、
 * 循環（相互依存を含む）は検出して拒否します（Issue #665／#670）。
 */
@Service
@Transactional
class AddBacklogItemDependencyCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final BacklogItemDependencyRepository dependencyRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final ApplicationEventPublisher eventPublisher;
  private final Clock clock;

  AddBacklogItemDependencyCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      BacklogItemDependencyRepository dependencyRepository,
      SnowflakeIdGenerator snowflakeIdGenerator,
      ApplicationEventPublisher eventPublisher,
      Clock clock) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.dependencyRepository = dependencyRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
    this.eventPublisher = eventPublisher;
    this.clock = clock;
  }

  SnowflakeId execute(@NonNull AddBacklogItemDependencyCommand command) {
    BacklogItem backlogItem =
        backlogItemRepository
            .findById(command.getBacklogItemId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "BacklogItem not found: " + command.getBacklogItemId()));
    BacklogItem dependencyItem =
        backlogItemRepository
            .findById(command.getDependencyItemId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Dependency backlog item not found: " + command.getDependencyItemId()));

    if (command.getBacklogItemId().equals(command.getDependencyItemId())) {
      throw new IllegalArgumentException("A backlog item cannot depend on itself");
    }
    if (!backlogItem.getScrumTeamId().equals(dependencyItem.getScrumTeamId())) {
      throw new IllegalArgumentException(
          "Dependencies can only be defined between backlog items in the same scrum team");
    }

    ScrumTeamSubset scrumTeam =
        scrumTeamSubsetRepository
            .findById(backlogItem.getScrumTeamId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ScrumTeam not found: " + backlogItem.getScrumTeamId()));
    if (!scrumTeam.canChangeStatusBy(command.getActorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException(
          "Only the Product Owner or Scrum Master can manage backlog item dependencies");
    }

    if (dependencyRepository
        .findByBacklogItemIdAndDependencyItemId(
            command.getBacklogItemId(), command.getDependencyItemId())
        .isPresent()) {
      throw new IllegalStateException("The dependency already exists");
    }
    if (createsCycle(command.getBacklogItemId(), command.getDependencyItemId())) {
      throw new IllegalStateException("The dependency would create a circular dependency");
    }

    BacklogItemDependency dependency =
        BacklogItemDependency.builder()
            .id(snowflakeIdGenerator.nextIdValue())
            .backlogItemId(command.getBacklogItemId())
            .dependencyItemId(command.getDependencyItemId())
            .createdBy(command.getActorEmployeeId())
            .createdAt(LocalDateTime.now(clock))
            .build();
    dependencyRepository.save(dependency);
    eventPublisher.publishEvent(
        new BacklogItemDependencyDefined(
            command.getBacklogItemId(),
            command.getDependencyItemId(),
            backlogItem.getScrumTeamId(),
            command.getActorEmployeeId(),
            LocalDateTime.now(clock)));
    return dependency.getId();
  }

  /**
   * 依存先から依存をたどって依存元に到達するかを深さ優先で判定します。
   *
   * <p>到達する場合は、追加しようとする依存で循環が閉じます。相互依存（依存先が直接依存元に依存）も
   * この判定に含まれます。
   */
  private boolean createsCycle(SnowflakeId dependentItemId, SnowflakeId dependencyItemId) {
    Deque<SnowflakeId> stack = new ArrayDeque<>();
    Set<SnowflakeId> visited = new HashSet<>();
    stack.push(dependencyItemId);
    while (!stack.isEmpty()) {
      SnowflakeId current = stack.pop();
      if (current.equals(dependentItemId)) {
        return true;
      }
      if (!visited.add(current)) {
        continue;
      }
      for (BacklogItemDependency dependency : dependencyRepository.findByBacklogItemId(current)) {
        stack.push(dependency.getDependencyItemId());
      }
    }
    return false;
  }
}
