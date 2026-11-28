package undecided.agiloom.requirementBacklog.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureBacklogItem;

/**
 * バックログアイテムをフィーチャーから外すサービス。
 */
@Service
@Transactional
class RemoveBacklogItemFromFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final FeatureBacklogItemRepository featureBacklogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  RemoveBacklogItemFromFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull FeatureBacklogItemRepository featureBacklogItemRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.featureRepository = featureRepository;
    this.featureBacklogItemRepository = featureBacklogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  /**
   * バックログアイテムをフィーチャーから外します。
   *
   * @param command 外しコマンド
   */
  public void execute(@NonNull RemoveBacklogItemFromFeatureCommand command) {
    // Check team exists and permission
    ScrumTeamSubset team = scrumTeamSubsetRepository.findById(command.scrumTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Scrum team not found: " + command.scrumTeamId()));

    if (!team.isTeamMember(command.actorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException("Not a team member");
    }

    // Find feature
    Feature feature = featureRepository.findById(command.featureId())
        .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + command.featureId()));

    // Find and delete association
    FeatureBacklogItem association = featureBacklogItemRepository.findByBacklogItemId(command.backlogItemId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Association not found for backlog item: " + command.backlogItemId()));

    featureBacklogItemRepository.delete(association);
  }
}
