package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;

/**
 * フィーチャー更新サービス。
 */
class UpdateFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  UpdateFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.featureRepository = featureRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  /**
   * フィーチャーを更新します。
   *
   * @param command 更新コマンド
   */
  public void execute(@NonNull UpdateFeatureCommand command) {
    // Check team exists and permission
    ScrumTeamSubset team = scrumTeamSubsetRepository.findById(command.scrumTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Scrum team not found: " + command.scrumTeamId()));

    if (!team.isTeamMember(command.actorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException("Not a team member");
    }

    // Find feature
    Feature feature = featureRepository.findById(command.featureId())
        .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + command.featureId()));

    // Update name/description
    feature.update(command.name(), command.description());

    // Update status if provided
    if (command.status() != null) {
      feature.changeStatus(command.status());
    }

    featureRepository.save(feature);
  }
}
