package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureBacklogItem;

/**
 * バックログアイテムをフィーチャーに割り当てるサービス。
 *
 * <p>同じフィーチャーへの再割り当ては冪等に成功し、別のフィーチャーへの割り当ては409エラー（決定4）。
 */
class AssignBacklogItemToFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final BacklogItemRepository backlogItemRepository;
  private final FeatureBacklogItemRepository featureBacklogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  AssignBacklogItemToFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull BacklogItemRepository backlogItemRepository,
      @NonNull FeatureBacklogItemRepository featureBacklogItemRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.featureRepository = featureRepository;
    this.backlogItemRepository = backlogItemRepository;
    this.featureBacklogItemRepository = featureBacklogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  /**
   * バックログアイテムをフィーチャーに割り当てます。
   *
   * @param command 割り当てコマンド
   */
  public void execute(@NonNull AssignBacklogItemToFeatureCommand command) {
    // Check team exists and permission
    ScrumTeamSubset team = scrumTeamSubsetRepository.findById(command.scrumTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Scrum team not found: " + command.scrumTeamId()));

    if (!team.isTeamMember(command.actorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException("Not a team member");
    }

    // Find feature
    Feature feature = featureRepository.findById(command.featureId())
        .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + command.featureId()));

    // Find backlog item
    BacklogItem backlogItem = backlogItemRepository.findById(command.backlogItemId())
        .orElseThrow(() -> new IllegalArgumentException("Backlog item not found: " + command.backlogItemId()));

    // Check existing assignment
    FeatureBacklogItem existing = featureBacklogItemRepository.findByBacklogItemId(command.backlogItemId())
        .orElse(null);

    if (existing != null) {
      // Already assigned to a feature
      if (existing.getFeatureId().equals(command.featureId())) {
        // Same feature - idempotent, do nothing
        return;
      } else {
        // Different feature - conflict
        throw new BacklogItemAlreadyAssignedToFeatureException(
            "Backlog item is already assigned to another feature: " + existing.getFeatureId());
      }
    }

    // Create new association
    FeatureBacklogItem association = FeatureBacklogItem.builder()
        .featureId(command.featureId())
        .backlogItemId(command.backlogItemId())
        .build();
    featureBacklogItemRepository.save(association);
  }
}
