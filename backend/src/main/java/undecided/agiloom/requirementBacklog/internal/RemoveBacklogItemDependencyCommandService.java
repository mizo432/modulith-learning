package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependency;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;

/** バックログアイテムの依存関係を削除するコマンドサービス（Issue #541）。権限は PO・SM のみ。 */
@Service
@Transactional
class RemoveBacklogItemDependencyCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final BacklogItemDependencyRepository dependencyRepository;

  RemoveBacklogItemDependencyCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      BacklogItemDependencyRepository dependencyRepository) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.dependencyRepository = dependencyRepository;
  }

  void execute(@NonNull RemoveBacklogItemDependencyCommand command) {
    BacklogItem backlogItem =
        backlogItemRepository
            .findById(command.getBacklogItemId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "BacklogItem not found: " + command.getBacklogItemId()));
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
    BacklogItemDependency dependency =
        dependencyRepository
            .findByBacklogItemIdAndDependencyItemId(
                command.getBacklogItemId(), command.getDependencyItemId())
            .orElseThrow(
                () -> new IllegalArgumentException("Dependency not found for the backlog item"));
    dependencyRepository.delete(dependency);
  }
}
