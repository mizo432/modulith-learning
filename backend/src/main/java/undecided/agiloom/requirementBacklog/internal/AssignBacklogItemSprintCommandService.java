package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSprintAssignmentChanged;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムのスプリント割り当て・変更・解除を行うコマンドサービス（Issue #525）。
 *
 * <p>割り当てはチームメンバー全員が行えます（Issue #620／#717）。対象は ESTIMATED の
 * アイテムのみで、割り当て先は未来4スプリント（進行中を含む。Issue #650）の範囲内に
 * 限ります。割り当ては状態を変えません（Issue #717）。
 */
@Service
@Transactional
class AssignBacklogItemSprintCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final SprintFacade sprintFacade;
  private final ApplicationEventPublisher eventPublisher;

  AssignBacklogItemSprintCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      SprintFacade sprintFacade,
      ApplicationEventPublisher eventPublisher) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.sprintFacade = sprintFacade;
    this.eventPublisher = eventPublisher;
  }

  void assign(@NonNull AssignBacklogItemSprintCommand command) {
    BacklogItem backlogItem = findBacklogItem(command.getBacklogItemId());
    requireTeamMember(backlogItem, command.getActorEmployeeId());
    requireEstimated(backlogItem);

    Sprint sprint =
        sprintFacade
            .findById(command.getSprintId())
            .orElseThrow(
                () -> new IllegalArgumentException("Sprint not found: " + command.getSprintId()));
    if (!backlogItem.getScrumTeamId().equals(sprint.getTeamId())) {
      throw new IllegalArgumentException(
          "Sprint does not belong to the backlog item's scrum team: " + command.getSprintId());
    }
    boolean inWindow =
        sprintFacade.findAssignableSprints(backlogItem.getScrumTeamId()).stream()
            .anyMatch(candidate -> candidate.getId().equals(sprint.getId()));
    if (!inWindow) {
      throw new IllegalArgumentException(
          "Sprint is outside the assignable window (next 4 sprints): " + command.getSprintId());
    }

    SnowflakeId previousSprintId = normalize(backlogItem.getSprintId());
    if (sprint.getId().equals(previousSprintId)) {
      return;
    }
    backlogItem.assignToSprint(sprint.getId());
    backlogItemRepository.save(backlogItem);
    eventPublisher.publishEvent(
        new BacklogItemSprintAssignmentChanged(
            backlogItem.getId(),
            backlogItem.getScrumTeamId(),
            previousSprintId,
            sprint.getId(),
            command.getActorEmployeeId(),
            LocalDateTime.now()));
  }

  void unassign(@NonNull UnassignBacklogItemSprintCommand command) {
    BacklogItem backlogItem = findBacklogItem(command.getBacklogItemId());
    requireTeamMember(backlogItem, command.getActorEmployeeId());
    requireEstimated(backlogItem);

    SnowflakeId previousSprintId = normalize(backlogItem.getSprintId());
    if (previousSprintId == null) {
      throw new IllegalStateException(
          "BacklogItem is not assigned to any sprint: " + command.getBacklogItemId());
    }
    backlogItem.unassignFromSprint();
    backlogItemRepository.save(backlogItem);
    eventPublisher.publishEvent(
        new BacklogItemSprintAssignmentChanged(
            backlogItem.getId(),
            backlogItem.getScrumTeamId(),
            previousSprintId,
            null,
            command.getActorEmployeeId(),
            LocalDateTime.now()));
  }

  /** DBから読み込んだ未設定のIDは SnowflakeId.EMPTY になるため、nullへ正規化する。 */
  private SnowflakeId normalize(SnowflakeId id) {
    return id == null || id.isEmpty() ? null : id;
  }

  private BacklogItem findBacklogItem(SnowflakeId backlogItemId) {
    return backlogItemRepository
        .findById(backlogItemId)
        .orElseThrow(() -> new IllegalArgumentException("BacklogItem not found: " + backlogItemId));
  }

  private void requireTeamMember(BacklogItem backlogItem, SnowflakeId actorEmployeeId) {
    ScrumTeamSubset scrumTeam =
        scrumTeamSubsetRepository
            .findById(backlogItem.getScrumTeamId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ScrumTeam not found: " + backlogItem.getScrumTeamId()));
    if (!scrumTeam.isTeamMember(actorEmployeeId)) {
      throw new BacklogItemPermissionDeniedException(
          "Only team members can change the sprint assignment of a backlog item");
    }
  }

  private void requireEstimated(BacklogItem backlogItem) {
    if (backlogItem.getStatus() != BacklogItemStatus.ESTIMATED) {
      throw new IllegalStateException(
          "Only ESTIMATED backlog items can be assigned to a sprint, current status: "
              + backlogItem.getStatus());
    }
  }
}
